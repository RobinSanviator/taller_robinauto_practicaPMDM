package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Map;


/**
 * Clase auxiliar para manejar la navegación inferior en una actividad.
 * Proporciona métodos para configurar la navegación entre fragmentos utilizando un ChipNavigationBar
 * y para gestionar la selección/deselección de ítems del menú.
 */
public class HelperNavegacionInferior {

    private AppCompatActivity activityActividad; // Actividad que utiliza este helper
    private ChipNavigationBar chipNavigationBarNavegacionInferior; // Barra de navegación inferior
    private HelperMenuPrincipal helperMenuPrincipalNavegacion; // Helper para cargar fragmentos

    /**
     * Constructor del HelperNavegacionInferior.
     *
     * @param activityActividad La actividad que utiliza este helper.
     * @param chipNavigationBarNavegacionInferior La barra de navegación inferior.
     * @param helperMenuPrincipalNavegacion El helper para cargar fragmentos.
     */
    public HelperNavegacionInferior(AppCompatActivity activityActividad,
                                    ChipNavigationBar chipNavigationBarNavegacionInferior,
                                    HelperMenuPrincipal helperMenuPrincipalNavegacion) {
        this.activityActividad = activityActividad;
        this.chipNavigationBarNavegacionInferior = chipNavigationBarNavegacionInferior;
        this.helperMenuPrincipalNavegacion = helperMenuPrincipalNavegacion;
    }

    /**
     * Configura la navegación inferior para cargar fragmentos según la opción seleccionada.
     *
     * @param opcionesDeMenu Un mapa que relaciona los IDs de los ítems del menú con los fragmentos correspondientes.
     */
    public void configurarNavegacionInferior(Map<Integer, Fragment> opcionesDeMenu) {
        chipNavigationBarNavegacionInferior.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int idSeleccionado) {
                // Verificar si el ID seleccionado está en el mapa de opciones
                if (opcionesDeMenu.containsKey(idSeleccionado)) {
                    // Cargar el fragmento correspondiente
                    helperMenuPrincipalNavegacion.cargarFragmento(opcionesDeMenu.get(idSeleccionado));
                }
            }
        });
    }

    /**
     * Deselecciona el ítem de menú "Menu Principal" en la barra de navegación inferior.
     * Útil para evitar que el ítem permanezca seleccionado cuando no es necesario.
     */
    public void deseleccionarItemMenuPrincipal() {
        if (activityActividad != null) {
            chipNavigationBarNavegacionInferior.setItemSelected(R.id.menuPrincipal, false);
        }
    }

    /**
     * Selecciona el ítem de menú "Menu Principal" en la barra de navegación inferior.
     * Útil para resaltar el ítem cuando se vuelve al menú principal.
     */
    public void seleccionarItemMenuPrincipal() {
        if (activityActividad != null) {
            chipNavigationBarNavegacionInferior.setItemSelected(R.id.menuPrincipal, true);
        }
    }
}
