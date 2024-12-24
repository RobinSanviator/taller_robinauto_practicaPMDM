package com.example.practica_2ev_pmdm_robingonzalez.navegacion;

import android.telephony.mbms.StreamingServiceInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorAjustesFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorPerfilFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrativo.AdministrativoAjustesFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrativo.AdministrativoMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.cliente.ClienteAjustesFragment;
import com.example.practica_2ev_pmdm_robingonzalez.cliente.ClienteMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.mecanico.MecanicoAjustesFragment;
import com.example.practica_2ev_pmdm_robingonzalez.mecanico.MecanicoMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.mecanico_jefe.MecanicoJefeAjustesFragment;
import com.example.practica_2ev_pmdm_robingonzalez.mecanico_jefe.MecanicoJefeMenuPrincipalFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ControladorDeNavegacionLateral {

    private AppCompatActivity pantalla;
    private DrawerLayout contenedorNavegacionLateral;
    private NavigationView navigationViewNavegacionLateral;
    private TextView textViewTipoUsuarioCabecera, textViewNombreCabecera, textViewCorreoCabecera;
    private FrameLayout frameLayoutContenedorFragmento;


    public ControladorDeNavegacionLateral(AppCompatActivity pantalla, DrawerLayout contenedorNavegacionLateral,
                                          NavigationView navigationViewNavegacionLateral,
                                          TextView textViewTipoUsuarioCabecera, TextView textViewNombreCabecera, TextView textViewCorreoCabecera,
                                          FrameLayout frameLayoutContenedorFragmento) {
        this.pantalla = pantalla;
        this.contenedorNavegacionLateral = contenedorNavegacionLateral;
        this.navigationViewNavegacionLateral = navigationViewNavegacionLateral;
        this.textViewTipoUsuarioCabecera = textViewTipoUsuarioCabecera;
        this.textViewNombreCabecera = textViewNombreCabecera;
        this.textViewCorreoCabecera = textViewCorreoCabecera;
        this.frameLayoutContenedorFragmento = frameLayoutContenedorFragmento;

    }






    /*public void crearNavegacionLateral(){
        ImageButton imageButtonAbrirMenuLateral = pantalla.findViewById(R.id.imageButtonAbrirNavegacionLateral);
        imageButtonAbrirMenuLateral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contenedorNavegacionLateral.openDrawer(GravityCompat.START);


            }
        });
    }*/

    public void seleccionarItemNavegacionLateral() {
        String tipoUsuario = pantalla.getIntent().getStringExtra("tipo_usuario");
        navigationViewNavegacionLateral.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int idSeleccionado = item.getItemId();


                if (idSeleccionado == R.id.opcionTerminosYcondiciones) {
                    Snackbar.make(frameLayoutContenedorFragmento, "Se ha seleccionado contactar terminos y condiciones", Snackbar.LENGTH_SHORT);
                } else if (idSeleccionado == R.id.opcionMenuPrincipal) {
                    mostrarFragmentoMenuPrincipal(tipoUsuario);
                } else if (idSeleccionado == R.id.opcionPefil) {
                    mostrarFragmentoPerfil(tipoUsuario);
                } else if (idSeleccionado == R.id.opcionAjustes){
                    mostrarFragmentoAjustes(tipoUsuario);
                }
                contenedorNavegacionLateral.closeDrawer(GravityCompat.START); // Cerrar el menú lateral
                return true; // Indicar que el item fue seleccionado
            }
        });
    }

    public void mostrarFragmentoMenuPrincipal(String tipoUsuario){
        if(tipoUsuario !=null){

            switch (tipoUsuario){
                case "Administrador":
                    cargarFragmentoNavegacionLateral(new AdministradorMenuPrincipalFragment(), frameLayoutContenedorFragmento);
                    break;
                case "Administrativo":
                    cargarFragmentoNavegacionLateral(new AdministrativoMenuPrincipalFragment(), frameLayoutContenedorFragmento);
                    break;
                case "Mecanico jefe":
                    cargarFragmentoNavegacionLateral(new MecanicoJefeMenuPrincipalFragment(), frameLayoutContenedorFragmento);
                    break;
                case "Mecanico":
                    cargarFragmentoNavegacionLateral(new MecanicoMenuPrincipalFragment(), frameLayoutContenedorFragmento);
                    break;
                case "Cliente":
                    cargarFragmentoNavegacionLateral(new ClienteMenuPrincipalFragment(), frameLayoutContenedorFragmento);
                    break;
                default:
                   // Snackbar.make(navigationViewNavegacionLateral, "Error al cargar el fragmento", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }

    public void mostrarFragmentoPerfil(String tipoUsuario){
            if(tipoUsuario !=null){

                switch (tipoUsuario){
                    case "Administrador":
                        cargarFragmentoNavegacionLateral(new AdministradorPerfilFragment(), frameLayoutContenedorFragmento);
                        break;
                    case "Administrativo":
                        cargarFragmentoNavegacionLateral(new AdministradorMenuPrincipalFragment(), frameLayoutContenedorFragmento);
                        break;
                    case "Mecanico jefe":
                        cargarFragmentoNavegacionLateral(new MecanicoJefeMenuPrincipalFragment(), frameLayoutContenedorFragmento);
                        break;
                    case "Mecanico":
                        cargarFragmentoNavegacionLateral(new MecanicoMenuPrincipalFragment(), frameLayoutContenedorFragmento);
                        break;
                    case "Cliente":
                        cargarFragmentoNavegacionLateral(new ClienteMenuPrincipalFragment(), frameLayoutContenedorFragmento);
                        break;
                    default:
                        Snackbar.make(navigationViewNavegacionLateral, "Error al cargar el fragmento", Snackbar.LENGTH_LONG).show();
                        break;
            }
        }
    }

    public void mostrarFragmentoAjustes(String tipoUsuario){
        if(tipoUsuario !=null){

            switch (tipoUsuario){
                case "Administrador":
                    cargarFragmentoNavegacionLateral(new AdministradorAjustesFragment(), frameLayoutContenedorFragmento);
                    break;
                case "Administrativo":
                    cargarFragmentoNavegacionLateral(new AdministrativoAjustesFragment(), frameLayoutContenedorFragmento);
                    break;
                case "Mecanico jefe":
                    cargarFragmentoNavegacionLateral(new MecanicoJefeAjustesFragment(), frameLayoutContenedorFragmento);
                    break;
                case "Mecanico":
                    cargarFragmentoNavegacionLateral(new MecanicoAjustesFragment(), frameLayoutContenedorFragmento);
                    break;
                case "Cliente":
                    cargarFragmentoNavegacionLateral(new ClienteAjustesFragment(), frameLayoutContenedorFragmento);
                    break;
                default:
                    Snackbar.make(navigationViewNavegacionLateral, "Error al cargar el fragmento", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }



    public void cargarFragmentoNavegacionLateral(Fragment fragmento, FrameLayout contenedorFragmento) {
        // Verificar si el fragmento ya está añadido
        Fragment fragment = pantalla.getSupportFragmentManager().findFragmentByTag(fragmento.getClass().getName());
        if (fragment == null) {
            FragmentTransaction fragmentTransaction = pantalla.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(contenedorFragmento.getId(), fragmento, fragmento.getClass().getName());
            fragmentTransaction.commit();
        }
    }


}
