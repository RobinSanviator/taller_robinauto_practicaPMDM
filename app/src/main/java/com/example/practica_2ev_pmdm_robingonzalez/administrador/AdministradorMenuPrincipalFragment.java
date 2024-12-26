package com.example.practica_2ev_pmdm_robingonzalez.administrador;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.navegacion.ManejadorFragmento;
import com.example.practica_2ev_pmdm_robingonzalez.navegacion.ManejadorNavegacionInferior;


public class AdministradorMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;
    private CardView cardViewEmpleados, cardViewUsuarios;
    private ManejadorFragmento manejadorFragmento;
    private ManejadorNavegacionInferior manejadorNavegacionInferior;

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
            manejadorFragmento= (((AdministradorActivity) getActivity()).getManejadorFragmento());
            manejadorNavegacionInferior = (((AdministradorActivity) getActivity()).getManejadorNavegacionInferior());

        }

    }

    private void obtenerDatosUsuarioCabecera() {
        String correo = getActivity().getIntent().getStringExtra("correo");
        if(manejadorFragmento != null && correo != null){
            manejadorFragmento.obtenerDatosUsuario(correo, textViewNombreCabecera);
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
                if(manejadorFragmento != null && manejadorNavegacionInferior != null){
                    manejadorFragmento.cargarFragmento(fragmento);
                    manejadorNavegacionInferior.deseleccionarItemMenuPrincipal();
                } else {
                   manejadorFragmento.cargarFragmento(new AdministradorMenuPrincipalFragment());
                }
            }
        });
    }


}

