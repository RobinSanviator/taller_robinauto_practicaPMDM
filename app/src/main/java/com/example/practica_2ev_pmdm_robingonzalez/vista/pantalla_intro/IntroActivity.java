package com.example.practica_2ev_pmdm_robingonzalez.vista.pantalla_intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.vista.inicio_sesion.InicioSesionActivity;
import com.example.practica_2ev_pmdm_robingonzalez.vista.registro.RegistroActivity;
import com.google.android.material.button.MaterialButton;

/**
 * Actividad de introducción de la aplicación que permite al usuario iniciar sesión o registrarse.
 * @author Robin Gonzalez
 */
public class IntroActivity extends AppCompatActivity {

    private MaterialButton materialButtonBoton;

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa la pantalla de introducción y configura las preferencias de la aplicación.
     * @param savedInstanceState Estado previo de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita la visualización de la interfaz a pantalla completa
        EdgeToEdge.enable(this);

        // Establece el layout de la actividad
        setContentView(R.layout.intro_activity);

        // Ajusta los márgenes para las barras del sistema (status bar y navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configura los botones de iniciar sesión y registrarse
        configurarBoton(R.id.buttonIniciarSesionIntro, InicioSesionActivity.class);
        configurarBoton(R.id.buttonRegistrarseIntro, RegistroActivity.class);

        // Aplica las preferencias globales, como el modo oscuro
        aplicarPreferenciasGlobales();
    }

    /**
     * Método para configurar un botón y asociarlo con una actividad de destino.
     * @param idBoton ID del botón a configurar.
     * @param activityDestino Clase de la actividad de destino.
     */
    public void configurarBoton(int idBoton, Class<?> activityDestino){
        materialButtonBoton = findViewById(idBoton);

        // Configura el listener del botón para iniciar la actividad correspondiente
        materialButtonBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad correspondiente
                startActivity(new Intent(IntroActivity.this, activityDestino));
            }
        });
    }

    /**
     * Aplica las preferencias globales, como el modo oscuro, desde SharedPreferences.
     * Configura el modo de tema de la aplicación según la preferencia del usuario.
     */
    private void aplicarPreferenciasGlobales() {
        SharedPreferences sharedPreferences = getSharedPreferences("ajustes", Context.MODE_PRIVATE);

        // Obtiene el valor del modo oscuro desde las preferencias compartidas
        boolean modoOscuroActivado = sharedPreferences.getBoolean("modoOscuro", false);

        if (modoOscuroActivado) {
            // Si el modo oscuro está activado, aplica el modo oscuro
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // Si el modo oscuro no está activado, aplica el modo claro
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}




