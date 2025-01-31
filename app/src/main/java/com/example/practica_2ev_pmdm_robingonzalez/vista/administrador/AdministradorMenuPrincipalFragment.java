package com.example.practica_2ev_pmdm_robingonzalez.vista.administrador;

import android.annotation.SuppressLint;
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
 * Fragmento que representa el menú principal de la vista del administrador.
 * Permite al administrador gestionar empleados y usuarios a través de opciones en tarjetas (CardViews).
 */
public class AdministradorMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;  // Texto que muestra el nombre del usuario en la cabecera
    private CardView cardViewEmpleados, cardViewUsuarios;  // Tarjetas para gestionar empleados y usuarios
    private String correo;  // Correo del usuario para cargar su información
    private HelperMenuPrincipal helperMenuPrincipal;  // Helper para manejar el menú principal
    private HelperNavegacionInferior helperNavegacionInferior;  // Helper para la navegación inferior
    private AdministradorActivity activityAdministrador;  // Actividad principal del administrador

    /**
     * Se llama cuando se crea el fragmento.
     * Se puede recuperar el estado guardado en caso de ser necesario.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Aquí se pueden obtener los argumentos si es necesario
        }
    }

    /**
     * Se llama para inflar la vista del fragmento.
     * En este método, se inicializan los componentes y los datos del usuario.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del layout para el menú principal
        View vista = inflater.inflate(R.layout.administrador_menu_principal_fragment, container, false);

        inicializarComponentes(vista);  // Inicializa las vistas
        obtenerHelper();  // Obtiene los helpers necesarios para la actividad
        obtenerDatosUsuarioCabecera();  // Obtiene los datos del usuario (nombre)
        inicializarListeners();  // Configura los listeners para los clics en las tarjetas

        return vista;  // Retorna la vista inflada
    }

    /**
     * Inicializa los componentes visuales del fragmento.
     * Se obtienen las referencias a las vistas (tarjetas y texto).
     */
    private void inicializarComponentes(View vista){
        cardViewEmpleados = vista.findViewById(R.id.cardViewDarAltaBajaAdministrador);  // Tarjeta de empleados
        cardViewUsuarios = vista.findViewById(R.id.cardViewUsuariosAdministrador);  // Tarjeta de usuarios
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraAdmin);  // Texto de nombre en cabecera
    }

    /**
     * Obtiene las instancias de los helpers necesarios desde la actividad.
     * Asegura que la actividad es una instancia de `AdministradorActivity`.
     */
    private void obtenerHelper(){
        if(getActivity() instanceof AdministradorActivity) {
            activityAdministrador = (AdministradorActivity) getActivity();  // Obtiene la actividad
            helperNavegacionInferior = activityAdministrador.getHelperNavegacionInferior();  // Obtiene el helper de navegación inferior
            helperMenuPrincipal = activityAdministrador.getHelperMenuPrincipal();  // Obtiene el helper del menú principal
        } else {
            // Si no es la actividad correcta, muestra un error
            Log.e("AdministradorMenuPrincipalFragment", "Error al obtener helper");
        }
    }

    /**
     * Obtiene los datos del usuario (correo) y carga el nombre en la cabecera.
     * Si el correo es válido, se obtienen los datos desde Firebase y se muestran en la interfaz.
     */
    private void obtenerDatosUsuarioCabecera() {
        correo = activityAdministrador.getCorreo();  // Obtiene el correo del administrador desde la actividad
        if (correo != null) {
            // Si el correo es válido, carga el nombre del usuario desde Firebase
            helperMenuPrincipal.obtenerDatosUsuario(correo, textViewNombreCabecera);
            helperMenuPrincipal.cargarNombreCabeceraDesdeFirebase(correo, textViewNombreCabecera);
            helperNavegacionInferior.seleccionarItemMenuPrincipal();  // Resalta el item del menú principal
        } else {
            // Si no hay correo, carga un nombre vacío y maneja la navegación
            helperMenuPrincipal.cargarNombreCabeceraDesdeFirebase(null, textViewNombreCabecera);
            helperNavegacionInferior.seleccionarItemMenuPrincipal();
        }
    }

    /**
     * Inicializa los listeners para las tarjetas (CardViews).
     * Configura las acciones a realizar cuando el usuario hace clic en una tarjeta.
     */
    private void inicializarListeners(){
        // Configura el clic en la tarjeta de empleados
        configurarOnclick(cardViewEmpleados, new AdministradorGestionEmpleadosFragment());
        // Configura el clic en la tarjeta de usuarios
        configurarOnclick(cardViewUsuarios, new AdministradorModificarUsuariosFragment());
    }

    /**
     * Configura un listener para una tarjeta. Al hacer clic en la tarjeta, se carga el fragmento correspondiente.
     *
     * @param cardView La tarjeta a la que se le agrega el listener.
     * @param fragmento El fragmento que se debe cargar cuando se hace clic en la tarjeta.
     */
    private void configurarOnclick(CardView cardView, Fragment fragmento){
        cardView.setOnClickListener(v -> {
            // Si los helpers están disponibles, carga el fragmento y deselecciona el menú principal
            if(helperMenuPrincipal != null && helperNavegacionInferior != null){
                helperMenuPrincipal.cargarFragmento(fragmento);
                helperNavegacionInferior.deseleccionarItemMenuPrincipal();
            } else {
                // Si los helpers no están disponibles, muestra un error
                Log.e("AdministradorMenuPrincipalFragment", "Error en configurarOnClick de los cardView");
            }
        });
    }
}

