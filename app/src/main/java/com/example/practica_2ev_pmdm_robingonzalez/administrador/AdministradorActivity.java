package com.example.practica_2ev_pmdm_robingonzalez.administrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.inicio_sesion.InicioSesionActivity;
import com.example.practica_2ev_pmdm_robingonzalez.menu_y_fragmentos_comunes.BienvenidaFragment;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;


public class AdministradorActivity extends AppCompatActivity {

    // objetos privados de la navegación inferior y sus fragmentos
    private BottomNavigationView navegacionInferior;
    private BienvenidaFragment bienvenidaFragment;
    private AdministradorDarAltaFragment gestionEmpleadosFragment;
    private AdministradorModificarUsuariosFragment modificarUsuariosFragment;

    // objetos privados de la navegación lateral y sus fragmentos
    private DrawerLayout navegacionLateral;
    private ImageButton imageButtonAbrirMenuLateral;
    private NavigationView navigationViewNavegacionLateral;
    private Fragment fragmentoPerfil;
    private Fragment fragmentoAjustes;
    ImageView imageViewPerfil;
    private TextView textViewNombre;
    private TextView textViewCorreo;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrador);

        // Configuración de las ventanas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayoutNavegacionLateralAdministrador), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas y fragmentos de la navegacion inferior(BottomNavigation)
        navegacionInferior = findViewById(R.id.bottomNavigationNavegacionAdministrador);
        bienvenidaFragment = new BienvenidaFragment();
        gestionEmpleadosFragment = new AdministradorDarAltaFragment();
        modificarUsuariosFragment = new AdministradorModificarUsuariosFragment();

        // Inicializar vistas y fragmentos de la navegacion lateral(DrawerNavigation)
        navegacionLateral = findViewById(R.id.drawerLayoutNavegacionLateralAdministrador);
        navigationViewNavegacionLateral = findViewById(R.id.navigationViewNavegacionLateral);
        imageButtonAbrirMenuLateral = findViewById(R.id.imageButtonAbrirNavegacionLateral);
        fragmentoPerfil = new AdministradorPerfilFragment();
        fragmentoAjustes = new AdministradorAjustesFragment();
        imageViewPerfil = findViewById(R.id.imageViewPerfilCabecera);

        View vistaCabecera = navigationViewNavegacionLateral.getHeaderView(0);
        textViewNombre = vistaCabecera.findViewById(R.id.textViewNombreUsuarioCabecera);
        textViewCorreo = vistaCabecera.findViewById(R.id.textViewCorreoUsuarioCabecera);



        crearNavegacionLateral();
        seleccionarNavegacionLateral();
        crearBottomNavigation();
        cargarFragmentoNavegacion(bienvenidaFragment); // Cargar fragmento de bienvenida de la navegación inferior inicialmente


    }

    public void crearNavegacionLateral(){
        imageButtonAbrirMenuLateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               navegacionLateral.open();
                // Supón que el correo lo obtienes del Intent de la actividad anterior
                String correo = getIntent().getStringExtra("correo");

                // Llamar al método para obtener los datos del usuario
                obtenerDatosUsuarioCabecera(correo);
            }
        });
    }

    public void seleccionarNavegacionLateral() {
        navigationViewNavegacionLateral.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int idSeleccionado = item.getItemId();

                // Cargar el fragmento correspondiente según la opción seleccionada
                if (idSeleccionado == R.id.opcionPefil) {
                    cargarFragmentoNavegacion(new AdministradorPerfilFragment());
                } else if (idSeleccionado == R.id.opcionAjustes) {
                    cargarFragmentoNavegacion(new AdministradorAjustesFragment());
                } else if (idSeleccionado == R.id.opcionContactar) {
                    Snackbar.make(navigationViewNavegacionLateral, "Has seleccionado contactar con nosotros", Snackbar.LENGTH_SHORT).show();
                } else if (idSeleccionado == R.id.opcionCerrarSesionr) {
                    startActivity(new Intent(AdministradorActivity.this, InicioSesionActivity.class));
                    finish();
                } else if (idSeleccionado == R.id.opcionSalir) {
                    finish();
                    Snackbar.make(navigationViewNavegacionLateral, "Has seleccionado Salir", Snackbar.LENGTH_SHORT).show();
                }

                // Cerrar el menú lateral después de seleccionar una opción
                navegacionLateral.closeDrawer(GravityCompat.START);

                return true; // Devolver true para marcar la selección del ítem
            }
        });
    }


    public void obtenerDatosUsuarioCabecera(String correo) {
        BBDDUsuariosSQLite baseDeDatosGestionUsuarios = new BBDDUsuariosSQLite(
                AdministradorActivity.this, "gestion_usuario_taller", null, 3);

        // Obtén el nombre completo y el correo desde la base de datos
        String nombreCompleto = baseDeDatosGestionUsuarios.obtenerNombreYApellidos(correo);
        String correoUsuario = baseDeDatosGestionUsuarios.obtenerCorreo(correo);

        // Verifica si obtuviste el nombre completo y el correo
        if (nombreCompleto != null && correoUsuario != null) {
            // Establece el nombre y correo en los TextViews de la cabecera
            textViewNombre.setText(nombreCompleto);
            textViewCorreo.setText(correoUsuario);
        } else {
            // Si no se encontró el usuario, puedes mostrar un mensaje predeterminado o dejar los campos vacíos
            textViewNombre.setText("Nombre no disponible");
            textViewCorreo.setText("Correo no disponible");
        }
    }


    // Configuración del listener del BottomNavigationView
    public void crearBottomNavigation() {
        navegacionInferior.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.bienvenidaAdminFragment) {
                    cargarFragmentoNavegacion(bienvenidaFragment);
                    return true;
                } else if (item.getItemId() == R.id.modificarUsuariosAdminFragment) {
                    cargarFragmentoNavegacion(modificarUsuariosFragment);
                    return true;
                } else if (item.getItemId() == R.id.gestionEmpleadosAdminFragment) {
                    cargarFragmentoNavegacion(gestionEmpleadosFragment);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    // Método para cargar los fragmentos
    public void cargarFragmentoNavegacion(Fragment fragmento) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutContenedorFragmentoAdmin, fragmento);
        fragmentTransaction.commit();
    }


}




