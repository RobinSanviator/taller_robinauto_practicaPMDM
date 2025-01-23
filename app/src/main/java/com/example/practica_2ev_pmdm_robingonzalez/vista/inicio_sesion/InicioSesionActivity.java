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

public class InicioSesionActivity extends AppCompatActivity {

    private TextInputEditText editTextCorreoIS;
    private TextInputEditText editTextContrasenya;
    private MaterialButton buttonIniciarSesion;
    private TextView textViewEnlaceRegistro;
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
    private UsuarioConsulta usuarioConsulta;



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


        inicializarComponenetes();
        inicializarBaseDeDatos();
        iniciarSesion();
        irPantallaRegistro();

    }

    private void inicializarComponenetes(){
        //Referencias a los elementos de la pantalla de inicio de sesión
        editTextCorreoIS = findViewById(R.id.editTextCorreoIS);
        editTextContrasenya = findViewById(R.id.editTextContrasenyaIS);
        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesion);
        textViewEnlaceRegistro = findViewById(R.id.textViewEnlaceRegistro);
    }

    private void inicializarBaseDeDatos(){
        // Usar el Singleton para obtener la instancia de la base de datos
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(InicioSesionActivity.this);
        // Obtener la instancia de UsuarioConsultas
        usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
    }

    private void iniciarSesion() {
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cerrarTeclado();

                String correo = editTextCorreoIS.getText().toString();
                String contrasenya = editTextContrasenya.getText().toString();
                obtenerUsuarioDesdeSQLite(correo, contrasenya);

            }
        });
    }

    private void cerrarTeclado(){
        //Obtener la vista actual
        View vista = getCurrentFocus();
        if(vista != null){
            InputMethodManager teclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            teclado.hideSoftInputFromWindow(vista.getWindowToken(), 0);
        }
    }

    private void irPantallaRegistro() {
        textViewEnlaceRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(InicioSesionActivity.this, RegistroActivity.class));
            }
        });
    }

    private void obtenerUsuarioDesdeSQLite(String correo, String contrasenya){
        if (correo.isEmpty() || contrasenya.isEmpty()) {
            Snackbar.make(buttonIniciarSesion, "Por favor, ingresa tu correo y contraseña", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Verificar si el usuario existe en SQLite
        Usuario usuarioLocal = usuarioConsulta.obtenerUsuarioPorCorreoYContrasena(correo, contrasenya);

        if (usuarioLocal != null) {
            Log.d("InicioSesion", "Usuario encontrado en SQLite: " + usuarioLocal.getCorreo());
            navegarPorTipoUsuario(usuarioLocal.getTipoUsuario(), usuarioLocal.getCorreo());
        } else {
            // Si no está en SQLite, consultar Firebase
            autenticarUsuarioDesdeFirebase(correo, contrasenya);
        }
    }

    private void autenticarUsuarioDesdeFirebase(String correo, String contrasenya) {
        FirebaseUtil.autenticarUsuarioEnFirebase(correo, contrasenya, new OnCompleteListener<>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Si la autenticación es exitosa, obtenemos los datos del usuario
                    Log.d("FirebaseAuth", "Autenticación exitosa");

                    // Ahora, obtenemos los datos del usuario desde Firebase Database
                    obtenerDatosUsuarioDesdeFirebase(correo);
                } else {
                    // Si la autenticación falla, mostramos un mensaje de error
                    Log.w("FirebaseAuth", "Error en la autenticación", task.getException());
                    Snackbar.make(buttonIniciarSesion, "Autenticación fallida. Verifique su correo y contraseña.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void obtenerDatosUsuarioDesdeFirebase(String correo) {
        //Obtener la referencia de la base de datos
      DatabaseReference databaseReference =  FirebaseUtil.getDatabaseReference();

      databaseReference.orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()) {
                  for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                      Usuario usuario = snapshot.getValue(Usuario.class);
                      Log.d("FirebaseQuery", "Usuario encontrado: " + usuario.toString());
                      navegarPorTipoUsuario(usuario.getTipoUsuario(), usuario.getCorreo());
                  }
              } else {
                  Log.d("FirebaseQuery", "No se encontraron usuarios con ese correo.");
                  Snackbar.make(buttonIniciarSesion, "No se ha podido iniciar sesion, inténtalo de nuevo", Snackbar.LENGTH_LONG).show();
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

    }

    private void navegarPorTipoUsuario(String tipoUsuario, String correo) {
        Log.d("InicioSesion", "Tipo de usuario recibido: '" + tipoUsuario + "'");


        // Normalizar el texto para comparación
        tipoUsuario = tipoUsuario.trim().toLowerCase();


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

        Log.d("InicioSesion", "Redirigiendo a la actividad correspondiente: " + tipoUsuario);
        intentPantallaUsuario.putExtra("correo", correo);
        intentPantallaUsuario.putExtra("tipo_usuario", tipoUsuario);
        startActivity(intentPantallaUsuario);
        finish();
    }
}