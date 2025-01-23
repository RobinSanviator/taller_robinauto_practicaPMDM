package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;


public class AdministrativoInventarioPedidoFragment extends Fragment {


    private AdministrativoActivity activityAdministrativo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar layout diseño del pedido al proveedor
        View vista = inflater.inflate(R.layout.fragment_administrativo_inventario_pedido, container, false);

        inicializarComponentes(vista);
        obtenerHelper();

        return vista;
    }

    private void inicializarComponentes(View vista) {

    }

    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
        } else {
            Log.e("AdministrativoInventarioPedidoFragment", "Error al obtener helper");
        }
    }



}