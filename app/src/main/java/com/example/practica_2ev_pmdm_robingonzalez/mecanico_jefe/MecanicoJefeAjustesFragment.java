package com.example.practica_2ev_pmdm_robingonzalez.mecanico_jefe;

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


public class MecanicoJefeAjustesFragment extends Fragment {

    private SwitchCompat switchCompatBotonModoOscuro;
    private ProgressBar progressBarModoOscuro;
    private ImageView imageViewVolverMenu;
    private TextView textViewNombre;
    private String correo;
    private RelativeLayout relativeLayoutCerrarSesion, relativeLayoutSalir;
    private MecanicoJefeActivity mecanicoJefeActivity;
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
        // Inflar diseño del layout ajustes
        View vista = inflater.inflate(R.layout.mecanico_jefe_ajustes_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        cargarPreferenciaModoOscuro();
        modoOscuro();
        volverAlMenuDesdeAjustes();
        introducirNombreUsuarioAjustes();
        cerrarSesion();
        salir();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        switchCompatBotonModoOscuro = vista.findViewById(R.id.switchBotonModoOscuroAjustesMjefe);
        progressBarModoOscuro = vista.findViewById(R.id.progressBarModoOscuroAjustesMjefe);
        imageViewVolverMenu = vista.findViewById(R.id.imageViewVolverMenuPrincipalAjustesMjefe);
        textViewNombre = vista.findViewById(R.id.textViewNombreAjustesMjefe);
        relativeLayoutCerrarSesion = vista.findViewById(R.id.relativeLayoutSesionAjustesMjefe);
        relativeLayoutSalir = vista.findViewById(R.id.relativeLayoutSalirAjustesMjefe);
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(getContext());
        usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();

    }

    private void obtenerHelper() {
        if (getActivity() instanceof MecanicoJefeActivity) {
           mecanicoJefeActivity = ((MecanicoJefeActivity) getActivity());
            helperMenuPrincipal = mecanicoJefeActivity.getHelperMenuPrincipal();
            helperAjustes = mecanicoJefeActivity.getHelperAjustes();

        }
    }


    private void volverAlMenuDesdeAjustes(){
        imageViewVolverMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperMenuPrincipal != null){
                    mecanicoJefeActivity.volverMenuPrincipal();
                } else {
                    Log.e("MecanicoJefeAjustesFragment", "No se pudo volver al menú principal");
                }

            }
        });
    }

    private void introducirNombreUsuarioAjustes(){
        correo = mecanicoJefeActivity.getCorreo();
        String nombre = usuarioConsulta.obtenerNombreYApellidos(correo);

        if(correo != null && nombre != null){
            textViewNombre.setText(nombre);
            helperAjustes.cargarNombreCabeceraDesdeFirebase(correo, textViewNombre);
        } else {
            helperAjustes.cargarNombreCabeceraDesdeFirebase(correo, textViewNombre);
        }

    }

    private void modoOscuro() {
        String correo = mecanicoJefeActivity.getCorreo();
        if(helperAjustes != null ){
            helperAjustes.modoOscuro(correo, switchCompatBotonModoOscuro, progressBarModoOscuro,  getContext());

        }else{
            Log.e("MecanicoJefeAjustesFragment", "Error al cargar el modo oscuro");
        }
    }


    // Cargar la preferencia del modo oscuro desde SharedPreferences
    private void cargarPreferenciaModoOscuro() {
        if(helperAjustes != null){
            helperAjustes.cargarPreferenciaModoOscuro(switchCompatBotonModoOscuro, getContext());
        }else{
            Log.e("MecanicoJefeAjustesFragment", "No se puede cargar la preferencia de modo oscuro");
        }
    }

    private void cerrarSesion(){
        relativeLayoutCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperAjustes != null){
                    helperAjustes.cerrarSesion(getContext());

                } else {
                    Log.e("MecanicoJefeAjustesFragment", "No se pudo cerrar sesión");
                }
            }
        });
    }

    private void salir(){
        relativeLayoutSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperAjustes != null){
                    helperAjustes.salir(getContext());

                } else {
                    Log.e("MecanicoJefeAjustesFragment", "No se pudo salir de la app");
                }
            }
        });
    }


}