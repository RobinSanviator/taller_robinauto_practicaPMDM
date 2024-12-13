package com.example.practica_2ev_pmdm_robingonzalez;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdministradorActivity extends AppCompatActivity {

    BBDDUsuariosSQLite baseDeDatos;
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
        baseDeDatos = new BBDDUsuariosSQLite(AdministradorActivity.this, "gestion_usuario_taller", null, 3);

        // Crear navegación inferior
        crearNavegacionInferior();
        loadFragment(bienvenidaFragment); // Cargar fragmento de bienvenida inicialmente

        obtenerTipoUsuario();

    }




    // Método que obtiene el tipo de usuario desde la base de datos
    public void obtenerTipoUsuario() {
        String correoUsuario = getIntent().getStringExtra("correo");
        String contrasenyaUsuario = getIntent().getStringExtra("contrasenya");
        String tipoUsuario = baseDeDatos.obtenerTipoUsuario(correoUsuario, contrasenyaUsuario);


            if (tipoUsuario != null && tipoUsuario.equals("Administrador")) {
                Menu menu = navegacionInferior.getMenu();
                // Funciones comunes en true
                menu.findItem(R.id.ajustesAdminFragment).setVisible(true);
                menu.findItem(R.id.bienvenidaAdminFragment).setVisible(true);
                menu.findItem(R.id.gestionEmpleadosAdminFragment).setVisible(true);
                menu.findItem(R.id.modificarUsuariosAdminFragment).setVisible(true);

        }
    }

    // Configuración del listener del BottomNavigationView
    public void crearNavegacionInferior() {
        navegacionInferior.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bienvenidaAdminFragment) {
                    loadFragment(bienvenidaFragment);
                    return true;
                } else if (item.getItemId() == R.id.modificarUsuariosAdminFragment) {
                    loadFragment(modificarUsuariosFragment);
                    return true;
                } else if (item.getItemId() == R.id.gestionEmpleadosAdminFragment) {
                    loadFragment(gestionEmpleadosFragment);
                    return true;
                } else if (item.getItemId() == R.id.ajustesAdminFragment) {
                    loadFragment(ajustesFragment);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    // Método para cargar los fragmentos
    private void loadFragment(Fragment fragmento) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutContenedorFragmentoAdmin, fragmento);
        fragmentTransaction.commit();
    }
}




