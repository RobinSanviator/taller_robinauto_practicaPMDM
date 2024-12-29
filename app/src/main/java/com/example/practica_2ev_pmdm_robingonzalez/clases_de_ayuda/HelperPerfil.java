package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;

public class HelperPerfil {

    // Método para cargar los datos del usuario en los TextViews
    public void cargarDatosPerfil(String correo, TallerRobinautoSQLite baseDeDatos, TextView textViewNombre, TextView textViewApellido,
                                  TextView textViewCorreo, TextView textViewTelefono) {

        // Llamar al método obtenerDatosUsuario de la base de datos
        String[] datosUsuario = baseDeDatos.obtenerDatosUsuario(correo);

        if (datosUsuario != null) {
            // Asignar los datos obtenidos a los TextViews
            textViewNombre.setText(datosUsuario[0]);
            textViewApellido.setText(datosUsuario[1]);
            textViewCorreo.setText(datosUsuario[2]);
            textViewTelefono.setText(datosUsuario[3]);
        }
    }


}
