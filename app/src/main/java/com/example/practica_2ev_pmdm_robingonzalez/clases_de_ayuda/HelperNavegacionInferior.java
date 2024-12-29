package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Map;


public class HelperNavegacionInferior {
    private AppCompatActivity activityActividad;
    private ChipNavigationBar chipNavigationBarNavegacionInferior;
    private HelperFragmento helperFragmentoNavegacion;

    public HelperNavegacionInferior(AppCompatActivity activityActividad,
                                    ChipNavigationBar chipNavigationBarNavegacionInferior, HelperFragmento helperFragmentoNavegacion) {
        this.activityActividad = activityActividad;
        this.chipNavigationBarNavegacionInferior = chipNavigationBarNavegacionInferior;
        this.helperFragmentoNavegacion = helperFragmentoNavegacion;

    }

    // Método para configurar la navegación
    public void configurarNavegacionInferior(Map<Integer, Fragment> opcionesDeMenu) {
        chipNavigationBarNavegacionInferior.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int idSeleccionado) {
                if (opcionesDeMenu.containsKey(idSeleccionado)) {
                    helperFragmentoNavegacion.cargarFragmento(opcionesDeMenu.get(idSeleccionado));
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
