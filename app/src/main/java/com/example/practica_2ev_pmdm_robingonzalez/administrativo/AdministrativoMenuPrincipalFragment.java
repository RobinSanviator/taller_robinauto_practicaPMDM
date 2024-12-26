package com.example.practica_2ev_pmdm_robingonzalez.administrativo;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorActivity;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorGestionEmpleadosFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorModificarUsuariosFragment;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.navegacion.ManejadorFragmento;
import com.example.practica_2ev_pmdm_robingonzalez.navegacion.ManejadorNavegacionInferior;


public class AdministrativoMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;
    private CardView cardViewRegistroCoches, cardViewReparaciones, cardViewNotificaciones, cardViewInventario ;
    private ManejadorFragmento manejadorFragmento;
    private ManejadorNavegacionInferior manejadorNavegacionInferior;

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
        obtenerManejadoresNavegacion();
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

    private void obtenerManejadoresNavegacion(){
        if(getActivity() instanceof AdministrativoActivity){
            manejadorFragmento= (((AdministrativoActivity) getActivity()).getManejadorFragmento());
            manejadorNavegacionInferior = (((AdministrativoActivity) getActivity()).getManejadorNavegacionInferior());

        }

    }

    private void obtenerDatosUsuarioCabecera() {
        String correo = getActivity().getIntent().getStringExtra("correo");
        if(manejadorFragmento != null && correo != null){
            manejadorFragmento.obtenerDatosUsuario(correo, textViewNombreCabecera);
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