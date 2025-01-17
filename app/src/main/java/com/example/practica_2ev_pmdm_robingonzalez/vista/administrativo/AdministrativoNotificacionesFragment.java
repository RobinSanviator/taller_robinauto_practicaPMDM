package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practica_2ev_pmdm_robingonzalez.R;


public class AdministrativoNotificacionesFragment extends Fragment {

    private ImageView imageViewVolver;
    private AdministrativoActivity activityAdministrativo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout para las notificaciones
        View vista = inflater.inflate(R.layout.administrativo_notificaciones_fragment, container, false);


        return vista;
    }

    private void inicializarComponenetes(View vista){
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalNotificaciones);
    }
}