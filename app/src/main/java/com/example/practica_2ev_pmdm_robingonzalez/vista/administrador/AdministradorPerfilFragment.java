package com.example.practica_2ev_pmdm_robingonzalez.vista.administrador;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsulta;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperPerfil;


/**
 * Fragmento que muestra y gestiona el perfil del administrador.
 * Permite visualizar y modificar los datos personales del administrador.
 */
public class AdministradorPerfilFragment extends Fragment {

    private TextView textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono;  // Campos del perfil
    private TextView textViewNombreCabecera, textViewCorreoCabecera;  // Campos en la cabecera del perfil
    private ImageView imageViewMenuPrincipal;  // Imagen para volver al menú principal
    private String correo;  // Correo del administrador
    private AdministradorActivity activityAdministrador;  // Actividad principal del administrador
    private HelperPerfil helperPerfil;  // Helper para gestionar los datos del perfil
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;  // Instancia de la base de datos local
    private UsuarioConsulta usuarioConsulta;  // Consultas relacionadas con el usuario
    private HelperMenuPrincipal helperMenuPrincipal;  // Helper para el menú principal
    private HelperNavegacionInferior helperNavegacionInferior;  // Helper para la navegación inferior

    /**
     * Se llama cuando el fragmento es creado. Este método se deja vacío si no es necesario guardar ningún estado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Se llama para inflar la vista del fragmento.
     * Aquí se inicializan los componentes visuales, se obtienen los helpers y se cargan los datos del perfil.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del layout para el perfil del administrador
        View vista = inflater.inflate(R.layout.administrador_perfil_fragment, container, false);

        inicializarComponentes(vista);  // Inicializa las vistas
        obtenerHelper();  // Obtiene los helpers necesarios
        volverMenuPrincipal();  // Configura el botón para volver al menú principal
        introducirDatosPerfilCabecera();  // Introduce los datos de la cabecera del perfil
        introducirDatosEnPerfil();  // Introduce los datos personales del perfil

        return vista;  // Retorna la vista inflada
    }

    /**
     * Inicializa los componentes visuales del fragmento.
     * Se obtiene la referencia a las vistas (TextViews, ImageView) correspondientes.
     */
    private void inicializarComponentes(View vista) {
        imageViewMenuPrincipal = vista.findViewById(R.id.imageViewVolverMenuPrincipalPerfilAdmin);  // Imagen para volver al menú principal
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombrePerfilAdmin);  // Texto con el nombre en la cabecera
        textViewCorreoCabecera = vista.findViewById(R.id.textViewCorreoPerfilAdmin);  // Texto con el correo en la cabecera
        textViewNombre = vista.findViewById(R.id.textViewDatoPerfilNombreAdmin);  // Texto con el nombre del perfil
        textViewApellidos = vista.findViewById(R.id.textViewDatoPerfilApellidoAdmin);  // Texto con los apellidos del perfil
        textViewCorreo = vista.findViewById(R.id.textViewDatoPerfilCorreoAdmin);  // Texto con el correo del perfil
        textViewTelefono = vista.findViewById(R.id.textViewDatoPerfilTelefonoAdmin);  // Texto con el teléfono del perfil
    }

    /**
     * Obtiene las instancias necesarias de los helpers y la base de datos.
     * Se asegura de que la actividad actual sea una instancia de `AdministradorActivity`.
     */
    private void obtenerHelper() {
        if (getActivity() instanceof AdministradorActivity) {
            activityAdministrador = (AdministradorActivity) getActivity();  // Obtiene la actividad principal del administrador
            helperPerfil = activityAdministrador.getHelperPerfil();  // Obtiene el helper del perfil
            helperMenuPrincipal = activityAdministrador.getHelperMenuPrincipal();  // Obtiene el helper del menú principal
            helperNavegacionInferior = activityAdministrador.getHelperNavegacionInferior();  // Obtiene el helper de navegación inferior
            baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(getActivity().getApplicationContext());  // Instancia de la base de datos
            usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();  // Obtiene las consultas de usuario
        }
    }

    /**
     * Configura la acción para volver al menú principal al hacer clic en la imagen correspondiente.
     */
    private void volverMenuPrincipal() {
        imageViewMenuPrincipal.setOnClickListener(v -> {
            if(helperMenuPrincipal != null){
                // Carga el fragmento del menú principal y selecciona el item correspondiente
                helperMenuPrincipal.cargarFragmento(new AdministradorMenuPrincipalFragment());
                helperNavegacionInferior.seleccionarItemMenuPrincipal();
            } else {
                Log.e("AdministradorPerfilFragment", "Error al volver al menú principal");
            }
        });
    }

    /**
     * Introduce los datos del usuario en la cabecera del perfil.
     * Si los datos no están disponibles localmente, se obtienen desde Firebase.
     */
    private void introducirDatosPerfilCabecera() {
        correo = activityAdministrador.getCorreo();  // Obtiene el correo del administrador
        String nombre = usuarioConsulta.obtenerNombreYApellidos(correo);  // Obtiene el nombre y apellidos del usuario desde la base de datos

        if (correo != null && nombre != null) {
            // Si los datos están disponibles, se muestran en la cabecera
            textViewNombreCabecera.setText(nombre);
            textViewCorreoCabecera.setText(correo);
        } else {
            // Si no, se cargan los datos desde Firebase
            helperPerfil.cargarDatosPerfilCabeceraDesdeFirebase(correo, textViewNombreCabecera, textViewCorreoCabecera);
        }
    }

    /**
     * Introduce los datos del usuario en los campos del perfil (nombre, apellidos, correo, teléfono).
     */
    private void introducirDatosEnPerfil() {
        correo = activityAdministrador.getCorreo();  // Obtiene el correo del administrador
        if (correo != null) {
            // Si el correo es válido, carga los datos personales desde Firebase
            helperPerfil.cargarDatosPerfil(correo, textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono);
        } else {
            Log.e("AdministradorPerfilFragment", "Error al introducir los datos en el perfil");
        }
    }
}
