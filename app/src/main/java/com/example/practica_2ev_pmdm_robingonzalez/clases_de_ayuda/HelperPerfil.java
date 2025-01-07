package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsultas;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class HelperPerfil {
    private UsuarioConsultas usuarioConsultas;

    // Constructor para inicializar la instancia de UsuarioConsultas
    public HelperPerfil(TallerRobinautoSQLite baseDeDatosGestionUsuarios) {
        if (baseDeDatosGestionUsuarios == null) {
            throw new IllegalArgumentException("La base de datos no puede ser null");
        }
        this.usuarioConsultas = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
    }

    // Método para cargar los datos del usuario en los TextViews
    public void cargarDatosPerfil(String correo, TextView textViewNombre, TextView textViewApellido,
                                  TextView textViewCorreo, TextView textViewTelefono) {

        // Llamar al método obtenerDatosUsuario de la base de datos
        String[] datosUsuario = usuarioConsultas.obtenerDatosUsuario(correo);

        if (datosUsuario != null) {
            // Asignar los datos obtenidos a los TextViews
            textViewNombre.setText(datosUsuario[0]);
            textViewApellido.setText(datosUsuario[1]);
            textViewCorreo.setText(datosUsuario[2]);
            textViewTelefono.setText(datosUsuario[3]);
        } else {
            // Si no encontramos los datos en SQLite, obtenemos los datos de Firebase
            obtenerDatosUsuarioDesdeFirebase(correo, textViewNombre, textViewApellido, textViewCorreo, textViewTelefono);
        }
    }

    // Método para obtener los datos de usuario desde Firebase y actualizar los TextViews
    private void obtenerDatosUsuarioDesdeFirebase(String correo, TextView textViewNombre,
                                                  TextView textViewApellido, TextView textViewCorreo,
                                                  TextView textViewTelefono) {

        DatabaseReference usuariosRef = FirebaseUtils.getDatabaseReference();

        // Buscar el usuario por correo en la base de datos de Firebase
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

    // Método para cargar el nombre y apellidos del usuario en el TextView de la cabecera
    public void cargarDatosPerfilCabeceraDesdeFirebase(String correo, TextView textViewNombreCabecera,
                                                       TextView textViewCorreoCabecera) {
        // Obtener la referencia a la base de datos de Firebase
        DatabaseReference usuariosRef = FirebaseUtils.getDatabaseReference();

        // Buscar el usuario por correo en la base de datos de Firebase
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


