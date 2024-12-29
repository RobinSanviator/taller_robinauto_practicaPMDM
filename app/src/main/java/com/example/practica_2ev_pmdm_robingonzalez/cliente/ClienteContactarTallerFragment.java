package com.example.practica_2ev_pmdm_robingonzalez.cliente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practica_2ev_pmdm_robingonzalez.R;


public class ClienteContactarTallerFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout de contacto con el taller
        View vista = inflater.inflate(R.layout.fragment_cliente_contactar_taller, container, false);

        return vista;
    }
}