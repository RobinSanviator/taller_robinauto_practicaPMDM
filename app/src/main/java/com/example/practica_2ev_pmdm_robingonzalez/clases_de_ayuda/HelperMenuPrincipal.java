package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsultas;

public class HelperMenuPrincipal {
    private AppCompatActivity activityActividad;
    private int frameLayoutContenedorFragmento;

    public HelperMenuPrincipal(AppCompatActivity activityActividad,
                               int frameLayoutContenedorFragmento) {
        this.activityActividad = activityActividad;
        this.frameLayoutContenedorFragmento = frameLayoutContenedorFragmento;
    }

    public void cargarFragmento(Fragment fragmento) {
        if (fragmento != null && activityActividad != null) {
            if (!activityActividad.isDestroyed() && !activityActividad.isFinishing()) {
                FragmentTransaction fragmentTransaction = activityActividad.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(frameLayoutContenedorFragmento, fragmento);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                Log.e("HelperFragmento", "No se puede realizar la transacción");
            }
        }
    }

    // Método para obtener los datos del usuario y establecer el nombre en el TextView
    public void obtenerDatosUsuario(String correo, TextView textViewNombreCabecera) {
        if (activityActividad != null) {
            // Accede a la base de datos desde la actividad
            TallerRobinautoSQLite baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(activityActividad);
            UsuarioConsultas usuarioConsultas = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();

            // Obtiene el nombre completo del usuario desde la base de datos
            String nombreCompleto = usuarioConsultas.obtenerNombreYApellidos(correo);

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
