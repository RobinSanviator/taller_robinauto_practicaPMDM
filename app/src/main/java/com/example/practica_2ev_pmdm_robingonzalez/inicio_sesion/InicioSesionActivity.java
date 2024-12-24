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

/**
 * La actividad principal de la aplicación, que gestiona la pantalla de inicio de sesión.
 * Esta actividad permite al usuario ingresar su correo y contraseña, además de ofrecer opciones
 * para navegar a la pantalla de registro y a las pantallas de usuario correspondiente (administrador,
 * administrativo, mecánico jefe, mecánico, cliente) y enviar un correo de contacto.
 * @author Robin González
 */
public class InicioSesionActivity extends AppCompatActivity {

    private  TextInputEditText editTextCorreoIS;
    private  TextInputEditText editTextContrasenya;
    private MaterialButton buttonIniciarSesion;
    private  TextView textViewEnlaceRegistro;
    private TextView textViewContactarCorreo;


    /**
     * Método que se ejecuta cuando se crea la actividad.
     * Configura la vista, las referencias de los elementos UI y los listeners para los botones.
     *
     * @param savedInstanceState El estado guardado de la actividad si está disponible.
     */
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

        // Establecer el comportamiento del botón de iniciar sesión
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = editTextCorreoIS.getText().toString();
                String contrasenya = editTextContrasenya.getText().toString();

                //Ocultar el teclado una vez que se haga clic en el botón
                if(v != null){
                    InputMethodManager teclado = (InputMethodManager) getSystemService((Context.INPUT_METHOD_SERVICE));
                    teclado.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                validarUsuario(correo, contrasenya);

            }
        });

        irPantallaRegistro();

    }



    public void irPantallaRegistro(){
        textViewEnlaceRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accederPantallaRegistro = new Intent(InicioSesionActivity.this, RegistroActivity.class);
                startActivity(accederPantallaRegistro);
            }
        });
    }



    /**
     * Método que valida si los campos de correo y contraseña están vacíos en la pantalla de inicio de sesión.
     * Este método puede ser utilizado para habilitar o deshabilitar el botón de inicio de sesión según el estado de los campos.
     */
    public void validarUsuario(String correo, String contrasenya) {
        BBDDUsuariosSQLite baseDeDatosGestionUsuarios = new BBDDUsuariosSQLite(
                InicioSesionActivity.this, "gestion_usuario_taller", null, 3);
        // Verificar si el correo existe
        String correoCorrecto = baseDeDatosGestionUsuarios.verificarCorreo(correo);

        if (correoCorrecto != null) {
            // Obtener tipo de usuario
            String tipoUsuario = baseDeDatosGestionUsuarios.obtenerTipoUsuario(correo, contrasenya);

            if (tipoUsuario != null) {

                switch (tipoUsuario) {
                    case "Administrador":
                    inicializarIntentInicio(AdministradorActivity.class, correo, tipoUsuario);
                        break;
                    case "Administrativo":
                        inicializarIntentInicio(AdministrativoActivity.class, correo, tipoUsuario);
                        break;
                    case "Mecanico jefe":
                        inicializarIntentInicio(MecanicoJefeActivity.class, correo, tipoUsuario);
                        break;
                    case "Mecanico":
                        inicializarIntentInicio(MecanicoActivity.class, correo, tipoUsuario);
                        finish();
                        break;
                    case "Cliente":
                        inicializarIntentInicio(ClienteActivity.class, correo, tipoUsuario);
                        break;
                    default:
                        Snackbar.make(buttonIniciarSesion, "No se ha encontrado ningún usuario válido", Snackbar.LENGTH_LONG).show();
                        break;
                }
            } else {

                Snackbar.make(buttonIniciarSesion, "Error al iniciar sesión, completa correctamente los campos", Snackbar.LENGTH_LONG).show();
            }
        } else {

            Snackbar.make(buttonIniciarSesion, "Error al iniciar sesión, completa correctamente los campos", Snackbar.LENGTH_LONG).show();
        }
    }

    //Método para inicializar el intent y redirigir a la pantalla de cada usuario pasando el correo y la contraseña
    public void inicializarIntentInicio(Class<?> claseDestino, String correo, String tipoUsuario){
        Intent intentPantallaUsuario = new Intent(InicioSesionActivity.this, claseDestino);
        intentPantallaUsuario.putExtra("correo", correo);
        intentPantallaUsuario.putExtra("tipo_usuario", correo);
        startActivity(intentPantallaUsuario);
        finish();
    }

    public void verificarVersionBBDD(){
        //Verificar la version de la base de datos
        BBDDUsuariosSQLite baseDeDatos = new BBDDUsuariosSQLite(this, "gestion_usuario_taller", null, 3);
        int version = baseDeDatos.obtenerVersion();
        Log.d("MainActivity", "Version base de datos " +version);
    }

}