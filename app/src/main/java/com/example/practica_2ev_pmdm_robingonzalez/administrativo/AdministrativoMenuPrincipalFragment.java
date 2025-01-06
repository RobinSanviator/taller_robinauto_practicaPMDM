package com.example.practica_2ev_pmdm_robingonzalez.administrativo;

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


public class AdministrativoMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;
    private CardView cardViewRegistroCoches, cardViewReparaciones, cardViewNotificaciones, cardViewInventario ;
    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperNavegacionInferior helperNavegacionInferior;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflar diseño del layout del menú principal
        View vista = inflater.inflate(R.layout.administrativo_menu_principal_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        obtenerDatosUsuarioCabecera();
        inicializarListeners();

        return vista;
    }

    private void inicializarComponentes(View vista){
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraAdministrativo);
        cardViewRegistroCoches = vista.findViewById(R.id.cardViewRegistroEntradaCochesAdministrativo);
        cardViewReparaciones = vista.findViewById(R.id.cardViewReparacionesAdministrativo);
        cardViewNotificaciones = vista.findViewById(R.id.cardViewNotificacionAdministrativo);
        cardViewInventario = vista.findViewById(R.id.cardViewInventarioAdministrativo);
    }

    private void obtenerHelper(){
        if(getActivity() instanceof AdministrativoActivity){
            helperMenuPrincipal = (((AdministrativoActivity) getActivity()).getHelperFragmento());
            helperNavegacionInferior = (((AdministrativoActivity) getActivity()).getHelperNavegacionInferior());

        }

    }

    private void obtenerDatosUsuarioCabecera() {
        String correo = getActivity().getIntent().getStringExtra("correo");
        if(helperMenuPrincipal != null && correo != null){
            helperMenuPrincipal.obtenerDatosUsuario(correo, textViewNombreCabecera);
        }else {
            Log.e("AdministrativoMenuPrincipalFragment", "Error al obtener los datos del usuario en la cabecera");
            textViewNombreCabecera.setText("Usuario no disponible");
        }
    }

    private void inicializarListeners(){
        //Mostrar pantalla de registro de entrada de coches
        configurarOnclick(cardViewRegistroCoches, new AdministrativoRegistroCochesFragment());
        //Mostrar pantalla de reparaciones
        configurarOnclick(cardViewReparaciones, new AdministrativoReparacionesFragment());
        //Mostrar pantalla de notificaciones
        configurarOnclick(cardViewNotificaciones, new AdministrativoNotificacionesFragment());
        //Mostrar pantalla del inventario
        configurarOnclick(cardViewInventario, new AdministrativoInventarioFragment());
    }

    private void configurarOnclick(CardView cardView, Fragment fragmento){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperMenuPrincipal != null && helperNavegacionInferior != null){
                    helperMenuPrincipal.cargarFragmento(fragmento);
                    helperNavegacionInferior.deseleccionarItemMenuPrincipal();
                }  else {
                    Log.e("AdministrativoMenuPrincipalFragment", "Error en configurarOnClick de los cardView");

                }
            }
        });
    }

}