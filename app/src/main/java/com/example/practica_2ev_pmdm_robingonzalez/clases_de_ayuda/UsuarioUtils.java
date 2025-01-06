package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.util.Log;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;

import java.util.LinkedHashMap;
import java.util.Map;

public class UsuarioUtils {

    // Método estático que convierte un objeto Usuario a un Map<String, Object>
    public static Map<String, Object> anadirUsuarioFirebase(Usuario usuario) {
        // Obtener los datos del usuario y convertirlos a un Map
        Map<String, Object> mapUsuario = new LinkedHashMap<>();
        mapUsuario.put("nombre", usuario.getNombre());
        mapUsuario.put("apellidos", usuario.getApellidos());
        mapUsuario.put("correo", usuario.getCorreo());
        mapUsuario.put("telefono", usuario.getTelefono());
        mapUsuario.put("contrasenya", usuario.getContrasenya());
        mapUsuario.put("tipoUsuario", usuario.getTipoUsuario());

        Log.d("Map Usuario", "Map Usuario: " + mapUsuario.toString());  // Verifica el contenido del Map

        return mapUsuario;
    }


}
