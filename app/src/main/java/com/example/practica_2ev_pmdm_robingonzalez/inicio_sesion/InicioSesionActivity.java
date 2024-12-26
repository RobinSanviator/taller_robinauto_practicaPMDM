package com.example.practica_2ev_pmdm_robingonzalez.inicio_sesion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.practica_2ev_pmdm_robingonzalez.cliente.ClienteActivity;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorActivity;
import com.example.practica_2ev_pmdm_robingonzalez.administrativo.AdministrativoActivity;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.mecanico.MecanicoActivity;
import com.example.practica_2ev_pmdm_robingonzalez.mecanico_jefe.MecanicoJefeActivity;
import com.example.practica_2ev_pmdm_robingonzalez.registro.RegistroActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class InicioSesionActivity extends AppCompatActivity {

    private TextInputEditText editTextCorreoIS;
    private TextInputEditText editTextContrasenya;
    private MaterialButton buttonIniciarSesion;
    private TextView textViewEnlaceRegistro;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.inicio_sesion_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //Referencias a los elementos de la pantalla de inicio de sesión
        editTextCorreoIS = findViewById(R.id.editTextCorreoIS);
        editTextContrasenya = findViewById(R.id.editTextContrasenyaIS);
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion);
        textViewEnlaceRegistro = findViewById(R.id.textViewEnlaceRegistro);

        iniciarSesion();
        irPantallaRegistro();

    }

    public void iniciarSesion() {
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = editTextCorreoIS.getText().toString();
                String contrasenya = editTextContrasenya.getText().toString();

                validarUsuario(correo, contrasenya);
            }
        });
    }

    public void irPantallaRegistro() {
        textViewEnlaceRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(InicioSesionActivity.this, RegistroActivity.class));
            }
        });
    }

    // Método para validar al usuario en la base de datos
    public void validarUsuario(String correo, String contrasenya) {
        BBDDUsuariosSQLite baseDeDatosGestionUsuarios = new BBDDUsuariosSQLite(
                InicioSesionActivity.this, "gestion_usuario_taller", null, 3);

        String correoCorrecto = baseDeDatosGestionUsuarios.verificarCorreo(correo);

        if (correoCorrecto != null) {
            String tipoUsuario = baseDeDatosGestionUsuarios.obtenerTipoUsuario(correo, contrasenya);
            if (tipoUsuario != null) {
                // Redirigir según el tipo de usuario
                navegarPorTipoUsuario(tipoUsuario, correo);
            } else {
                Snackbar.make(buttonIniciarSesion, "Error al iniciar sesión, verifica los datos", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(buttonIniciarSesion, "Error al iniciar sesión, verifica los datos", Snackbar.LENGTH_LONG).show();
        }
    }

    // Método para redirigir según el tipo de usuario
    public void navegarPorTipoUsuario(String tipoUsuario, String correo) {
        Intent intentPantallaUsuario;
        switch (tipoUsuario) {
            case "Administrador":
                intentPantallaUsuario = new Intent(InicioSesionActivity.this, AdministradorActivity.class);
                break;
            case "Administrativo":
                intentPantallaUsuario = new Intent(InicioSesionActivity.this, AdministrativoActivity.class);
                break;
            case "Mecanico jefe":
                intentPantallaUsuario = new Intent(InicioSesionActivity.this, MecanicoJefeActivity.class);
                break;
            case "Mecanico":
                intentPantallaUsuario = new Intent(InicioSesionActivity.this, MecanicoActivity.class);
                break;
            case "Cliente":
                intentPantallaUsuario = new Intent(InicioSesionActivity.this, ClienteActivity.class);
                break;
            default:
                Snackbar.make(buttonIniciarSesion, "Tipo de usuario no reconocido.", Snackbar.LENGTH_LONG).show();
                return;
        }
        intentPantallaUsuario.putExtra("correo", correo);
        intentPantallaUsuario.putExtra("tipo_usuario", tipoUsuario);
        startActivity(intentPantallaUsuario);
        finish();
    }

    public void verificarVersionBBDD () {
            //Verificar la version de la base de datos
            BBDDUsuariosSQLite baseDeDatos = new BBDDUsuariosSQLite(this, "gestion_usuario_taller", null, 3);
            int version = baseDeDatos.obtenerVersion();
            Log.d("MainActivity", "Version base de datos " + version);
        }

    }
