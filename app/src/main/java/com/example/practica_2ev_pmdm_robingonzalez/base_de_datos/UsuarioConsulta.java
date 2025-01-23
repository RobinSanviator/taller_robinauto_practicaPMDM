package com.example.practica_2ev_pmdm_robingonzalez.base_de_datos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;

public class UsuarioConsulta {

    private SQLiteDatabase baseDeDatos;

    public UsuarioConsulta(SQLiteDatabase baseDeDatos) {
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

        try (Cursor cursor = baseDeDatos.rawQuery("SELECT * FROM usuarios WHERE correo = ? AND contrasenya = ?", new String[]{correo, contrasenya})) {

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
        }

        return usuario;
    }

    public Usuario obtenerUsuarioPorCorreo(String correo) {
        Usuario usuario = null;

        try (Cursor cursor = baseDeDatos.rawQuery("SELECT * FROM usuarios WHERE correo = ?", new String[]{correo})) {
            // Consulta para obtener el usuario solo por correo

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
        }

        return usuario;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        // Verificar si el correo del usuario existe en SQLite
        if (usuario.getCorreo() == null || usuario.getCorreo().isEmpty()) {
            Log.e("ActualizarUsuario", "Correo del usuario no válido.");
            return false;
        }

        // Comprobar si el usuario existe en SQLite
        if (!usuarioExisteEnSQLite(usuario.getCorreo())) {
            return false; // Si el usuario no existe en SQLite
        }

        // Verificar que el ID del usuario no sea nulo o inválido
        if (usuario.getIdUsuario() == 0) {  // Usar 0 si el ID no es válido
            Log.e("ActualizarUsuario", "ID de usuario inválido o no asignado.");
            return false;
        }

        // Crear un objeto ContentValues para almacenar los valores a actualizar
        ContentValues values = new ContentValues();
        values.put("nombre", usuario.getNombre());
        values.put("apellidos", usuario.getApellidos());
        values.put("telefono", usuario.getTelefono());

        // Verificar que la base de datos esté inicializada correctamente
        if (baseDeDatos == null) {
            Log.e("ActualizarUsuario", "La base de datos no está inicializada");
            return false;
        }

        // Intentar actualizar el usuario en SQLite
        try {
            int rowsAffected = baseDeDatos.update("usuarios", values, "correo = ?", new String[]{usuario.getCorreo()});

            // Comprobar si la actualización afectó alguna fila
            if (rowsAffected > 0) {
                Log.d("ActualizarUsuario", "Usuario actualizado correctamente. Filas afectadas: " + rowsAffected);
                return true;  // Si se actualizó correctamente, devuelve true
            } else {
                Log.e("ActualizarUsuario", "No se actualizó ningún usuario. El correo no coincide o la actualización falló.");
                return false;  // Si no se afectaron filas, retorna false
            }
        } catch (SQLException e) {
            // Captura y registra cualquier error relacionado con la base de datos
            Log.e("ActualizarUsuario", "Error al actualizar el usuario en SQLite", e);
            return false;
        }
    }

    // Método adicional para verificar si el usuario existe en SQLite por correo
    private boolean usuarioExisteEnSQLite(String correo) {
        try (Cursor cursor = baseDeDatos.rawQuery("SELECT 1 FROM usuarios WHERE correo = ?", new String[]{correo})) {
            // Verificamos si existe el correo en la base de datos
            // Si no encontramos el correo, el usuario no existe
            return cursor != null && cursor.moveToFirst();  // Si encontramos un resultado, el usuario existe
        } catch (SQLException e) {
            Log.e("ActualizarUsuario", "Error al verificar existencia de usuario en SQLite", e);
            return false;
        }
        // Cerramos el cursor si fue abierto
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

    public boolean eliminarUsuarioSQLite(String correo) {
        boolean eliminado = false;

        // Verificar si el correo es válido
        if (correo == null || correo.isEmpty()) {
            Log.e("UsuarioConsultas", "Correo no válido");
            return false;
        }

        // Verificar si el correo existe en la base de datos
        if (!existeCorreoEnBaseDeDatos(correo)) {
            Log.d("UsuarioConsultas", "El usuario no está registrado en la base de datos local.");
            return false; // Si no existe, no intentar eliminar
        }

        try {
            // Ejecutar la consulta de eliminación usando el correo
            int filasAfectadas = baseDeDatos.delete(
                    "usuarios",  // Nombre de la tabla
                    "correo = ?",  // Condición WHERE
                    new String[]{correo}  // Valor del correo
            );

            // Verificar si se eliminó alguna fila
            if (filasAfectadas > 0) {
                Log.d("UsuarioConsultas", "Usuario eliminado correctamente.");
                eliminado = true;
            } else {
                Log.e("UsuarioConsultas", "No se encontró un usuario con ese correo.");
            }
        } catch (SQLException e) {
            Log.e("UsuarioConsultas", "Error al eliminar el usuario", e);
        }

        return eliminado;
    }

    // Método adicional para verificar si el correo existe en la base de datos
    private boolean existeCorreoEnBaseDeDatos(String correo) {
        Cursor cursor = null;
        boolean existe = false;
        try {
            cursor = baseDeDatos.query("usuarios",
                    new String[] {"correo"},
                    "correo = ?",
                    new String[] {correo},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                existe = true; // Si se encuentra el correo, entonces existe en la base de datos
            }
        } catch (SQLException e) {
            Log.e("UsuarioConsultas", "Error al verificar la existencia del correo", e);
        } finally {
            if (cursor != null) {
                cursor.close(); // Asegúrate de cerrar el cursor
            }
        }
        return existe;
    }
}
