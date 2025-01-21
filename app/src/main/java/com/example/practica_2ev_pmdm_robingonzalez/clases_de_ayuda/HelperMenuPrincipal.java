package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsulta;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class HelperMenuPrincipal {
    private AppCompatActivity activityActividad;
    private int frameLayoutContenedorFragmento;

    public HelperMenuPrincipal(AppCompatActivity activityActividad,
                               int frameLayoutContenedorFragmento) {
        this.activityActividad = activityActividad;
        this.frameLayoutContenedorFragmento = frameLayoutContenedorFragmento;
    }

    public void cargarFragmento(Fragment fragmento) {

            if (fragmento != null && !activityActividad.isFinishing() && !activityActividad.isDestroyed()) {

                if (!activityActividad.isFinishing()) {
                    FragmentTransaction fragmentTransaction = activityActividad.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(frameLayoutContenedorFragmento, fragmento);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Log.e("HelperMenuPrincipal", "No se puede realizar la transacción");
                }
            }
        }

        // Método para obtener los datos del usuario y establecer el nombre en el TextView
        public void obtenerDatosUsuario (String correo, TextView textViewNombreCabecera){
            if (activityActividad != null) {
                // Accede a la base de datos desde la actividad
                TallerRobinautoSQLite baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(activityActividad);
                UsuarioConsulta usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();

                // Obtiene el nombre completo del usuario desde la base de datos
                String nombreCompleto = usuarioConsulta.obtenerNombreYApellidos(correo);

                // Si hay un nombre completo, lo establece en el TextView
                if (nombreCompleto != null) {
                    textViewNombreCabecera.setText(nombreCompleto);
                } else {
                    // Si no se encuentra el nombre, se establece un valor por defecto
                    textViewNombreCabecera.setText("Usuario");
                }
            }
        }

        // Método para cargar el nombre y apellidos del usuario en el TextView de la cabecera
        public void cargarNombreCabeceraDesdeFirebase (String correo, TextView
        textViewNombreCabecera){
            // Obtener la referencia a la base de datos de Firebase
            DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();

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
                                        // Asignar el nombre completo
                                        textViewNombreCabecera.setText(nombreCompleto);

                                    }
                                }
                            } else {
                                Log.d("FirebaseQuery", "No se encontraron usuarios con ese correo.");

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("FirebaseQuery", "Error al obtener datos: " + databaseError.getMessage());
                        }
                    });
        }


}
