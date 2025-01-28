package com.example.practica_2ev_pmdm_robingonzalez.vista.cliente;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.net.Uri;
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


public class ClienteAjustesFragment extends Fragment {

    private SwitchCompat switchCompatBotonModoOscuro;
    private ProgressBar progressBarModoOscuro;
    private ImageView imageViewVolverMenu;
    private TextView textViewNombre;
    private String correo;
    private RelativeLayout relativeLayoutTyC, relativeLayoutContactar, relativeLayoutCerrarSesion, relativeLayoutSalir;
    private ClienteActivity activityCliente;
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
    private UsuarioConsulta usuarioConsulta;
    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperAjustes helperAjustes;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout ajustes de cliente
        View vista = inflater.inflate(R.layout.cliente_ajustes_fragment, container, false);
        inicializarComponentes(vista);
        obtenerHelper();
        cargarPreferenciaModoOscuro();
        modoOscuro();
        volverAlMenuDesdeAjustes();
        introducirNombreUsuarioAjustes();
        realizarLlamada();
        mostrarTyC();
        cerrarSesion();
        salir();
        return vista;
    }

    private void inicializarComponentes(View vista) {
        switchCompatBotonModoOscuro = vista.findViewById(R.id.switchBotonModoOscuroAjustesCliente);
        progressBarModoOscuro = vista.findViewById(R.id.progressBarModoOscuroAjustesCliente);
        imageViewVolverMenu = vista.findViewById(R.id.imageViewVolverMenuPrincipalAjustesCliente);
        textViewNombre = vista.findViewById(R.id.textViewNombreAjustesCliente);
        relativeLayoutContactar = vista.findViewById(R.id.relativeLayoutContactarAjustesCliente);
        relativeLayoutTyC = vista.findViewById(R.id.relativeLayoutTyCAjustesCliente);
        relativeLayoutCerrarSesion = vista.findViewById(R.id.relativeLayoutSesionAjustesCliente);
        relativeLayoutSalir = vista.findViewById(R.id.relativeLayoutSalirAjustesCliente);
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(getContext());
        usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();

    }

    private void obtenerHelper() {
        if (getActivity() instanceof ClienteActivity) {
            activityCliente = ((ClienteActivity) getActivity());
            helperMenuPrincipal = activityCliente.getHelperMenuPrincipal();
            helperAjustes = activityCliente.getHelperAjustes();

        }
    }


    private void volverAlMenuDesdeAjustes(){
        imageViewVolverMenu.setOnClickListener(v -> {
            if(helperMenuPrincipal != null){
                activityCliente.volverMenuPrincipal();
            } else {
                Log.e("ClienteAjustesFragment", "No se pudo volver al menú principal");
            }

        });
    }

    private void introducirNombreUsuarioAjustes(){
        correo = activityCliente.getCorreo();
        String nombre = usuarioConsulta.obtenerNombreYApellidos(correo);

        if(correo != null && nombre != null){
            textViewNombre.setText(nombre);
            helperAjustes.cargarNombreCabeceraDesdeFirebase(correo, textViewNombre);
        } else {
            helperAjustes.cargarNombreCabeceraDesdeFirebase(correo, textViewNombre);
        }

    }

    private void modoOscuro() {
        String correo = activityCliente.getCorreo();
        if(helperAjustes != null ){
            helperAjustes.modoOscuro(correo, switchCompatBotonModoOscuro, progressBarModoOscuro,  getContext());

        }else{
            Log.e("ClienteAjustesFragment", "Error al cargar el modo oscuro");
        }
    }

    private void realizarLlamada(){
        relativeLayoutContactar.setOnClickListener(v -> contactarTaller());
    }

    private void contactarTaller(){
        // Número de teléfono del taller
        String telefonoTaller = "+34 123456789";

        // Crear un Intent para iniciar la llamada
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + telefonoTaller));

        // Iniciar la actividad de llamada
        startActivity(intent);
    }

    // Cargar la preferencia del modo oscuro desde SharedPreferences
    private void cargarPreferenciaModoOscuro() {
        if(helperAjustes != null){
            helperAjustes.cargarPreferenciaModoOscuro(switchCompatBotonModoOscuro, getContext());
        }else{
            Log.e("ClienteAjustesFragment", "No se puede cargar la preferencia de modo oscuro");
        }
    }

    private void mostrarTyC(){
        relativeLayoutTyC.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.mostrarTerminosYCondiciones(getContext());
            }else{
                Log.e("ClienteAjustesFragment", "helperAjustes null: no se pudo mostrar términos y condiciones");
            }
        });

    }

    private void cerrarSesion(){
        relativeLayoutCerrarSesion.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.cerrarSesion(getContext());

            } else {
                Log.e("ClienteAjustesFragment", "No se pudo cerrar sesión");
            }
        });
    }

    private void salir(){
        relativeLayoutSalir.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.salir(getContext());

            } else {
                Log.e("ClienteAjustesFragment", "No se pudo salir de la app");
            }
        });
    }


}