package com.example.practica_2ev_pmdm_robingonzalez.vista.inicio_sesion;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsulta;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.vista.cliente.ClienteActivity;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.vista.administrador.AdministradorActivity;
import com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo.AdministrativoActivity;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico.MecanicoActivity;
import com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico_jefe.MecanicoJefeActivity;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.example.practica_2ev_pmdm_robingonzalez.vista.registro.RegistroActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Activity para el inicio de sesión del usuario.
 * Permite que el usuario ingrese su correo y contraseña y se autentique, ya sea desde SQLite o Firebase.
 */
public class InicioSesionActivity extends AppCompatActivity {

    private TextInputEditText editTextCorreoIS;  // Campo para el correo
    private TextInputEditText editTextContrasenya;  // Campo para la contraseña
    private MaterialButton buttonIniciarSesion;  // Botón para iniciar sesión
    private TextView textViewEnlaceRegistro;  // Enlace para ir a la pantalla de registro
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;  // Instancia de la base de datos
    private UsuarioConsulta usuarioConsulta;  // Consulta de usuarios en la base de datos

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa los componentes de la interfaz, la base de datos, y configura los eventos de la actividad.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Habilita la funcionalidad de borde a borde
        setContentView(R.layout.inicio_sesion_activity);  // Configura el layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Ajusta el padding para los elementos de la UI según las barras del sistema
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponenetes();  // Inicializa los elementos de la UI
        inicializarBaseDeDatos();  // Inicializa la base de datos
        iniciarSesion();  // Configura la acción para iniciar sesión
        irPantallaRegistro();  // Configura la acción para ir a la pantalla de registro
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     * Asocia los elementos visuales con las variables correspondientes.
     */
    private void inicializarComponenetes(){
        editTextCorreoIS = findViewById(R.id.editTextCorreoIS);  // Correo
        editTextContrasenya = findViewById(R.id.editTextContrasenyaIS);  // Contraseña
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion);  // Botón de inicio de sesión
        textViewEnlaceRegistro = findViewById(R.id.textViewEnlaceRegistro);  // Enlace para ir al registro
    }

    /**
     * Inicializa la base de datos para la gestión de usuarios.
     * Obtiene la instancia de la base de datos y la consulta de usuarios.
     */
    private void inicializarBaseDeDatos(){
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(InicioSesionActivity.this);  // Instancia de la base de datos
        usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();  // Instancia de las consultas de usuarios
    }

    /**
     * Configura el evento del botón para iniciar sesión.
     * Cuando se hace clic, valida los campos y obtiene el usuario de la base de datos.
     */
    private void iniciarSesion() {
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarTeclado();  // Cierra el teclado al hacer clic en el botón

                String correo = editTextCorreoIS.getText().toString();  // Obtiene el correo
                String contrasenya = editTextContrasenya.getText().toString();  // Obtiene la contraseña
                obtenerUsuarioDesdeSQLite(correo, contrasenya);  // Busca al usuario en la base de datos
            }
        });
    }

    /**
     * Cierra el teclado cuando se hace clic fuera del campo de texto.
     */
    private void cerrarTeclado(){
        // Obtiene la vista actual
        View vista = getCurrentFocus();
        if(vista != null){
            InputMethodManager teclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            teclado.hideSoftInputFromWindow(vista.getWindowToken(), 0);  // Cierra el teclado
        }
    }

    /**
     * Configura el evento para redirigir a la pantalla de registro.
     * Se ejecuta cuando el usuario hace clic en el enlace de registro.
     */
    private void irPantallaRegistro() {
        textViewEnlaceRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicioSesionActivity.this, RegistroActivity.class));  // Abre la pantalla de registro
            }
        });
    }

    /**
     * Obtiene el usuario desde la base de datos SQLite usando el correo y la contraseña.
     * Si no se encuentra el usuario, se intenta autenticar desde Firebase.
     *
     * @param correo El correo del usuario.
     * @param contrasenya La contraseña del usuario.
     */
    private void obtenerUsuarioDesdeSQLite(String correo, String contrasenya){
        if (correo.isEmpty() || contrasenya.isEmpty()) {
            // Si los campos están vacíos, muestra un mensaje de advertencia
            Snackbar.make(buttonIniciarSesion, "Por favor, ingresa tu correo y contraseña", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Verifica si el usuario existe en SQLite
        Usuario usuarioLocal = usuarioConsulta.obtenerUsuarioPorCorreoYContrasena(correo, contrasenya);

        if (usuarioLocal != null) {
            Log.d("InicioSesion", "Usuario encontrado en SQLite: " + usuarioLocal.getCorreo());
            navegarPorTipoUsuario(usuarioLocal.getTipoUsuario(), usuarioLocal.getCorreo());  // Navega según el tipo de usuario
        } else {
            // Si no se encuentra en SQLite, intenta autenticar en Firebase
            autenticarUsuarioDesdeFirebase(correo, contrasenya);
        }
    }

    /**
     * Intenta autenticar al usuario usando Firebase.
     * Si la autenticación es exitosa, se obtiene el usuario desde Firebase.
     *
     * @param correo El correo del usuario.
     * @param contrasenya La contraseña del usuario.
     */
    private void autenticarUsuarioDesdeFirebase(String correo, String contrasenya) {
        FirebaseUtil.autenticarUsuarioEnFirebase(correo, contrasenya, new OnCompleteListener<>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Si la autenticación es exitosa
                    Log.d("FirebaseAuth", "Autenticación exitosa");
                    obtenerDatosUsuarioDesdeFirebase(correo);  // Obtiene los datos del usuario desde Firebase
                } else {
                    // Si la autenticación falla
                    Log.w("FirebaseAuth", "Error en la autenticación", task.getException());
                    Snackbar.make(buttonIniciarSesion, "Autenticación fallida. Verifique su correo y contraseña.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Obtiene los datos del usuario desde Firebase usando el correo.
     * Si el usuario se encuentra, navega a la actividad correspondiente.
     *
     * @param correo El correo del usuario.
     */
    private void obtenerDatosUsuarioDesdeFirebase(String correo) {
        DatabaseReference databaseReference = FirebaseUtil.getDatabaseReference();

        // Consulta en Firebase para obtener el usuario por correo
        databaseReference.orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Usuario usuario = snapshot.getValue(Usuario.class);
                        Log.d("FirebaseQuery", "Usuario encontrado: " + usuario.toString());
                        navegarPorTipoUsuario(usuario.getTipoUsuario(), usuario.getCorreo());  // Navega según el tipo de usuario
                    }
                } else {
                    // Si no se encuentra el usuario
                    Log.d("FirebaseQuery", "No se encontraron usuarios con ese correo.");
                    Snackbar.make(buttonIniciarSesion, "No se ha podido iniciar sesion, inténtalo de nuevo", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Maneja el error de la consulta
            }
        });
    }

    /**
     * Navega a la actividad correspondiente según el tipo de usuario.
     *
     * @param tipoUsuario El tipo de usuario que se debe redirigir.
     * @param correo El correo del usuario.
     */
    private void navegarPorTipoUsuario(String tipoUsuario, String correo) {
        Log.d("InicioSesion", "Tipo de usuario recibido: '" + tipoUsuario + "'");

        tipoUsuario = tipoUsuario.trim().toLowerCase();  // Normaliza el tipo de usuario para la comparación

        Intent intentPantallaUsuario;
        switch (tipoUsuario) {
            case "administrador":
                intentPantallaUsuario = new Intent(InicioSesionActivity.this, AdministradorActivity.class);
                break;
            case "administrativo":
                intentPantallaUsuario = new Intent(InicioSesionActivity.this, AdministrativoActivity.class);
                break;
            case "mecanico jefe":
                intentPantallaUsuario = new Intent(InicioSesionActivity.this, MecanicoJefeActivity.class);
                break;
            case "mecanico":
                intentPantallaUsuario = new Intent(InicioSesionActivity.this, MecanicoActivity.class);
                break;
            case "cliente":
                intentPantallaUsuario = new Intent(InicioSesionActivity.this, ClienteActivity.class);
                break;
            default:
                Log.e("InicioSesion", "Tipo de usuario no reconocido: '" + tipoUsuario + "'");
                Snackbar.make(buttonIniciarSesion, "Tipo de usuario no reconocido.", Snackbar.LENGTH_LONG).show();
                return;
        }

        // Redirige a la pantalla correspondiente
        Log.d("InicioSesion", "Redirigiendo a la actividad correspondiente: " + tipoUsuario);
        intentPantallaUsuario.putExtra("correo", correo);
        intentPantallaUsuario.putExtra("tipo_usuario", tipoUsuario);
        startActivity(intentPantallaUsuario);
        finish();
    }
}
