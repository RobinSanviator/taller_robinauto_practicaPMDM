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

public class IntroActivity extends AppCompatActivity {

    private MaterialButton materialButtonBoton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.intro_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Configura los botones de iniciar sesión y registrarse
       configurarBoton(R.id.buttonIniciarSesionIntro, InicioSesionActivity.class);
       configurarBoton(R.id.buttonRegistrarseIntro, RegistroActivity.class);

       aplicarPreferenciasGlobales();
    }

    // Método para configurar un botón y su acción
    public void configurarBoton(int idBoton, Class<?> activityDestino){
        materialButtonBoton = findViewById(idBoton);
        materialButtonBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad correspondiente
                startActivity(new Intent(IntroActivity.this, activityDestino));
            }
        });
    }

    // Aplicar las preferencias globales como el modo oscuro
    private void aplicarPreferenciasGlobales() {
        SharedPreferences sharedPreferences = getSharedPreferences("ajustes", Context.MODE_PRIVATE);
        boolean modoOscuroActivado = sharedPreferences.getBoolean("modoOscuro", false);

        if (modoOscuroActivado) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


}




