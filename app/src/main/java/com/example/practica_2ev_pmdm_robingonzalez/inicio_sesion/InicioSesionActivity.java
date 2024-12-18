package com.example.practica_2ev_pmdm_robingonzalez.inicio_sesion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private boolean correoEnviado = false;

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
        setContentView(R.layout.activity_inicio_sesion);
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
                String contrasenya =  editTextContrasenya.getText().toString();

                validarUsuario(correo,contrasenya);

            }
        });

        irPantallaRegistro();
        enviarCorreoContacto();

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

    public void enviarCorreoContacto(){
        // Referencia al ImageView para contactar por correo
        textViewContactarCorreo = findViewById(R.id.textViewContactarIs);


        // Establecer el comportamiento al hacer clic en el ImageView para enviar un correo
        textViewContactarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Crear un Intent para enviar un correo con Gmail
                    Intent intentCorreo = new Intent(Intent.ACTION_SENDTO);
                    intentCorreo.setData(Uri.parse("mailto:tallerrobinauto@gmail.com"));  // Esta línea está bien con mailto:

                    // Usar un chooser para que el usuario pueda elegir qué app usar para enviar el correo
                    startActivity(Intent.createChooser(intentCorreo, "Elige una aplicación de correo"));
                    correoEnviado = true;

                } catch(Exception e){
                    Snackbar.make(v, "Error al enviar el mensaje", Snackbar.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(correoEnviado){
            Snackbar.make(textViewContactarCorreo, "Mensaje enviado con éxito", Snackbar.LENGTH_LONG).show();
            correoEnviado = false;
        }
    }

    /**
     * Método que valida si los campos de correo y contraseña están vacíos en la pantalla de inicio de sesión.
     * Este método puede ser utilizado para habilitar o deshabilitar el botón de inicio de sesión según el estado de los campos.
     */
    public void validarUsuario(String correo, String contrasenya) {
        BBDDUsuariosSQLite baseDeDatosGestionUsuarios = new BBDDUsuariosSQLite(
                InicioSesionActivity.this, "gestion_usuario_taller", null, 3);

        //SQLiteDatabase baseDeDatos = baseDeDatosGestionUsuarios.getReadableDatabase();
        //baseDeDatos.close();


        // Verificar si el correo existe
        String correoCorrecto = baseDeDatosGestionUsuarios.verificarCorreo(correo);


        if (correoCorrecto != null) {
            // Si el correo existe, verificar la contraseña
            String tipoUsuario = baseDeDatosGestionUsuarios.obtenerTipoUsuario(correo, contrasenya);



            if (tipoUsuario != null) {

                switch (tipoUsuario) {
                    case "Administrador":
                    inicializarIntentInicio(AdministradorActivity.class, correo, contrasenya);
                        break;

                    case "Administrativo":
                        inicializarIntentInicio(AdministrativoActivity.class, correo, contrasenya);
                        break;

                    case "Mecanico jefe":
                        inicializarIntentInicio(MecanicoJefeActivity.class, correo, contrasenya);
                        break;

                    case "Mecanico":
                        inicializarIntentInicio(MecanicoActivity.class, correo, contrasenya);
                        finish();
                        break;

                    case "Cliente":
                        inicializarIntentInicio(ClienteActivity.class, correo, contrasenya);
                        break;

                    default:
                        Snackbar.make(buttonIniciarSesion, "No se ha encontrado ningún usuario válido", Snackbar.LENGTH_LONG).show();
                        break;
                }
            } else {

                Snackbar.make(buttonIniciarSesion, "Contraseña incorrecta", Snackbar.LENGTH_LONG).show();
            }
        } else {

            Snackbar.make(buttonIniciarSesion, "El correo no es válido", Snackbar.LENGTH_LONG).show();
        }



    }

    //Método para inicializar el intent y redirigir a la pantalla de cada usuario pasando el correo y la contraseña
    public void inicializarIntentInicio(Class<?> claseDestino, String correo, String contrasenya) {
        Intent intentPantallaUsuario = new Intent(InicioSesionActivity.this, claseDestino);
        intentPantallaUsuario.putExtra("correo", correo);
        intentPantallaUsuario.putExtra("contrasenya", contrasenya);
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