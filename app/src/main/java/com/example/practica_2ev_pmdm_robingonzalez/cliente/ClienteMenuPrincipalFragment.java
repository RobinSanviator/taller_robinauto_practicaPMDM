package com.example.practica_2ev_pmdm_robingonzalez.cliente;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.mecanico.MecanicoMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperFragmento;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;

public class ClienteMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;
    private CardView cardViewContactarTaller, cardViewReparaciones;
    private HelperFragmento helperFragmento;
    private HelperNavegacionInferior helperNavegacionInferior;

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
        obtenerManejadoresNavegacion();
        obtenerDatosUsuarioCabecera();
        inicializarListeners();

        return vista;
    }

    private void inicializarComponentes(View vista){
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraCliente);
        cardViewContactarTaller = vista.findViewById(R.id.cardViewContactarTallerCliente);
        cardViewReparaciones = vista.findViewById(R.id.cardViewReparacionesCliente);
    }

    private void obtenerManejadoresNavegacion(){
        if(getActivity() instanceof ClienteActivity){
            helperFragmento = (((ClienteActivity) getActivity()).getManejadorFragmento());
            helperNavegacionInferior = (((ClienteActivity) getActivity()).getManejadorNavegacionInferior());

        }

    }

    private void obtenerDatosUsuarioCabecera() {
        String correo = getActivity().getIntent().getStringExtra("correo");
        if (correo != null && helperFragmento != null) {
            helperFragmento.obtenerDatosUsuario(correo, textViewNombreCabecera);
        } else {
            Log.e("Error", "El correo es null o el manejador no está inicializado");
            textViewNombreCabecera.setText("Usuario no disponible");
        }
    }

    private void inicializarListeners(){
        //Mostrar pantalla de tareas
        configurarOnclick(cardViewContactarTaller, new ClienteContactarTallerFragment());
        //Mostrar pantalla de solicitud de piezas
        configurarOnclick(cardViewReparaciones, new ClienteReparacionesFragment());
    }

    private void configurarOnclick(CardView cardView, Fragment fragmento){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperFragmento != null && helperNavegacionInferior != null){
                    helperFragmento.cargarFragmento(fragmento);
                    helperNavegacionInferior.deseleccionarItemMenuPrincipal();
                } else {
                    Log.e("Error", "Los manejadores no están inicializados.");
                    helperFragmento.cargarFragmento(new MecanicoMenuPrincipalFragment());
                }
            }
        });
    }

}