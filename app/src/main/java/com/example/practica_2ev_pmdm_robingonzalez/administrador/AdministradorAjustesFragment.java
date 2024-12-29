package com.example.practica_2ev_pmdm_robingonzalez.administrador;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperAjustes;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperFragmento;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.google.android.material.snackbar.Snackbar;


public class AdministradorAjustesFragment extends Fragment {

    private SwitchCompat switchCompatBotonModoOscuro;
    private ProgressBar progressBarModoOscuro;
    private ImageView imageViewVolverMenu;
    private TextView textViewNombre;
    private String correo;
    private RelativeLayout relativeLayoutCerrarSesion;
    private AdministradorActivity activityAdministrador;
    private BBDDUsuariosSQLite baseDeDatos;
    private HelperFragmento helperFragmento;
    private HelperAjustes helperAjustes;
    private HelperNavegacionInferior helperNavegacionInferior;


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
        obtenerManejadores();
        cargarPreferenciaModoOscuro();
        modoOscuro();
        volverAlMenuDesdeAjustes();
        introducirNombreUsuarioAjustes();
        cerrarSesion();
        return vista;

    }

    private void inicializarComponentes(View vista) {
        switchCompatBotonModoOscuro = vista.findViewById(R.id.switchBotonModoOscuroAjustesAdmin);
        progressBarModoOscuro = vista.findViewById(R.id.progressBarModoOscuroAjustesAdmin);
        imageViewVolverMenu = vista.findViewById(R.id.imageViewVolverMenuPrincipalAjustesAdmin);
        textViewNombre = vista.findViewById(R.id.textViewNombreAjustesAdmin);
        relativeLayoutCerrarSesion = vista.findViewById(R.id.relativeLayoutSesionAjustesAdmin);

    }

    private void obtenerManejadores() {
        if (getActivity() instanceof AdministradorActivity) {
            activityAdministrador = ((AdministradorActivity) getActivity());
            helperFragmento = activityAdministrador.getHelperFragmento();
            helperNavegacionInferior = activityAdministrador.getHelperNavegacionInferior();
            helperAjustes = activityAdministrador.getHelperAjustes();
            baseDeDatos = activityAdministrador.obtenerInstanciaBaseDeDatos();
        }
    }

    private void irAMenuPrincipal(){
        if(helperFragmento != null && helperNavegacionInferior != null){
            helperFragmento.cargarFragmento(new AdministradorMenuPrincipalFragment());
            helperNavegacionInferior.seleccionarItemMenuPrincipal();

        }
    }

    private void volverAlMenuDesdeAjustes(){
        imageViewVolverMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperFragmento != null){
                    irAMenuPrincipal();
                } else {
                    Log.e("Error", "helperFragmento null");
                }

            }
        });
    }

    private void introducirNombreUsuarioAjustes(){
        correo = activityAdministrador.getCorreo();
        String nombre = baseDeDatos.obtenerNombreYApellidos(correo);

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
          Log.e("Error", "helperAjustes null");
      }
    }

    private void cerrarSesion(){
        relativeLayoutCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helperAjustes.cerrarSesion(getContext());

            }
        });
    }


}

