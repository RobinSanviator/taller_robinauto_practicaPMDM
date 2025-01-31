package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsulta;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Clase auxiliar para manejar la carga y visualización de datos del perfil de usuario.
 * Proporciona métodos para obtener y mostrar información del usuario desde la base de datos local (SQLite)
 * y la base de datos remota (Firebase).
 */
public class HelperPerfil {

    private UsuarioConsulta usuarioConsulta; // Instancia para realizar consultas de usuario

    /**
     * Constructor del HelperPerfil.
     *
     * @param baseDeDatosGestionUsuarios Instancia de la base de datos SQLite para gestionar usuarios.
     * @throws IllegalArgumentException Si la base de datos es null.
     */
    public HelperPerfil(TallerRobinautoSQLite baseDeDatosGestionUsuarios) {
        if (baseDeDatosGestionUsuarios == null) {
            throw new IllegalArgumentException("La base de datos no puede ser null");
        }
        this.usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
    }

    /**
     * Carga los datos del usuario en los TextViews proporcionados.
     * Primero intenta obtener los datos desde la base de datos local (SQLite).
     * Si no se encuentran, los obtiene desde Firebase.
     *
     * @param correo El correo del usuario cuyos datos se van a cargar.
     * @param textViewNombre TextView para mostrar el nombre del usuario.
     * @param textViewApellido TextView para mostrar los apellidos del usuario.
     * @param textViewCorreo TextView para mostrar el correo del usuario.
     * @param textViewTelefono TextView para mostrar el teléfono del usuario.
     */
    public void cargarDatosPerfil(String correo, TextView textViewNombre, TextView textViewApellido,
                                  TextView textViewCorreo, TextView textViewTelefono) {

        // Obtener los datos del usuario desde SQLite
        String[] datosUsuario = usuarioConsulta.obtenerDatosUsuario(correo);

        if (datosUsuario != null) {
            // Asignar los datos obtenidos a los TextViews
            textViewNombre.setText(datosUsuario[0]);
            textViewApellido.setText(datosUsuario[1]);
            textViewCorreo.setText(datosUsuario[2]);
            textViewTelefono.setText(datosUsuario[3]);
        } else {
            // Si no se encuentran los datos en SQLite, obtenerlos desde Firebase
            obtenerDatosUsuarioDesdeFirebase(correo, textViewNombre, textViewApellido, textViewCorreo, textViewTelefono);
        }
    }

    /**
     * Obtiene los datos del usuario desde Firebase y los asigna a los TextViews proporcionados.
     *
     * @param correo El correo del usuario cuyos datos se van a obtener.
     * @param textViewNombre TextView para mostrar el nombre del usuario.
     * @param textViewApellido TextView para mostrar los apellidos del usuario.
     * @param textViewCorreo TextView para mostrar el correo del usuario.
     * @param textViewTelefono TextView para mostrar el teléfono del usuario.
     */
    private void obtenerDatosUsuarioDesdeFirebase(String correo, TextView textViewNombre,
                                                  TextView textViewApellido, TextView textViewCorreo,
                                                  TextView textViewTelefono) {

        DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();

        // Buscar el usuario por correo en Firebase
        usuariosRef.orderByChild("correo").equalTo(correo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Obtener el usuario desde Firebase
                                Usuario usuario = snapshot.getValue(Usuario.class);

                                if (usuario != null) {
                                    // Actualizar los TextViews con los datos del usuario
                                    textViewNombre.setText(usuario.getNombre());
                                    textViewApellido.setText(usuario.getApellidos());
                                    textViewCorreo.setText(usuario.getCorreo());
                                    textViewTelefono.setText(usuario.getTelefono());
                                }
                            }
                        } else {
                            Log.e("FirebaseQuery", "No se encontraron usuarios con ese correo");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseQuery", "Error al obtener datos: " + databaseError.getMessage());
                    }
                });
    }

    /**
     * Carga el nombre y apellidos del usuario, así como su correo, en los TextViews de la cabecera.
     * Los datos se obtienen desde Firebase.
     *
     * @param correo El correo del usuario cuyos datos se van a cargar.
     * @param textViewNombreCabecera TextView para mostrar el nombre completo del usuario.
     * @param textViewCorreoCabecera TextView para mostrar el correo del usuario.
     */
    public void cargarDatosPerfilCabeceraDesdeFirebase(String correo, TextView textViewNombreCabecera,
                                                       TextView textViewCorreoCabecera) {
        // Obtener la referencia a la base de datos de Firebase
        DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();

        // Buscar el usuario por correo en Firebase
        usuariosRef.orderByChild("correo").equalTo(correo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Obtener el usuario desde Firebase
                                Usuario usuario = snapshot.getValue(Usuario.class);

                                if (usuario != null) {
                                    // Concatenar nombre y apellidos
                                    String nombreCompleto = usuario.getNombre() + " " + usuario.getApellidos();
                                    String correo = usuario.getCorreo();

                                    // Asignar el nombre completo y el correo a los TextViews
                                    textViewNombreCabecera.setText(nombreCompleto);
                                    textViewCorreoCabecera.setText(correo);
                                }
                            }
                        } else {
                            Log.d("FirebaseQuery", "No se encontraron usuarios con ese correo.");
                            Snackbar.make(((Activity) textViewNombreCabecera.getContext()).findViewById(android.R.id.content),
                                    "No se encontraron usuarios con ese correo.", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseQuery", "Error al obtener datos: " + databaseError.getMessage());
                    }
                });
    }
}

