package com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico;

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
import com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico_jefe.MecanicoJefeActivity;


public class MecanicoMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;
    private CardView cardViewTareas, cardViewPiezas;
    private MecanicoActivity mecanicoActivity;
    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperNavegacionInferior helperNavegacionInferior;
    private String correo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del layout del menú principal
        View vista = inflater.inflate(R.layout.mecanico_menu_principal_fragment, container, false);


        inicializarComponentes(vista);
        obtenerHelper();
        obtenerDatosUsuarioCabecera();
        inicializarListeners();

        return vista;
    }

    private void inicializarComponentes(View vista){
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraMecanico);
        cardViewTareas = vista.findViewById(R.id.cardViewTareasMecanico);
        cardViewPiezas = vista.findViewById(R.id.cardViewPiezasMecanico);
    }

    private void obtenerHelper(){
        if(getActivity() instanceof MecanicoActivity){
            mecanicoActivity =((MecanicoActivity) getActivity());
            helperMenuPrincipal = (((MecanicoActivity) getActivity()).getHelperFragmento());
            helperNavegacionInferior = (((MecanicoActivity) getActivity()).getHelperNavegacionInferior());

        }

    }

    private void obtenerDatosUsuarioCabecera() {
        correo = mecanicoActivity.getCorreo();
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
        //Mostrar pantalla de registro de entrada de coches
        configurarOnclick(cardViewTareas, new MecanicoTareasFragment());
        //Mostrar pantalla de reparaciones
        configurarOnclick(cardViewPiezas, new MecanicoPiezasFragment());
    }

    private void configurarOnclick(CardView cardView, Fragment fragmento){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperMenuPrincipal != null && helperNavegacionInferior != null){
                    helperMenuPrincipal.cargarFragmento(fragmento);
                    helperNavegacionInferior.deseleccionarItemMenuPrincipal();
                }  else {
                    Log.e("MecanicoMenuPrincipalFragment", "Error en configurarOnClick de los cardView");

                }
            }
        });
    }
}