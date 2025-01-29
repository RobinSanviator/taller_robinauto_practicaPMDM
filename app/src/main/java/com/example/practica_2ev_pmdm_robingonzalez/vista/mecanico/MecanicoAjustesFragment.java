package com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico;

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


public class MecanicoAjustesFragment extends Fragment {

    private SwitchCompat switchCompatBotonModoOscuro;
    private ProgressBar progressBarModoOscuro;
    private ImageView imageViewVolverMenu;
    private TextView textViewNombre;
    private String correo;
    private RelativeLayout relativeLayoutTyC, relativeLayoutCerrarSesion, relativeLayoutSalir;
    private MecanicoActivity activityMecanico;
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
        // Inflar diseño del layout ajustes del mecánico
        View vista= inflater.inflate(R.layout.mecanico_ajustes_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        cargarPreferenciaModoOscuro();
        modoOscuro();
        volverAlMenuDesdeAjustes();
        introducirNombreUsuarioAjustes();
        mostrarTyC();
        cerrarSesion();
        salir();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        switchCompatBotonModoOscuro = vista.findViewById(R.id.switchBotonModoOscuroAjustesMecanico);
        progressBarModoOscuro = vista.findViewById(R.id.progressBarModoOscuroAjustesMecanico);
        imageViewVolverMenu = vista.findViewById(R.id.imageViewVolverMenuPrincipalAjustesMecanico);
        textViewNombre = vista.findViewById(R.id.textViewNombreAjustesMecanico);
        relativeLayoutTyC = vista.findViewById(R.id.relativeLayoutTyCAjustesMecanico);
        relativeLayoutCerrarSesion = vista.findViewById(R.id.relativeLayoutSesionAjustesMecanico);
        relativeLayoutSalir = vista.findViewById(R.id.relativeLayoutSalirAjustesMecanico);
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(getContext());
        usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();

    }

    private void obtenerHelper() {
        if (getActivity() instanceof MecanicoActivity) {
            activityMecanico = ((MecanicoActivity) getActivity());
            helperMenuPrincipal = activityMecanico.getHelperMenuPrincipal();
            helperAjustes = activityMecanico.getHelperAjustes();

        }
    }


    private void volverAlMenuDesdeAjustes(){
        imageViewVolverMenu.setOnClickListener(v -> {
            if(helperMenuPrincipal != null){
                activityMecanico.volverMenuPrincipal();
            } else {
                Log.e("MecanicoAjustesFragment", "No se pudo volver al menú principal");
            }

        });
    }

    private void introducirNombreUsuarioAjustes(){
        correo = activityMecanico.getCorreo();
        String nombre = usuarioConsulta.obtenerNombreYApellidos(correo);

        if(correo != null && nombre != null){
            textViewNombre.setText(nombre);
            helperAjustes.cargarNombreCabeceraDesdeFirebase(correo, textViewNombre);
        } else {
            helperAjustes.cargarNombreCabeceraDesdeFirebase(correo, textViewNombre);
        }

    }

    private void modoOscuro() {
        String correo = activityMecanico.getCorreo();
        if(helperAjustes != null ){
            helperAjustes.modoOscuro(correo, switchCompatBotonModoOscuro, progressBarModoOscuro,  getContext());

        }else{
            Log.e("MecanicoAjustesFragment", "Error al cargar el modo oscuro");
        }
    }


    // Cargar la preferencia del modo oscuro desde SharedPreferences
    private void cargarPreferenciaModoOscuro() {
        if(helperAjustes != null){
            helperAjustes.cargarPreferenciaModoOscuro(switchCompatBotonModoOscuro, getContext());
        }else{
            Log.e("MecanicoAjustesFragment", "No se puede cargar la preferencia de modo oscuro");
        }
    }

    private void mostrarTyC(){
        relativeLayoutTyC.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.mostrarTerminosYCondiciones(getContext());
            }else{
                Log.e("MecanicoAjustesFragment", "helperAjustes null: no se pudo mostrar términos y condiciones");
            }
        });

    }

    private void cerrarSesion(){
        relativeLayoutCerrarSesion.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.cerrarSesion(getContext());

            } else {
                Log.e("MecanicoAjustesFragment", "No se pudo cerrar sesión");
            }
        });
    }

    private void salir(){
        relativeLayoutSalir.setOnClickListener(v -> {
            if(helperAjustes != null){
                helperAjustes.salir(getContext());

            } else {
                Log.e("MecanicoAjustesFragment", "No se pudo salir de la app");
            }
        });
    }

}