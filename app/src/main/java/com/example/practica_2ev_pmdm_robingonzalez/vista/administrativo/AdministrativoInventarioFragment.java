package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.google.android.material.button.MaterialButton;


public class AdministrativoInventarioFragment extends Fragment {

    private ImageView imageViewVolver;
    private AdministrativoActivity activityAdministrativo;
    private MaterialButton buttonConsultarPiezas, buttonHacerPedido;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout inventario
        View vista = inflater.inflate(R.layout.administrativo_inventario_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeInventario();
        cargarFragmentoPorDefectoPiezas();
        configurarBotones();
        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalInventario);
        buttonConsultarPiezas = vista.findViewById(R.id.buttonConsultarPiezas);
        buttonHacerPedido = vista.findViewById(R.id.buttonHacerPedido);
    }

    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
        } else {
            Log.e("AdministrativoInventarioFragment", "Error al obtener helper");
        }
    }

    private void volverMenuPrincipalDesdeInventario(){
        imageViewVolver.setOnClickListener(v -> activityAdministrativo.volverMenuPrincipal());
    }

    private void cargarFragmentoPorDefectoPiezas(){
        // Cargar fragmento inicial y marcar botón
        cargarFragmento(new AdministrativoInventarioPiezaFragment(), buttonConsultarPiezas);
    }

    private void configurarBotones(){
        buttonConsultarPiezas.setOnClickListener(v ->
                cargarFragmento(new AdministrativoInventarioPiezaFragment(), buttonConsultarPiezas)
        );

        buttonHacerPedido.setOnClickListener(v ->
                cargarFragmento(new AdministrativoInventarioPedidoFragment(), buttonHacerPedido)
        );
    }

    private void cargarFragmento(Fragment fragmento, MaterialButton botonSeleccionado) {
        // Resetear todos los botones
        buttonConsultarPiezas.setBackgroundColor(getResources().getColor(R.color.color_desactivado_fondo));
        buttonHacerPedido.setBackgroundColor(getResources().getColor(R.color.color_desactivado_fondo));

        // Resaltar botón seleccionado
        botonSeleccionado.setBackgroundColor(getResources().getColor(R.color.verde));

        // Cargar fragmento
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutContenedorFragmentoStockYpedidos, fragmento)
                .addToBackStack(null)
                .commit();
    }
}