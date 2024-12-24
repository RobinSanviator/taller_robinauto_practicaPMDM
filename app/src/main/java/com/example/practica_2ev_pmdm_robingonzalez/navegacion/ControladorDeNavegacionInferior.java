package com.example.practica_2ev_pmdm_robingonzalez.navegacion;


import android.view.Menu;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class ControladorDeNavegacionInferior {
    private AppCompatActivity pantalla;
    private ChipNavigationBar chipNavigationBarNavegacionInferior;
    private FrameLayout frameLayoutContenedorFragmento;

    public ControladorDeNavegacionInferior(AppCompatActivity pantalla,
                                           ChipNavigationBar chipNavigationBarNavegacionInferior,
                                           FrameLayout frameLayoutContenedorFragmento) {
        this.pantalla = pantalla;
        this.chipNavigationBarNavegacionInferior = chipNavigationBarNavegacionInferior;
        this.frameLayoutContenedorFragmento = frameLayoutContenedorFragmento;
    }




  /*  public void seleccionarItemNavegacionInferior(String tipoUsuario) {
        bottomNavigationViewNavegacionInferior.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (tipoUsuario != null) {
                    int idSeleccionado = item.getItemId();

                    // Llamar al método para cargar el fragmento correspondiente según el tipo de usuario y el ítem seleccionado
                    cargarFragmentoPorTipoUsuario(tipoUsuario, idSeleccionado);
                }
                return true; // Indicamos que el ítem ha sido seleccionado
            }
        });
    }

    public void cargarFragmentoPorTipoUsuario(String tipoUsuario, int idSeleccionado) {

        } else if ("Administrativo".equals(tipoUsuario)) {
            if (idSeleccionado == R.id.menuPrincipalAdministrativoFragment) {
                cargarFragmentoNavegacionInferior(new AdministrativoMenuPrincipalFragment(), frameLayoutContenedorFragmento);
            } else if (idSeleccionado == R.id.reparacionesAdministrativoFragment) {
                cargarFragmentoNavegacionInferior(new AdministrativoReparacionesFragment(), frameLayoutContenedorFragmento);
            } else if (idSeleccionado == R.id.stockYpedidosAdministrativoFragment) {
                cargarFragmentoNavegacionInferior(new AdministrativoStockPedidosFragment(), frameLayoutContenedorFragmento);
            } else if (idSeleccionado == R.id.notificacionesAdministrativoFragment) {
                cargarFragmentoNavegacionInferior(new AdministrativoNotificacionesFragment(), frameLayoutContenedorFragmento);
            }
        } else if ("Mecanico jefe".equals(tipoUsuario)) {
            if (idSeleccionado == R.id.menuPrincipalMecanicoJefeFragment) {
                cargarFragmentoNavegacionInferior(new MecanicoJefeMenuPrincipalFragment(), frameLayoutContenedorFragmento);
            } else if (idSeleccionado == R.id.tareasYdiagnosticosMecanicoJefeFragment) {
                cargarFragmentoNavegacionInferior(new MecanicoJefeTareasYDiagnosticosFragment(), frameLayoutContenedorFragmento);
            } else if (idSeleccionado == R.id.consultasRepMecanicoJefeFragment) {
                cargarFragmentoNavegacionInferior(new MecanicoJefeConsultasFragment(), frameLayoutContenedorFragmento);
            }
        } else if ("Mecanico".equals(tipoUsuario)) {
            if (idSeleccionado == R.id.menuPrincipalMecanicoFragment) {
                cargarFragmentoNavegacionInferior(new MecanicoMenuPrincipalFragment(), frameLayoutContenedorFragmento);
            } else if (idSeleccionado == R.id.tareasMecanicoFragment) {
                cargarFragmentoNavegacionInferior(new MecanicoTareasFragment(), frameLayoutContenedorFragmento);
            } else if (idSeleccionado == R.id.solicitarPiezasMecanicoFragment) {
                cargarFragmentoNavegacionInferior(new MecanicoPiezasFragment(), frameLayoutContenedorFragmento);
            }
        } else if ("Cliente".equals(tipoUsuario)) {
            if (idSeleccionado == R.id.menuPrincipalClienteFragment) {
                cargarFragmentoNavegacionInferior(new ClienteMenuPrincipalFragment(), frameLayoutContenedorFragmento);
            } else if (idSeleccionado == R.id.perfilClienteFragment) {
                cargarFragmentoNavegacionInferior(new ClientePerfilFragment(), frameLayoutContenedorFragmento);
            } else if (idSeleccionado == R.id.reparacionesClienteFragment) {
                cargarFragmentoNavegacionInferior(new ClienteReparacionesFragment(), frameLayoutContenedorFragmento);
            }
        } else {
            Snackbar.make(bottomNavigationViewNavegacionInferior, "Error al cargar la pantalla", Snackbar.LENGTH_LONG).show();
        }
    }*/



}
