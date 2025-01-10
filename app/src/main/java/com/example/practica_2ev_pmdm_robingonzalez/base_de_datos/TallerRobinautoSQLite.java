package com.example.practica_2ev_pmdm_robingonzalez.base_de_datos;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class TallerRobinautoSQLite extends SQLiteOpenHelper {

    private static TallerRobinautoSQLite instance;
    private static final String NOMBRE_BASE_DE_DATOS ="gestion_usuario_taller";
    private static final int VERSION_BASE_DE_DATOS = 7;

    //String sqlCreacion = "CREATE DATABASE gestion_usuario_taller";
    String sqlCreacionTablaUsuarios = "CREATE TABLE usuarios(id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT," +
            "apellidos TEXT," +
            "correo TEXT UNIQUE NOT NULL," +
            "telefono TEXT," +
            "contrasenya TEXT,"+
            "tipo_usuario TEXT CHECK(tipo_usuario IN('Administrador', 'Administrativo', 'Mecanico jefe', 'Mecanico', 'Cliente')));";


    String sqlBorradoTablaUsuarios = "DROP TABLE IF EXISTS usuarios;";
    String sqlBorradoTablaHistorialCambios = "DROP TABLE IF EXISTS historial_cambios;";




    public TallerRobinautoSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreacionTablaUsuarios);
        Log.d("TallerRobinautoSQLite", "Base de datos creada con éxito");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {

        }
    }


    //Se implementa el patrón Singletone para gestionar una única instancia de TallerRobinautoSQLite en toda la aplicación
    public static synchronized TallerRobinautoSQLite getInstance(Context contexto){
        if(instance == null){
            try {
                // Intentar crear la nueva instancia de la base de datos
                instance = new TallerRobinautoSQLite(contexto.getApplicationContext(),
                        NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS);
            } catch (SQLException e) {
                Log.e("TallerRobinautoSQLite", "Error al crear la instancia de la base de datos");
                e.printStackTrace();
            }
        }

        return instance;
    }


    // Método para obtener la instancia de UsuarioConsultas en TallerRobinautoSQLite
    public UsuarioConsulta obtenerUsuarioConsultas() {
        SQLiteDatabase baseDeDatos = this.getWritableDatabase(); // Obtener la base de datos en modo lectura y escritura
        return new UsuarioConsulta(baseDeDatos);
    }


}
