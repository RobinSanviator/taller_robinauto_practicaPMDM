package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;

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


/**
 * Fragmento que representa la pantalla de ajustes del administrador.
 * Permite al administrador gestionar configuraciones como el modo oscuro, cerrar sesión, ver términos y condiciones, etc.
 */
public class AdministrativoAjustesFragment extends Fragment {

    private SwitchCompat switchCompatBotonModoOscuro;
    private ProgressBar progressBarModoOscuro;
    private ImageView imageViewVolverMenu;
    private TextView textViewNombre;
    private String correo;
    private RelativeLayout relativeLayoutTyC, relativeLayoutCerrarSesion, relativeLayoutSalir;
    private AdministrativoActivity activityAdministrativo;
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
    private UsuarioConsulta usuarioConsulta;
    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperAjustes helperAjustes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Infla el layout del fragmento y configura los componentes y funcionalidades.
     *
     * @param inflater El inflador para convertir el layout XML en un objeto de vista.
     * @param container El contenedor donde el fragmento será insertado.
     * @param savedInstanceState El estado guardado del fragmento, si lo hubiera.
     * @return La vista inflada con el contenido del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout de ajustes
        View vista = inflater.inflate(R.layout.administrativo_ajustes_fragment, container, false);

        // Inicializar componentes
        inicializarComponentes(vista);

        // Obtener los helpers para funcionalidades
        obtenerHelper();

        // Cargar preferencia del modo oscuro desde SharedPreferences
        cargarPreferenciaModoOscuro();

        // Activar el modo oscuro basado en la preferencia
        modoOscuro();

        // Configurar el botón para volver al menú principal
        volverAlMenuDesdeAjustes();

        // Introducir el nombre del usuario en el ajuste de la interfaz
        introducirNombreUsuarioAjustes();

        // Configurar los términos y condiciones
        mostrarTyC();

        // Configurar la funcionalidad para cerrar sesión
        cerrarSesion();

        // Configurar la funcionalidad para salir de la aplicación
        salir();

        return vista;
    }

    /**
     * Inicializa los componentes del layout asociados con este fragmento.
     *
     * @param vista La vista inflada del fragmento.
     */
    private void inicializarComponentes(View vista) {
        switchCompatBotonModoOscuro = vista.findViewById(R.id.switchBotonModoOscuroAjustesAdministrativo);
        progressBarModoOscuro = vista.findViewById(R.id.progressBarModoOscuroAjustesAdministrativo);
        imageViewVolverMenu = vista.findViewById(R.id.imageViewVolverMenuPrincipalAjustesAdministrativo);
        textViewNombre = vista.findViewById(R.id.textViewNombreAjustesAdministrativo);
        relativeLayoutTyC = vista.findViewById(R.id.relativeLayoutTyCAjustesAdministrativo);
        relativeLayoutCerrarSesion = vista.findViewById(R.id.relativeLayoutSesionAjustesAdministrativo);
        relativeLayoutSalir = vista.findViewById(R.id.relativeLayoutSalirAjustesAdministrativo);
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(getContext());
        usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
    }

    /**
     * Obtiene los objetos Helper necesarios para la funcionalidad del fragmento.
     */
    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
            helperMenuPrincipal = activityAdministrativo.getHelperMenuPrincipal();
            helperAjustes = activityAdministrativo.getHelperAjustes();
        }
    }

    /**
     * Configura el comportamiento del botón para volver al menú principal.
     */
    private void volverAlMenuDesdeAjustes(){
        imageViewVolverMenu.setOnClickListener(v -> {
            if(helperMenuPrincipal != null){
                activityAdministrativo.volverMenuPrincipal();
            } else {
                Log.e("AdministrativoAjustesFragment", "No se pudo volver al menú principal");
            }
        });
    }

    /**
     * Introduce el nombre del usuario en el ajuste de la interfaz, obteniéndolo desde la base de datos.
     *
     * Si el nombre está disponible en la base de datos, lo muestra en la interfaz,
     * y si no, lo obtiene de Firebase a través del helper.
     */
    private void introducirNombreUsuarioAjustes(){
        correo = activityAdministrativo.getCorreo();
        String nombre = usuarioConsulta.obtenerNombreYApellidos(correo);

        if(correo != null && nombre != null){
            textViewNombre.setText(nombre);
            helperAjustes.cargarNombreCabeceraDesdeFirebase(correo, textViewNombre);
        } else {
            helperAjustes.cargarNombreCabeceraDesdeFirebase(correo, textViewNombre);
        }
    }

    /**
     * Configura el modo oscuro según la preferencia del usuario.
     * Llama al helper para que active el modo oscuro si es necesario.
     */
    private void modoOscuro() {
        String correo = activityAdministrativo.getCorreo();
        if(helperAjustes != null ){
            helperAjustes.modoOscuro(correo, switchCompatBotonModoOscuro, progressBarModoOscuro,  getContext());
        } else {
            Log.e("AdministrativoAjustesFragment", "Error al cargar el modo oscuro");
        }
    }

    /**
     * Carga la preferencia del modo oscuro desde las SharedPreferences.
     *
     * Llama al helper para cargar y aplicar la preferencia de modo oscuro.
     */
    private void cargarPreferenciaModoOscuro() {
        if(helperAjustes != null){
            helperAjustes.cargarPreferenciaModoOscuro(switchCompatBotonModoOscuro, getContext());
        } else {
            Log.e("AdministrativoAjustesFragment", "No se puede cargar la preferencia de modo oscuro");
        }
    }

    /**
     * Configura la funcionalidad para mostrar los términos y condiciones cuando el usuario lo solicite.
     */
    private void mostrarTyC(){
        relativeLayoutTyC.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.mostrarTerminosYCondiciones(getContext());
            } else {
                Log.e("AdministrativoAjustesFragment", "helperAjustes null: no se pudo mostrar términos y condiciones");
            }
        });
    }

    /**
     * Configura el comportamiento para cerrar sesión en la aplicación.
     */
    private void cerrarSesion(){
        relativeLayoutCerrarSesion.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.cerrarSesion(getContext());
            } else {
                Log.e("AdministrativoAjustesFragment", "No se pudo cerrar sesión");
            }
        });
    }

    /**
     * Configura el comportamiento para salir de la aplicación.
     */
    private void salir(){
        relativeLayoutSalir.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.salir(getContext());
            } else {
                Log.e("AdministrativoAjustesFragment", "No se pudo salir de la app");
            }
        });
    }
}

