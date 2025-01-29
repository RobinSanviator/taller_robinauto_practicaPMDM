package com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practica_2ev_pmdm_robingonzalez.R;


public class MecanicoPiezasFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout piezas mecánico
        View vista = inflater.inflate(R.layout.mecanico_piezas_fragment, container, false);

        return vista;
    }
}