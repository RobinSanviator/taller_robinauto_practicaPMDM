package com.example.practica_2ev_pmdm_robingonzalez.mecanico_jefe;

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


public class MecanicoJefeMenuPrincipalFragment extends Fragment {
    private TextView textViewNombreCabecera;
    private CardView cardViewDiagnosticos, cardViewTareas;
    private HelperFragmento helperFragmento;
    private HelperNavegacionInferior helperNavegacionInferior;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflar diseño del layout del menú principal
        View vista = inflater.inflate(R.layout.mecanico_jefe_menu_principal_fragment, container, false);

        inicializarComponentes(vista);
        obtenerManejadoresNavegacion();
        obtenerDatosUsuarioCabecera();
        inicializarListeners();

        return vista;
    }

    private void inicializarComponentes(View vista){
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraMjefe);
        cardViewDiagnosticos = vista.findViewById(R.id.cardViewDiagnosticosMjefe);
        cardViewTareas = vista.findViewById(R.id.cardViewTareasMjefe);
    }

    private void obtenerManejadoresNavegacion(){
        if (getActivity() instanceof MecanicoJefeActivity) {
            helperFragmento = ((MecanicoJefeActivity) getActivity()).getHelperFragmento();
            helperNavegacionInferior = ((MecanicoJefeActivity) getActivity()).getHelperNavegacionInferior();
        } else {
            Log.e("Error", "La actividad no pertenece a MecanicoJefeActivity");
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
        //Mostrar pantalla de diagnósticos
        configurarOnclick(cardViewDiagnosticos, new MecanicoJefeDiagnosticosFragment());
        //Mostrar pantalla de tareas
        configurarOnclick(cardViewTareas, new MecanicoJefeConsultarTareasFragment());
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
                    helperFragmento.cargarFragmento(new MecanicoJefeMenuPrincipalFragment());
                }
            }
        });
    }

}