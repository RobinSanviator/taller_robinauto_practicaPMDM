package com.example.practica_2ev_pmdm_robingonzalez.vista.cliente;

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

public class ClienteMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;
    private CardView cardViewContactar, cardViewReparaciones ;
    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperNavegacionInferior helperNavegacionInferior;
    private ClienteActivity activityCliente;
    private String correo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout del menú principal
        View vista = inflater.inflate(R.layout.cliente_menu_principal_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        obtenerDatosUsuarioCabecera();
        inicializarListeners();

        return vista;
    }
    private void inicializarComponentes(View vista){
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraCliente);
        cardViewReparaciones = vista.findViewById(R.id.cardViewReparacionesCliente);
        cardViewContactar = vista.findViewById(R.id.cardViewContactarTallerCliente);
    }

    private void obtenerHelper(){
        if(getActivity() instanceof ClienteActivity) {
            activityCliente = ((ClienteActivity) getActivity());
            helperNavegacionInferior = activityCliente.getHelperNavegacionInferior();
            helperMenuPrincipal = activityCliente.getHelperMenuPrincipal();

        }else {
            Log.e("ClienteMenuPrincipalFragment", "Error al obtener helper");
        }


    }

    private void obtenerDatosUsuarioCabecera() {
        correo = activityCliente.getCorreo();
        if (correo != null) {
            helperMenuPrincipal.obtenerDatosUsuario(correo, textViewNombreCabecera);
            helperMenuPrincipal.cargarNombreCabeceraDesdeFirebase(correo,textViewNombreCabecera);
            helperNavegacionInferior.seleccionarItemMenuPrincipal();
        } else {
            helperMenuPrincipal.cargarNombreCabeceraDesdeFirebase(null,textViewNombreCabecera);
            helperNavegacionInferior.seleccionarItemMenuPrincipal();
        }
    }

    private void inicializarListeners(){
        //Mostrar pantalla de reparaciones
        configurarOnclick(cardViewReparaciones, new ClienteReparacionesFragment());
        //Contactar con el taller
        configurarOnclick(cardViewContactar, new ClienteContactarTallerFragment());
    }

    private void configurarOnclick(CardView cardView, Fragment fragmento){
        cardView.setOnClickListener(v -> {
            if(helperMenuPrincipal != null && helperNavegacionInferior != null){
                helperMenuPrincipal.cargarFragmento(fragmento);
                helperNavegacionInferior.deseleccionarItemMenuPrincipal();
            }  else {
                Log.e("ClienteMenuPrincipalFragment", "Error en configurarOnClick de los cardView");

            }
        });
    }

}