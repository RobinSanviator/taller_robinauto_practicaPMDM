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

/**
 * Clase auxiliar para manejar la carga de fragmentos y la obtención de datos del usuario
 * en la actividad principal del menú.
 * Proporciona métodos para cargar fragmentos en un contenedor específico y para obtener
 * y mostrar el nombre del usuario desde la base de datos local (SQLite) o remota (Firebase).
 */
public class HelperMenuPrincipal {

    private AppCompatActivity activityActividad; // Actividad principal que utiliza este helper
    private int frameLayoutContenedorFragmento; // ID del contenedor de fragmentos

    /**
     * Constructor del HelperMenuPrincipal.
     *
     * @param activityActividad La actividad principal que utiliza este helper.
     * @param frameLayoutContenedorFragmento El ID del contenedor de fragmentos donde se cargarán los fragmentos.
     */
    public HelperMenuPrincipal(AppCompatActivity activityActividad, int frameLayoutContenedorFragmento) {
        this.activityActividad = activityActividad;
        this.frameLayoutContenedorFragmento = frameLayoutContenedorFragmento;
    }

    /**
     * Carga un fragmento en el contenedor especificado.
     * Realiza una transacción de fragmento y lo añade a la pila de retroceso.
     *
     * @param fragmento El fragmento que se va a cargar.
     */
    public void cargarFragmento(Fragment fragmento) {
        if (fragmento != null && !activityActividad.isFinishing() && !activityActividad.isDestroyed()) {
            if (!activityActividad.isFinishing()) {
                FragmentTransaction fragmentTransaction = activityActividad.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(frameLayoutContenedorFragmento, fragmento);
                fragmentTransaction.addToBackStack(null); // Añadir a la pila de retroceso
                fragmentTransaction.commit();
            } else {
                Log.e("HelperMenuPrincipal", "No se puede realizar la transacción");
            }
        }
    }

    /**
     * Obtiene los datos del usuario desde la base de datos local (SQLite) y establece el nombre en un TextView.
     *
     * @param correo El correo del usuario para buscar en la base de datos.
     * @param textViewNombreCabecera El TextView donde se mostrará el nombre del usuario.
     */
    public void obtenerDatosUsuario(String correo, TextView textViewNombreCabecera) {
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

    /**
     * Obtiene los datos del usuario desde Firebase y establece el nombre en un TextView.
     *
     * @param correo El correo del usuario para buscar en Firebase.
     * @param textViewNombreCabecera El TextView donde se mostrará el nombre del usuario.
     */
    public void cargarNombreCabeceraDesdeFirebase(String correo, TextView textViewNombreCabecera) {
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