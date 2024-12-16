package com.example.practica_2ev_pmdm_robingonzalez.administrador;

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

import com.example.practica_2ev_pmdm_robingonzalez.menu_y_fragmentos_comunes.AjustesFragment;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.menu_y_fragmentos_comunes.BienvenidaFragment;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdministradorActivity extends AppCompatActivity {

    BottomNavigationView navegacionInferior;
    BienvenidaFragment bienvenidaFragment;
    AdministradorDarAltaFragment gestionEmpleadosFragment;
    AdministradorModificarUsuariosFragment modificarUsuariosFragment;
    AjustesFragment ajustesFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrador);

        // Configuración de las ventanas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas y fragments
        navegacionInferior = findViewById(R.id.bottomNavigationNavegacionAdministrador);
        bienvenidaFragment = new BienvenidaFragment();
        gestionEmpleadosFragment = new AdministradorDarAltaFragment();
        modificarUsuariosFragment = new AdministradorModificarUsuariosFragment();
        ajustesFragment = new AjustesFragment();


        navgacionAdministradorConFragmentos();
        cargarFragmento(bienvenidaFragment); // Cargar fragmento de bienvenida inicialmente


    }


    // Configuración del listener del BottomNavigationView
    public void navgacionAdministradorConFragmentos() {
        navegacionInferior.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bienvenidaAdminFragment) {
                    cargarFragmento(bienvenidaFragment);
                    return true;
                } else if (item.getItemId() == R.id.modificarUsuariosAdminFragment) {
                    cargarFragmento(modificarUsuariosFragment);
                    return true;
                } else if (item.getItemId() == R.id.gestionEmpleadosAdminFragment) {
                    cargarFragmento(gestionEmpleadosFragment);
                    return true;
                } else if (item.getItemId() == R.id.ajustesAdminFragment) {
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
        fragmentTransaction.replace(R.id.frameLayoutContenedorFragmentoAdmin, fragmento);
        fragmentTransaction.commit();
    }
}




