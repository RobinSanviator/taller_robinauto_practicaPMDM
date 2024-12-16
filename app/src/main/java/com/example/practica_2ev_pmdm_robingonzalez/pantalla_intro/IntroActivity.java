package com.example.practica_2ev_pmdm_robingonzalez.pantalla_intro;

import android.content.Intent;
import android.os.Bundle;
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

public class IntroActivity extends AppCompatActivity {

    private Button buttonIrInicioSesion;
    private Button buttonIrRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        accederApp();

    }

    public void accederApp(){
        buttonIrInicioSesion = findViewById(R.id.buttonIniciarSesionIntro);


       if(buttonIrInicioSesion.isEnabled()){
           buttonIrInicioSesion.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   startActivity(new Intent(IntroActivity.this, InicioSesionActivity.class));
                   finish();
               }
           });
       }

        buttonIrRegistrarse = findViewById(R.id.buttonRegistrarseIntro);
       if(buttonIrRegistrarse.isEnabled()){
           buttonIrRegistrarse.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   startActivity(new Intent(IntroActivity.this, RegistroActivity.class));
                   finish();
               }
           });
       }

    }


}