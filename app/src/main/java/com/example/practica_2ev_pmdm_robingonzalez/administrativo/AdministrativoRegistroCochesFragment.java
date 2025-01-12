package com.example.practica_2ev_pmdm_robingonzalez.administrativo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorActivity;

public class AdministrativoRegistroCochesFragment extends Fragment {

    private ImageView imageViewVolver;
    private AdministrativoActivity activityAdministrativo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del layout de registro de coches
        View vista = inflater.inflate(R.layout.administrativo_registro_coches_fragment, container, false);

        inicializarComponentes(vista);


        return vista;
    }


    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalDesdeEmpleados);

    }

    private void volverMenuPrincipalDesdeRegistroCoches(){
        imageViewVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               activityAdministrativo.volverMenuPrincipal();
            }
        });
    }

}