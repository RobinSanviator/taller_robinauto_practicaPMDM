package com.example.practica_2ev_pmdm_robingonzalez.vista.administrador;


import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsulta;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperAjustes;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Fragmento de la pantalla de ajustes del administrador.
 * Permite modificar la configuración de la aplicación, como el modo oscuro, cerrar sesión, salir de la aplicación, etc.
 */
public class AdministradorAjustesFragment extends Fragment {

    private SwitchCompat switchCompatBotonModoOscuro;  // Switch para activar/desactivar el modo oscuro
    private ProgressBar progressBarModoOscuro;  // Barra de progreso que indica el estado del modo oscuro
    private ImageView imageViewVolverMenu;  // Imagen para volver al menú principal
    private TextView textViewNombre;  // Nombre del administrador en los ajustes
    private String correo;  // Correo del administrador
    private RelativeLayout relativeLayoutTyC, relativeLayoutCerrarSesion, relativeLayoutSalir;  // Layouts para los botones de Términos y condiciones, cerrar sesión, salir
    private AdministradorActivity activityAdministrador;  // Actividad principal del administrador
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;  // Instancia de la base de datos local
    private UsuarioConsulta usuarioConsulta;  // Consultas relacionadas con el usuario
    private HelperMenuPrincipal helperMenuPrincipal;  // Helper para gestionar el menú principal
    private HelperAjustes helperAjustes;  // Helper para gestionar los ajustes

    /**
     * Se llama cuando el fragmento es creado. Este método se deja vacío si no es necesario guardar ningún estado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Se llama para inflar la vista del fragmento.
     * Aquí se inicializan los componentes visuales, se obtienen los helpers y se configuran las acciones de los botones.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del layout de ajustes
        View vista = inflater.inflate(R.layout.administrador_ajustes_fragment, container, false);

        inicializarComponentes(vista);  // Inicializa las vistas
        obtenerHelper();  // Obtiene los helpers necesarios
        cargarPreferenciaModoOscuro();  // Carga la preferencia de modo oscuro desde SharedPreferences
        modoOscuro();  // Configura el modo oscuro
        volverAlMenuDesdeAjustes();  // Configura el botón para volver al menú principal
        introducirNombreUsuarioAjustes();  // Introduce el nombre del usuario en los ajustes
        mostrarTyC();  // Configura el botón para mostrar los términos y condiciones
        cerrarSesion();  // Configura el botón para cerrar sesión
        salir();  // Configura el botón para salir de la aplicación

        return vista;  // Retorna la vista inflada
    }

    /**
     * Inicializa los componentes visuales del fragmento.
     * Se obtiene la referencia a las vistas (Switch, ProgressBar, ImageView, TextView, etc.).
     */
    private void inicializarComponentes(View vista) {
        switchCompatBotonModoOscuro = vista.findViewById(R.id.switchBotonModoOscuroAjustesAdmin);  // Switch para modo oscuro
        progressBarModoOscuro = vista.findViewById(R.id.progressBarModoOscuroAjustesAdmin);  // Barra de progreso del modo oscuro
        imageViewVolverMenu = vista.findViewById(R.id.imageViewVolverMenuPrincipalAjustesAdmin);  // Imagen para volver al menú principal
        textViewNombre = vista.findViewById(R.id.textViewNombreAjustesAdmin);  // Texto con el nombre del usuario
        relativeLayoutTyC = vista.findViewById(R.id.relativeLayoutTyCAjustesAdmin);  // Layout de términos y condiciones
        relativeLayoutCerrarSesion = vista.findViewById(R.id.relativeLayoutSesionAjustesAdmin);  // Layout para cerrar sesión
        relativeLayoutSalir = vista.findViewById(R.id.relativeLayoutSalirAjustesAdmin);  // Layout para salir de la app
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(getContext());  // Instancia de la base de datos
        usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();  // Consultas de usuario
    }

    /**
     * Obtiene las instancias necesarias de los helpers.
     * Se asegura de que la actividad actual sea una instancia de `AdministradorActivity`.
     */
    private void obtenerHelper() {
        if (getActivity() instanceof AdministradorActivity) {
            activityAdministrador = (AdministradorActivity) getActivity();  // Obtiene la actividad principal del administrador
            helperMenuPrincipal = activityAdministrador.getHelperMenuPrincipal();  // Obtiene el helper del menú principal
            helperAjustes = activityAdministrador.getHelperAjustes();  // Obtiene el helper de ajustes
        }
    }

    /**
     * Configura la acción para volver al menú principal al hacer clic en la imagen correspondiente.
     */
    private void volverAlMenuDesdeAjustes() {
        imageViewVolverMenu.setOnClickListener(v -> {
            if(helperMenuPrincipal != null){
                activityAdministrador.volverMenuPrincipal();  // Vuelve al menú principal
            } else {
                Log.e("AdministradorAjustesFragment", "No se pudo volver al menú principal");
            }
        });
    }

    /**
     * Introduce el nombre del usuario en el área de ajustes.
     * Si el nombre no está disponible localmente, se obtiene desde Firebase.
     */
    private void introducirNombreUsuarioAjustes() {
        correo = activityAdministrador.getCorreo();  // Obtiene el correo del administrador
        String nombre = usuarioConsulta.obtenerNombreYApellidos(correo);  // Obtiene el nombre del usuario desde la base de datos

        if(correo != null && nombre != null){
            // Si los datos están disponibles, se muestra el nombre en los ajustes
            textViewNombre.setText(nombre);
            helperAjustes.cargarNombreCabeceraDesdeFirebase(correo, textViewNombre);
        } else {
            // Si no, se cargan los datos desde Firebase
            helperAjustes.cargarNombreCabeceraDesdeFirebase(correo, textViewNombre);
        }
    }

    /**
     * Configura el modo oscuro.
     * Si está habilitado, activa el modo oscuro y actualiza la interfaz de usuario.
     */
    private void modoOscuro() {
        if(helperAjustes != null){
            helperAjustes.modoOscuro(correo, switchCompatBotonModoOscuro, progressBarModoOscuro, getContext());
        } else {
            Log.e("AdministradorAjustesFragment", "No se puede cargar el modo oscuro");
        }
    }

    /**
     * Carga la preferencia de modo oscuro desde SharedPreferences.
     * Actualiza el switch de modo oscuro según la preferencia almacenada.
     */
    private void cargarPreferenciaModoOscuro() {
        if(helperAjustes != null){
            helperAjustes.cargarPreferenciaModoOscuro(switchCompatBotonModoOscuro, getContext());
        } else {
            Log.e("AdministradorAjustesFragment", "No se puede cargar la preferencia de modo oscuro");
        }
    }

    /**
     * Configura la acción para mostrar los términos y condiciones al hacer clic en el layout correspondiente.
     */
    private void mostrarTyC() {
        relativeLayoutTyC.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.mostrarTerminosYCondiciones(getContext());  // Muestra los términos y condiciones
            } else {
                Log.e("AdministradorAjustesFragment", "helperAjustes null: no se pudo mostrar términos y condiciones");
            }
        });
    }

    /**
     * Configura la acción para cerrar sesión al hacer clic en el layout correspondiente.
     */
    private void cerrarSesion() {
        relativeLayoutCerrarSesion.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.cerrarSesion(getContext());  // Cierra la sesión del usuario
            } else {
                Log.e("AdministradorAjustesFragment", "helperAjustes null: no se pudo cerrar sesión");
            }
        });
    }

    /**
     * Configura la acción para salir de la aplicación al hacer clic en el layout correspondiente.
     */
    private void salir() {
        relativeLayoutSalir.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.salir(getContext());  // Sale de la aplicación
            } else {
                Log.e("Error", "helperAjustes null: no se pudo salir de la app");
            }
        });
    }
}

