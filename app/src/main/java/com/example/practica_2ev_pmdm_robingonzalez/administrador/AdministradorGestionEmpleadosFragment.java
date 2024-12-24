package com.example.practica_2ev_pmdm_robingonzalez.administrador;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AdministradorGestionEmpleadosFragment extends Fragment {

    private ImageView imageViewVolver;
    private ChipNavigationBar chipNavigationBarNavegacionInferior;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout gestion de empleados
        View vista = inflater.inflate(R.layout.administrador_gestion_empleados_fragment, container, false);
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalDesdeEmpleados);
        volverMenuPrincipalDesdeEmpleados();

        return vista;
    }

    public void volverMenuPrincipalDesdeEmpleados(){
        imageViewVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  //Obtener la actividad e instanciar Administrador y llamar a sus métodos
                  if (getActivity() instanceof AdministradorActivity) {
                      ((AdministradorActivity)getActivity()).cargarFragmentoNavegacionInferiorAdministrador(new AdministradorMenuPrincipalFragment());
                      ((AdministradorActivity) getActivity()).seleccionarItemMenuPrincipal();
                  }
              }

        });
    }



}