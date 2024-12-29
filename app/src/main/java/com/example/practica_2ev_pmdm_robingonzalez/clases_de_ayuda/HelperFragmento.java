package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;

public class HelperFragmento {
    private AppCompatActivity activityActividad;
    private int frameLayoutContenedorFragmento;

    public HelperFragmento(AppCompatActivity activityActividad,
                           int frameLayoutContenedorFragmento) {
        this.activityActividad = activityActividad;
        this.frameLayoutContenedorFragmento = frameLayoutContenedorFragmento;
    }

    public void cargarFragmento(Fragment fragmento) {
        if (fragmento != null && activityActividad != null) {
            FragmentTransaction fragmentTransaction = activityActividad.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(frameLayoutContenedorFragmento, fragmento);
            fragmentTransaction.addToBackStack(null); //Agregar el fragmento al back stack para que se pueda navegar hacia atrás
            fragmentTransaction.commit();
        }
    }

    // Método para obtener los datos del usuario y establecer el nombre en el TextView
    public void obtenerDatosUsuario(String correo, TextView textViewNombreCabecera) {
        if (activityActividad != null) {
            // Accede a la base de datos desde la actividad
            BBDDUsuariosSQLite baseDeDatosGestionUsuarios = new BBDDUsuariosSQLite(
                    activityActividad, "gestion_usuario_taller", null, 3);

            // Obtiene el nombre completo del usuario desde la base de datos
            String nombreCompleto = baseDeDatosGestionUsuarios.obtenerNombreYApellidos(correo);

            // Si hay un nombre completo, lo establece en el TextView
            if (nombreCompleto != null) {
                textViewNombreCabecera.setText(nombreCompleto);
            } else {
                // Si no se encuentra el nombre, se establece un valor por defecto
                textViewNombreCabecera.setText("Usuario");
            }
        }
    }

}
