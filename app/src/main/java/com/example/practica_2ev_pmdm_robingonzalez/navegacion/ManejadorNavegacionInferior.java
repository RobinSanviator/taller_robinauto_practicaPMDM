package com.example.practica_2ev_pmdm_robingonzalez.navegacion;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorAjustesFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorPerfilFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrativo.AdministrativoAjustesFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrativo.AdministrativoMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrativo.AdministrativoPerfilFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;


public class ManejadorNavegacionInferior {
    private AppCompatActivity activityActividad;
    private ChipNavigationBar chipNavigationBarNavegacionInferior;
    private ManejadorFragmento manejadorFragmentoNavegacion;

    public ManejadorNavegacionInferior(AppCompatActivity activityActividad,
                                       ChipNavigationBar chipNavigationBarNavegacionInferior, ManejadorFragmento manejadorFragmentoNavegacion) {
        this.activityActividad = activityActividad;
        this.chipNavigationBarNavegacionInferior = chipNavigationBarNavegacionInferior;
        this.manejadorFragmentoNavegacion = manejadorFragmentoNavegacion;

    }

    // Método para configurar la navegación
    public void configurarNavegacionInferior(Map<Integer, Fragment> opcionesDeMenu) {
        chipNavigationBarNavegacionInferior.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int idSeleccionado) {
                if (opcionesDeMenu.containsKey(idSeleccionado)) {
                    manejadorFragmentoNavegacion.cargarFragmento(opcionesDeMenu.get(idSeleccionado));
                }
            }
        });
    }


    // Método para deseleccionar el ítem de menú de "Menu Principal" en la navegación inferior
    public void deseleccionarItemMenuPrincipal(){
        if (activityActividad != null) {
            chipNavigationBarNavegacionInferior.setItemSelected(R.id.menuPrincipal, false);
        }
    }

    // Método para seleccionar el ítem de menú de "Menu Principal" en la navegación inferior
    public void seleccionarItemMenuPrincipal(){
        if (activityActividad != null) {
            chipNavigationBarNavegacionInferior.setItemSelected(R.id.menuPrincipal, true);
        }
    }

}
