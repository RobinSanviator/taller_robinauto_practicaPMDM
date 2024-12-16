package com.example.practica_2ev_pmdm_robingonzalez.mecanico_jefe;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.menu_y_fragmentos_comunes.AjustesFragment;
import com.example.practica_2ev_pmdm_robingonzalez.menu_y_fragmentos_comunes.BienvenidaFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MecanicoJefeActivity extends AppCompatActivity {

    BottomNavigationView navegacionInferior;
    BienvenidaFragment bienvenidaFragment;
    MecanicoJefeDiagnosticosFragment diagnosticosFragment;
    MecanicoJefeTareasFragment tareasFragment;
    MecanicoJefeConsultasFragment consultasFragment;
    AjustesFragment ajustesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mecanico_jefe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        navegacionInferior = findViewById(R.id.bottomNavigationNavegacionMecanicoJefe);
        bienvenidaFragment = new BienvenidaFragment();
        diagnosticosFragment = new MecanicoJefeDiagnosticosFragment();
        tareasFragment = new MecanicoJefeTareasFragment();
        consultasFragment = new MecanicoJefeConsultasFragment();
        ajustesFragment = new AjustesFragment();


        navgacionMecanicoJefeConFragmentos();
        cargarFragmento(bienvenidaFragment);


    }

    // Configuración del listener del BottomNavigationView
    public void navgacionMecanicoJefeConFragmentos() {
        navegacionInferior.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bienvenidaMecanicoJefeFragment) {
                    cargarFragmento(bienvenidaFragment);
                    return true;
                } else if (item.getItemId() == R.id.diagnosticosMecanicoJefeFragment) {
                    cargarFragmento(diagnosticosFragment);
                    return true;
                } else if (item.getItemId() == R.id.tareasMecanicoJefeFragment) {
                    cargarFragmento(tareasFragment);
                    return true;

                } else if (item.getItemId() == R.id.consReparacionesMecanicoJefeFragment) {
                    cargarFragmento(consultasFragment);
                    return true;
                } else if (item.getItemId() == R.id.ajustesMecanicoJefeFragment) {
                    cargarFragmento(ajustesFragment);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }



    // Método para cargar los fragmentos
    private void cargarFragmento(Fragment fragmento) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutContenedorFragmentoMecanicoJefe, fragmento);
        fragmentTransaction.commit();
    }

    }



