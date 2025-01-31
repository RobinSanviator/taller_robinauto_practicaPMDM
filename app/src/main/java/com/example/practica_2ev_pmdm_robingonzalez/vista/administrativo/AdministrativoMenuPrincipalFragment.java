package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;


/**
 * Fragmento que representa el menú principal para el usuario administrativo.
 * Este fragmento gestiona las interacciones con las diferentes opciones de navegación disponibles.
 */
public class AdministrativoMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;
    private CardView cardViewRegistroCoches, cardViewReparaciones, cardViewNotificaciones, cardViewInventario;
    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperNavegacionInferior helperNavegacionInferior;
    private AdministrativoActivity activityAdministrativo;
    private String correo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflar el layout del fragmento y configurar los componentes y listeners.
     *
     * @param inflater El inflador para convertir el layout XML en un objeto de vista.
     * @param container El contenedor donde el fragmento será insertado.
     * @param savedInstanceState El estado guardado del fragmento, si lo hubiera.
     * @return La vista inflada con el contenido del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño para el menú principal
        View vista = inflater.inflate(R.layout.administrativo_menu_principal_fragment, container, false);

        // Inicializar componentes
        inicializarComponentes(vista);

        // Obtener los helpers necesarios
        obtenerHelper();

        // Obtener los datos del usuario para la cabecera
        obtenerDatosUsuarioCabecera();

        // Inicializar los listeners de los CardView
        inicializarListeners();

        return vista;
    }

    /**
     * Inicializa los componentes del layout asociados con este fragmento.
     *
     * @param vista La vista inflada del fragmento.
     */
    private void inicializarComponentes(View vista) {
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraAdministrativo);
        cardViewRegistroCoches = vista.findViewById(R.id.cardViewRegistroEntradaCochesAdministrativo);
        cardViewReparaciones = vista.findViewById(R.id.cardViewReparacionesAdministrativo);
        cardViewNotificaciones = vista.findViewById(R.id.cardViewNotificacionAdministrativo);
        cardViewInventario = vista.findViewById(R.id.cardViewInventarioAdministrativo);
    }

    /**
     * Obtiene los objetos Helper necesarios para la funcionalidad del fragmento.
     */
    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
            helperNavegacionInferior = activityAdministrativo.getHelperNavegacionInferior();
            helperMenuPrincipal = activityAdministrativo.getHelperMenuPrincipal();
        } else {
            Log.e("AdministrativoMenuPrincipalFragment", "Error al obtener helper");
        }
    }

    /**
     * Obtiene los datos del usuario (correo) y actualiza la cabecera con su nombre.
     *
     * @return El correo del usuario que está logueado.
     */
    private void obtenerDatosUsuarioCabecera() {
        correo = activityAdministrativo.getCorreo();
        if (correo != null) {
            // Si el correo es válido, cargar los datos del usuario desde Firebase
            helperMenuPrincipal.obtenerDatosUsuario(correo, textViewNombreCabecera);
            helperMenuPrincipal.cargarNombreCabeceraDesdeFirebase(correo, textViewNombreCabecera);
        } else {
            // Si no hay correo, cargamos datos por defecto
            helperMenuPrincipal.cargarNombreCabeceraDesdeFirebase(null, textViewNombreCabecera);
            helperNavegacionInferior.seleccionarItemMenuPrincipal();
        }
    }

    /**
     * Inicializa los listeners para los CardView, de forma que al hacer clic en cada uno,
     * se muestre el fragmento correspondiente.
     */
    private void inicializarListeners() {
        // Configura el clic para cada CardView, asociando el fragmento correspondiente
        configurarOnclick(cardViewRegistroCoches, new AdministrativoRegistroCochesFragment());
        configurarOnclick(cardViewReparaciones, new AdministrativoReparacionesFragment());
        configurarOnclick(cardViewNotificaciones, new AdministrativoNotificacionesFragment());
        configurarOnclick(cardViewInventario, new AdministrativoInventarioFragment());
    }

    /**
     * Configura el OnClickListener para un CardView, de manera que al hacer clic se cargue el fragmento correspondiente.
     *
     * @param cardView El CardView al que se le asignará el listener.
     * @param fragmento El fragmento que se cargará cuando se haga clic en el CardView.
     */
    private void configurarOnclick(CardView cardView, Fragment fragmento) {
        cardView.setOnClickListener(v -> {
            if (helperMenuPrincipal != null && helperNavegacionInferior != null) {
                // Cargar el fragmento y deseleccionar el ítem del menú inferior
                helperMenuPrincipal.cargarFragmento(fragmento);
                helperNavegacionInferior.deseleccionarItemMenuPrincipal();
            } else {
                Log.e("AdministrativoMenuPrincipalFragment", "Error en configurarOnClick de los cardView");
            }
        });
    }
}
