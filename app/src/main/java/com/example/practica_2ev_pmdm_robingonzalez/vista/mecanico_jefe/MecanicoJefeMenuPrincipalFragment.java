package com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico_jefe;

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


public class MecanicoJefeMenuPrincipalFragment extends Fragment {
    private TextView textViewNombreCabecera;
    private CardView cardViewDiagnosticos, cardViewTareas;
    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperNavegacionInferior helperNavegacionInferior;
    private MecanicoJefeActivity mecanicoJefeActivity;
    private String correo;

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
        obtenerHelper();
        obtenerDatosUsuarioCabecera();
        inicializarListeners();

        return vista;
    }

    private void inicializarComponentes(View vista){
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraMjefe);
        cardViewDiagnosticos = vista.findViewById(R.id.cardViewDiagnosticosMjefe);
        cardViewTareas = vista.findViewById(R.id.cardViewTareasMjefe);
    }

    private void obtenerHelper(){
        if (getActivity() instanceof MecanicoJefeActivity) {
          mecanicoJefeActivity =((MecanicoJefeActivity) getActivity());
            helperNavegacionInferior = mecanicoJefeActivity.getHelperNavegacionInferior();
            helperMenuPrincipal = mecanicoJefeActivity.getHelperMenuPrincipal();
        } else {
            Log.e("MecanicoJefeMenuPrincipalFragment", "Error al obtener helper");
        }


    }

    private void obtenerDatosUsuarioCabecera() {
        correo = mecanicoJefeActivity.getCorreo();
        if (correo != null) {
            helperMenuPrincipal.obtenerDatosUsuario(correo, textViewNombreCabecera);
            helperMenuPrincipal.cargarNombreCabeceraDesdeFirebase(correo,textViewNombreCabecera);
        } else {
            helperMenuPrincipal.cargarNombreCabeceraDesdeFirebase(correo,textViewNombreCabecera);
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
                if(helperMenuPrincipal != null && helperNavegacionInferior != null){
                    helperMenuPrincipal.cargarFragmento(fragmento);
                    helperNavegacionInferior.deseleccionarItemMenuPrincipal();
                } else {
                    Log.e("MecanicoJefeMenuPrincipalFragment", "Error en configurarOnClick de los cardView");

                }
            }
        });
    }

}