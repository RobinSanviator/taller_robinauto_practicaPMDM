package com.example.practica_2ev_pmdm_robingonzalez.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BBDDUsuariosSQLite extends SQLiteOpenHelper {

    String sqlCreacion = "CREATE DATABASE gestion_usuario_taller";
    String sqlCreacionTablaUsuarios = "CREATE TABLE usuarios(id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT," +
            "apellidos TEXT," +
            "correo TEXT UNIQUE NOT NULL," +
            "telefono TEXT," +
            "contrasenya TEXT,"+
            "tipo_usuario TEXT CHECK(tipo_usuario IN('Administrador', 'Administrativo', 'Mecanico jefe', 'Mecanico', 'Cliente')));";

    String sqlCreacionTablaHistorial = "CREATE TABLE historial_cambios (" +
            "id_cambio INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Identificador único de cada cambio
            "id_usuario INTEGER NOT NULL, " +  // Referencia al usuario afectado por el cambio
            "tipo_cambio TEXT NOT NULL CHECK(tipo_cambio IN ('Alta', 'Modificación', 'Baja')), " +  // Tipo de cambio
            "fecha_cambio DATETIME DEFAULT CURRENT_TIMESTAMP, " +  // Fecha del cambio (automáticamente la hora actual)
            "FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE);";  // Clave foránea que referencia la tabla usuarios

    String sqlBorradoTablaUsuarios = "DROP TABLE IF EXISTS usuarios;";
    String sqlBorradoTablaHistorialCambios = "DROP TABLE IF EXISTS historial_cambios;";

    public BBDDUsuariosSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreacionTablaUsuarios);
        db.execSQL(sqlCreacionTablaHistorial);
        Log.d("BBDDUsuariosSQLite", "Base de datos creada");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }



    public int obtenerVersion() {
        SQLiteDatabase db = this.getReadableDatabase();
        int version = db.getVersion();
        db.close();
        return version;
    }

    public String obtenerTipoUsuario(String correo, String contrasenya){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT tipo_usuario FROM usuarios WHERE correo = ? AND contrasenya = ?", new String[]{correo, contrasenya});

        if(cursor.moveToFirst()){
            String tipoUsuario = cursor.getString(0);
            // Log para verificar el resultado
           // Log.d("BBDDUsuariosSQLite", "Tipo de usuario encontrado: " + tipoUsuario);
            cursor.close();
            return tipoUsuario;

        } else{
         cursor.close();
         return null;
        }

    }

    public String verificarCorreo(String correo){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCorreo = db.rawQuery("SELECT correo FROM usuarios WHERE correo = ?", new String[]{correo});
        if(cursorCorreo.moveToFirst()){
            String correoCorrecto = cursorCorreo.getString(0);
            cursorCorreo.close();
            db.close();
            return correoCorrecto;
        }
        return  null;
    }

    public String obtenerNombreYApellidos(String correo, String contrasenya) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Consulta para obtener el nombre y apellidos del usuario
        Cursor cursor = db.rawQuery("SELECT nombre, apellidos FROM usuarios WHERE correo = ? AND contrasenya = ?",
                new String[]{correo, contrasenya});

        if (cursor.moveToFirst()) {
            // Si se encuentra el usuario, concatenamos el nombre y los apellidos
            String nombreYApellidos = cursor.getString(0) + " " + cursor.getString(1);  // Concatenar nombre y apellidos
            cursor.close();
            return nombreYApellidos;  // Devolver el nombre y apellidos concatenados
        } else {
            cursor.close();
            return null;  // Si no se encuentra el usuario, devolver null
        }
    }

    public String correoEnUso(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

            // Consulta para buscar el correo exacto
            cursor = db.rawQuery("SELECT correo FROM usuarios WHERE correo = ?", new String[]{correo});
            if (cursor.moveToFirst()) {
                // Devolver el correo si existe
                return cursor.getString(0);
            } else {

                cursor.close();
            }
            return null;
    }
}
