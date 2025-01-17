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


public class AdministradorMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;
    private CardView cardViewEmpleados, cardViewUsuarios;
    private String correo;
    private HelperMenuPrincipal helperMenuPrincipal;
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
        obtenerHelper();
        obtenerDatosUsuarioCabecera();
        inicializarListeners();

        return vista;
    }

    private void inicializarComponentes(View vista){
        cardViewEmpleados = vista.findViewById(R.id.cardViewDarAltaBajaAdministrador);
        cardViewUsuarios = vista.findViewById(R.id.cardViewUsuariosAdministrador);
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraAdmin);

    }

    private void obtenerHelper(){
        if(getActivity() instanceof AdministradorActivity) {
            activityAdministrador = ((AdministradorActivity) getActivity());
            helperNavegacionInferior = activityAdministrador.getHelperNavegacionInferior();
            helperMenuPrincipal = activityAdministrador.getHelperMenuPrincipal();

        }else {
                Log.e("AdministradorMenuPrincipalFragment", "Error al obtener helper");
            }

    }

    private void obtenerDatosUsuarioCabecera() {
            correo = activityAdministrador.getCorreo();
        if (correo != null) {
           helperMenuPrincipal.obtenerDatosUsuario(correo, textViewNombreCabecera);
           helperMenuPrincipal.cargarNombreCabeceraDesdeFirebase(correo,textViewNombreCabecera);
        } else {
            helperMenuPrincipal.cargarNombreCabeceraDesdeFirebase(correo,textViewNombreCabecera);
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
                if(helperMenuPrincipal != null && helperNavegacionInferior != null){
                    helperMenuPrincipal.cargarFragmento(fragmento);
                    helperNavegacionInferior.deseleccionarItemMenuPrincipal();
                }  else {
                    Log.e("AdministradorMenuPrincipalFragment", "Error en configurarOnClick de los cardView");

                }
            }
        });
    }


}

