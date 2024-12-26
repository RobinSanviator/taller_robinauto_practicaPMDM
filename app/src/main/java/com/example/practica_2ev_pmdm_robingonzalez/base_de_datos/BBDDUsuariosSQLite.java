package com.example.practica_2ev_pmdm_robingonzalez.base_de_datos;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class BBDDUsuariosSQLite extends SQLiteOpenHelper {

    //String sqlCreacion = "CREATE DATABASE gestion_usuario_taller";
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
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS usuarios");
            db.execSQL("DROP TABLE IF EXISTS historial_cambios");
            onCreate(db);
        }

    }



    public int obtenerVersion() {
        SQLiteDatabase db = this.getReadableDatabase();
        int version = db.getVersion();
        db.close();
        return version;
    }

    public String obtenerTipoUsuario(String correo, String contrasenya) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String tipoUsuario = null;

        try {

            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT tipo_usuario FROM usuarios WHERE correo = ? AND contrasenya = ? ", new String[]{correo, contrasenya});

            if (cursor != null && cursor.moveToFirst()) {
                tipoUsuario = cursor.getString(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();

        }

        return tipoUsuario; // Retornar null si no se encontró el tipo de usuario
    }

    public String verificarCorreo(String correo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String correoCorrecto = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT correo FROM usuarios WHERE correo = ?", new String[]{correo});

            if (cursor!= null && cursor.moveToFirst()) {
                correoCorrecto = cursor.getString(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();

        }

        return correoCorrecto; // Retornar null si no se encontró el correo
    }

    public String obtenerNombreYApellidos(String correo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String nombreYApellidos = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT nombre, apellidos FROM usuarios WHERE correo = ?", new String[]{correo});

            if (cursor != null && cursor.moveToFirst()) {
                nombreYApellidos = cursor.getString(0) + " " + cursor.getString(1);  // Concatenar nombre y apellidos
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();

        }

        return nombreYApellidos; // Devolver null si no se encontró el nombre y apellidos
    }

    public String correoEnUso(String correo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String correoEnUso = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT correo FROM usuarios WHERE correo = ?", new String[]{correo});

            if (cursor != null && cursor.moveToFirst()) {
                correoEnUso = cursor.getString(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            cursor.close();
            db.close();

        }

        return correoEnUso; // Retornar null si no se encuentra el correo
    }

}
