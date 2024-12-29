package com.example.practica_2ev_pmdm_robingonzalez.administrador;

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
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperFragmento;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;


public class AdministradorMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;
    private CardView cardViewEmpleados, cardViewUsuarios;
    private HelperFragmento helperFragmento;
    private HelperNavegacionInferior helperNavegacionInferior;
    private AdministradorActivity activityAdministrador;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflar diseño del layout del menú principal
        View vista = inflater.inflate(R.layout.administrador_menu_principal_fragment, container, false);

        inicializarComponentes(vista);
        obtenerManejadoresNavegacion();
        obtenerDatosUsuarioCabecera();
        inicializarListeners();

        return vista;
    }

    private void inicializarComponentes(View vista){
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraAdmin);
        cardViewEmpleados = vista.findViewById(R.id.cardViewDarAltaBajaAdministrador);
        cardViewUsuarios = vista.findViewById(R.id.cardViewUsuariosAdministrador);
    }

    private void obtenerManejadoresNavegacion(){
        if(getActivity() instanceof AdministradorActivity){
            activityAdministrador = ((AdministradorActivity) getActivity());
            helperFragmento = activityAdministrador.getHelperFragmento();
            helperNavegacionInferior = activityAdministrador.getHelperNavegacionInferior();

        }

    }

    private void obtenerDatosUsuarioCabecera() {

        if (helperFragmento != null && activityAdministrador != null) {
            String correo = activityAdministrador.getCorreo();
            helperFragmento.obtenerDatosUsuario(correo, textViewNombreCabecera);
        } else {
            Log.e("Error", "El correo es null o el manejador no está inicializado");
            textViewNombreCabecera.setText("Usuario no disponible");
        }
    }

    private void inicializarListeners(){
        //Mostrar pantalla de empleados
        configurarOnclick(cardViewEmpleados, new AdministradorGestionEmpleadosFragment());
        //Mostrar pantalla de usuarios
        configurarOnclick(cardViewUsuarios, new AdministradorModificarUsuariosFragment());
    }

    private void configurarOnclick(CardView cardView, Fragment fragmento){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperFragmento != null && helperNavegacionInferior != null){
                    helperFragmento.cargarFragmento(fragmento);
                    helperNavegacionInferior.deseleccionarItemMenuPrincipal();
                }  else {
                    Log.e("Error", "Los manejadores no están inicializados.");
                    helperFragmento.cargarFragmento(new AdministradorMenuPrincipalFragment());
                }
            }
        });
    }


}

