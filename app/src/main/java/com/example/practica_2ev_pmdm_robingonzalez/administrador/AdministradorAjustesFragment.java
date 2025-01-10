package com.example.practica_2ev_pmdm_robingonzalez.administrador;


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


public class AdministradorAjustesFragment extends Fragment {

    private SwitchCompat switchCompatBotonModoOscuro;
    private ProgressBar progressBarModoOscuro;
    private ImageView imageViewVolverMenu;
    private TextView textViewNombre;
    private String correo;
    private RelativeLayout relativeLayoutCerrarSesion, relativeLayoutSalir;
    private AdministradorActivity activityAdministrador;
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
    private UsuarioConsulta usuarioConsulta;
    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperAjustes helperAjustes;
    //Firebase
    private FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflar diseño del layout de ajustes
        View vista = inflater.inflate(R.layout.administrador_ajustes_fragment, container, false);

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
        switchCompatBotonModoOscuro = vista.findViewById(R.id.switchBotonModoOscuroAjustesAdmin);
        progressBarModoOscuro = vista.findViewById(R.id.progressBarModoOscuroAjustesAdmin);
        imageViewVolverMenu = vista.findViewById(R.id.imageViewVolverMenuPrincipalAjustesAdmin);
        textViewNombre = vista.findViewById(R.id.textViewNombreAjustesAdmin);
        relativeLayoutCerrarSesion = vista.findViewById(R.id.relativeLayoutSesionAjustesAdmin);
        relativeLayoutSalir = vista.findViewById(R.id.relativeLayoutSalirAjustesAdmin);
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(getContext());
        usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();

    }

    private void obtenerHelper() {
        if (getActivity() instanceof AdministradorActivity) {
            activityAdministrador = ((AdministradorActivity) getActivity());
            helperMenuPrincipal = activityAdministrador.getHelperMenuPrincipal();
            helperAjustes = activityAdministrador.getHelperAjustes();

        }
    }

    private void volverAlMenuDesdeAjustes(){
        imageViewVolverMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperMenuPrincipal != null){
                    activityAdministrador.volverMenuPrincipal();
                } else {
                    Log.e("Error", "helperFragmento null: no se pudo volver al menú principal");
                }

            }
        });
    }

    private void introducirNombreUsuarioAjustes(){
        correo = activityAdministrador.getCorreo();
        String nombre = usuarioConsulta.obtenerNombreYApellidos(correo);

        if(correo != null && nombre != null){
            textViewNombre.setText(nombre);

        } else {
            Log.e("Error", "Correo o nombre null");
        }

    }

    private void modoOscuro() {
        if(helperAjustes != null ){
            helperAjustes.modoOscuro(switchCompatBotonModoOscuro, progressBarModoOscuro,  getContext());

        }else{
            Log.e("Error", "helperAjustes null");
        }
    }


    // Cargar la preferencia del modo oscuro desde SharedPreferences
    private void cargarPreferenciaModoOscuro() {
      if(helperAjustes != null){
          helperAjustes.cargarPreferenciaModoOscuro(switchCompatBotonModoOscuro, getContext());
      }else{
          Log.e("Error", "helperAjustes null: no se pudo cargar la precerencia de modo oscuro");
      }
    }


    private void cerrarSesion(){
        relativeLayoutCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperAjustes != null){
                    helperAjustes.cerrarSesion(getContext());

                } else {
                    Log.e("AdministradorAjustesFragment", "helperAjustes null: no se pudo cerrar sesión");
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
                    Log.e("Error", "helperAjustes null: no se pudo salir de la app");
                }
            }
        });
    }


}

