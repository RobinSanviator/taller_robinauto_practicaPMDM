package com.example.practica_2ev_pmdm_robingonzalez.base_de_datos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;

public class UsuarioConsultas {

    private SQLiteDatabase baseDeDatos;

    public UsuarioConsultas(SQLiteDatabase baseDeDatos) {
        this.baseDeDatos = baseDeDatos;
    }

    // Método para verificar si el tipo de usuario es válido
    public boolean esTipoUsuarioValido(String tipoUsuario) {
        if (tipoUsuario == null) {
            return false;
        }

        // Normalizar el tipo de usuario (mayúsculas/minúsculas no afectarán la validación)
        tipoUsuario = tipoUsuario.trim().toLowerCase();
        switch (tipoUsuario) {
            case "administrador":
            case "administrativo":
            case "mecanico jefe":
            case "mecanico":
            case "cliente":
                return true;
            default:
                return false;
        }
    }

    // Método para insertar un nuevo usuario
    public long insertarUsuario(Usuario usuario) {
        // Verificar que tipo_usuario es válido
        if (!esTipoUsuarioValido(usuario.getTipoUsuario())) {
            Log.e("UsuarioConsultas", "Tipo de usuario no válido");
            return -1;  // No insertamos el usuario si el tipo es inválido
        }

        // Valor por defecto -1
        long resultado = -1;
        ContentValues values = new ContentValues();
        values.put("nombre", usuario.getNombre());
        values.put("apellidos", usuario.getApellidos());
        values.put("correo", usuario.getCorreo());
        values.put("telefono", usuario.getTelefono());
        values.put("contrasenya", usuario.getContrasenya());
        values.put("tipo_usuario", usuario.getTipoUsuario());

        try {
            // Insertar el usuario a la base de datos
            resultado = baseDeDatos.insert("usuarios", null, values);

            // Verificar el resultado
            if (resultado == -1) {
                Log.e("UsuarioConsultas", "Error al insertar el nuevo usuario");
            } else {
                Log.d("UsuarioConsultas", "Usuario insertado con ID: " + resultado);
                // Asignar el ID generado automáticamente al objeto Usuario
                usuario.setIdUsuario((int) resultado);
            }

        } catch (SQLException e) {
            Log.e("UsuarioConsultas", "Error al insertar el nuevo usuario", e);
        }

        return resultado;
    }


    public Usuario obtenerUsuarioPorCorreoYContrasena(String correo, String contrasenya) {
        Usuario usuario = null;
        Cursor cursor = null;

        try {
            cursor = baseDeDatos.rawQuery( "SELECT * FROM usuarios WHERE correo = ? AND contrasenya = ?", new String[]{correo, contrasenya});

            if (cursor != null && cursor.moveToFirst()) {
                usuario = new Usuario();

                // Asignar valores del cursor al objeto Usuario
                int indiceNombre = cursor.getColumnIndex("nombre");
                int indiceApellidos = cursor.getColumnIndex("apellidos");
                int indiceCorreo = cursor.getColumnIndex("correo");
                int indiceTelefono = cursor.getColumnIndex("telefono");
                int indiceContrasenya = cursor.getColumnIndex("contrasenya");
                int indiceTipoUsuario = cursor.getColumnIndex("tipo_usuario");

              if(indiceNombre != -1 && indiceApellidos!= -1 && indiceCorreo != -1
                      && indiceTelefono != -1 && indiceContrasenya != -1  &&  indiceTipoUsuario != -1 ){
                 {
                     usuario.setNombre(cursor.getString(indiceNombre));
                     usuario.setApellidos(cursor.getString(indiceApellidos));
                     usuario.setCorreo(cursor.getString(indiceCorreo));
                     usuario.setTelefono(cursor.getString(indiceTelefono));
                     usuario.setContrasenya(cursor.getString(indiceContrasenya));
                     usuario.setTipoUsuario(cursor.getString(indiceTipoUsuario));
                  }

              }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return usuario;
    }

    public Usuario obtenerUsuarioPorCorreo(String correo) {
        Usuario usuario = null;
        Cursor cursor = null;

        try {
            // Consulta para obtener el usuario solo por correo
            cursor = baseDeDatos.rawQuery("SELECT * FROM usuarios WHERE correo = ?", new String[]{correo});

            if (cursor != null && cursor.moveToFirst()) {
                usuario = new Usuario();

                // Asignar valores del cursor al objeto Usuario
                int indiceNombre = cursor.getColumnIndex("nombre");
                int indiceApellidos = cursor.getColumnIndex("apellidos");
                int indiceCorreo = cursor.getColumnIndex("correo");
                int indiceTelefono = cursor.getColumnIndex("telefono");
                int indiceContrasenya = cursor.getColumnIndex("contrasenya");
                int indiceTipoUsuario = cursor.getColumnIndex("tipo_usuario");

                if (indiceNombre != -1 && indiceApellidos != -1 && indiceCorreo != -1
                        && indiceTelefono != -1 && indiceContrasenya != -1 && indiceTipoUsuario != -1) {
                    usuario.setNombre(cursor.getString(indiceNombre));
                    usuario.setApellidos(cursor.getString(indiceApellidos));
                    usuario.setCorreo(cursor.getString(indiceCorreo));
                    usuario.setTelefono(cursor.getString(indiceTelefono));
                    usuario.setContrasenya(cursor.getString(indiceContrasenya));
                    usuario.setTipoUsuario(cursor.getString(indiceTipoUsuario));
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return usuario;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        // Verificar que el ID del usuario no sea nulo o inválido
        if (usuario.getIdUsuario() == -1) {
            Log.e("InicioSesion", "ID de usuario inválido o no asignado.");
            return false;
        }

        // Crear un objeto ContentValues para almacenar los valores a actualizar
        ContentValues values = new ContentValues();
        values.put("nombre", usuario.getNombre());
        values.put("apellidos", usuario.getApellidos());
        values.put("correo", usuario.getCorreo());
        values.put("telefono", usuario.getTelefono());
        values.put("contrasenya", usuario.getContrasenya());
        values.put("tipo_usuario", usuario.getTipoUsuario());

        // Verificar que la base de datos esté inicializada correctamente
        if (baseDeDatos == null) {
            Log.e("InicioSesion", "La base de datos no está inicializada");
            return false;
        }

        // Intentar actualizar el usuario
        try {
            int rowsAffected = baseDeDatos.update("usuarios", values, "id_usuario = ?", new String[]{String.valueOf(usuario.getIdUsuario())});

            // Comprobar si la actualización afectó alguna fila
            if (rowsAffected > 0) {
                Log.d("InicioSesion", "Usuario actualizado correctamente. Filas afectadas: " + rowsAffected);
                return true;  // Devuelve true si la actualización fue exitosa
            } else {
                Log.e("InicioSesion", "No se actualizó ningún usuario. El ID no coincide");
                return false;  // Devuelve false si no se afectaron filas
            }
        } catch (SQLException e) {
            // Captura y registra cualquier error relacionado con la base de datos
            Log.e("InicioSesion", "Error al actualizar el usuario en SQLite", e);
            return false;
        }
    }

    public String obtenerTipoUsuario(String correo, String contrasenya) {
        Cursor cursor = null;
        String tipoUsuario = null;

        try {
            cursor = baseDeDatos.rawQuery("SELECT tipo_usuario FROM usuarios WHERE correo = ? AND contrasenya = ? ", new String[]{correo, contrasenya});

            if (cursor != null && cursor.moveToFirst()) {
                tipoUsuario = cursor.getString(0);
            }
        } catch (SQLException e) {
            Log.e("UsuarioConsultas", "Error al obtener el tipo de usuario");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return tipoUsuario; // Retornar null si no se encontró el tipo de usuario
    }

    public String obtenerNombreYApellidos(String correo) {
        Cursor cursor = null;
        String nombreYApellidos = null;

        try {
            cursor = baseDeDatos.rawQuery("SELECT nombre, apellidos FROM usuarios WHERE correo = ?", new String[]{correo});

            if (cursor != null && cursor.moveToFirst()) {
                nombreYApellidos = cursor.getString(0) + " " + cursor.getString(1);  // Concatenar nombre y apellidos
            }
        } catch (SQLException e) {
            Log.e("UsuarioConsultas", "Error al obtener nombre y apellidos");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return nombreYApellidos; // Devolver null si no se encontró el nombre y apellidos
    }

    // Método para verificar si un correo ya está registrado
    public boolean correoEnUso(String correo) {
        if (correo == null || correo.isEmpty()) {
            return false;
        }

        String query = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
        try (Cursor cursor = baseDeDatos.rawQuery(query, new String[]{correo})) {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                return count > 0;
            }
        } catch (SQLException e) {
            Log.e("UsuarioConsultas", "Error al verificar si el correo está en uso", e);
        }

        return false;
    }


    public String[] obtenerDatosUsuario(String correo) {
        Cursor cursor = null;
        String[] datosUsuario = null;

        try {
            cursor = baseDeDatos.rawQuery(
                    "SELECT nombre, apellidos, correo, telefono FROM usuarios WHERE correo = ?",
                    new String[]{correo}
            );

            if (cursor != null && cursor.moveToFirst()) {
                int indiceNombre = cursor.getColumnIndex("nombre");
                int indiceApellidos = cursor.getColumnIndex("apellidos");
                int indiceCorreo = cursor.getColumnIndex("correo");
                int indiceTelefono = cursor.getColumnIndex("telefono");

                if (indiceNombre != -1 && indiceApellidos != -1 && indiceCorreo != -1 && indiceTelefono != -1) {
                    datosUsuario = new String[]{
                            cursor.getString(indiceNombre),
                            cursor.getString(indiceApellidos),
                            cursor.getString(indiceCorreo),
                            cursor.getString(indiceTelefono)
                    };
                }
            }
        } catch (SQLException e) {
            Log.e("UsuarioConsultas", "Error al obtener datos del usuario");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return datosUsuario;
    }

}
