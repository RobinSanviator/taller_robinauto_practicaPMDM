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
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class AdministradorMenuPrincipalFragment extends Fragment {

    private TextView textViewNombreCabecera;
    private CardView cardViewEmpleados, cardViewUsuarios;

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

        textViewNombreCabecera = vista.findViewById(R.id.textViewNombreUsuarioCabeceraAdmin);
        cardViewEmpleados = vista.findViewById(R.id.cardViewDarAltaBajaAdministrador);
        cardViewUsuarios = vista.findViewById(R.id.cardViewUsuariosAdministrador);

        obtenerDatosUsuarioCabecera();
        mostrarFragmentoEmpleados();
        mostrarFragmentoUsuarios();

        return vista;
    }

    public void obtenerDatosUsuarioCabecera() {

            if (getActivity() != null) {

                BBDDUsuariosSQLite baseDeDatosGestionUsuarios = new BBDDUsuariosSQLite(
                        getActivity(), "gestion_usuario_taller", null, 3);

                String correo  = getActivity().getIntent().getStringExtra("correo");

                // Obtén el nombre completo del usuario desde la base de datos
                String nombreCompleto = baseDeDatosGestionUsuarios.obtenerNombreYApellidos(correo);

                // Establece 'Administrador' por defecto en caso de error en la consulta
                if (nombreCompleto != null) {
                    textViewNombreCabecera.setText(nombreCompleto);
                } else {
                    textViewNombreCabecera.setText("Administrador");
                }
            }
    }

    public void mostrarFragmentoEmpleados(){
        cardViewEmpleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //Obtener la actividad e instanciar Administrador y llamar a sus métodos
                    if(getActivity() instanceof  AdministradorActivity){
                        ((AdministradorActivity) getActivity()).cargarFragmentoNavegacionInferiorAdministrador(new AdministradorGestionEmpleadosFragment());
                        ((AdministradorActivity) getActivity()).deseleccionarItemMenuPrincipal();
                    }
            }
        });
    }

    public void mostrarFragmentoUsuarios(){
        cardViewUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtener la actividad e instanciar Administrador y llamar a sus métodos
                if(getActivity() instanceof  AdministradorActivity){
                    ((AdministradorActivity) getActivity()).cargarFragmentoNavegacionInferiorAdministrador(new AdministradorModificarUsuariosFragment());
                    ((AdministradorActivity) getActivity()).deseleccionarItemMenuPrincipal();
                }
            }
        });
    }


}

