package com.example.practica_2ev_pmdm_robingonzalez.pantalla_intro;

import android.content.Intent;
import android.os.Bundle;
import android.system.Os;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.inicio_sesion.InicioSesionActivity;
import com.example.practica_2ev_pmdm_robingonzalez.registro.RegistroActivity;
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

    }




