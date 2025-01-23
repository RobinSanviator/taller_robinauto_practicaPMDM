package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.PiezaAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.PiezaPedidoAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.PiezaUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.PedidoProveedor;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Pieza;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdministrativoInventarioPedidoFragment extends Fragment  implements PiezaPedidoAdapter.OnCantidadChangeListener {


    private AdministrativoActivity activityAdministrativo;
    private RecyclerView recyclerViewPiezaPedido;
    private List<Pieza> listaPiezaPedido;
    private PiezaPedidoAdapter piezaPedidoAdapter;
    private TextView textViewMostrarPrecio;
    private MaterialButton materialButtonConfirmarPedido;
    private PedidoProveedor pedidoProveedor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar layout diseño del pedido al proveedor
        View vista = inflater.inflate(R.layout.administrativo_inventario_pedido_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        configurarRecyclerView();
        cargarPiezas();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        recyclerViewPiezaPedido = vista.findViewById(R.id.recyclerViewPedidoProveedor);
        textViewMostrarPrecio = vista.findViewById(R.id.textViewMostrarPrecioTotalPedido);
        materialButtonConfirmarPedido = vista.findViewById(R.id.buttonConfirmarPedido);
        pedidoProveedor = new PedidoProveedor();

    }


    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
        } else {
            Log.e("AdministrativoInventarioPedidoFragment", "Error al obtener helper");
        }
    }

    private void configurarRecyclerView() {
        listaPiezaPedido = new ArrayList<>();
        piezaPedidoAdapter = new PiezaPedidoAdapter(listaPiezaPedido, getContext(), this);
        recyclerViewPiezaPedido.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPiezaPedido.setAdapter(piezaPedidoAdapter);
    }

    @Override
    public void onCantidadChanged(Pieza pieza, int cantidad) {
       // pedidoProveedor.(pieza, cantidad);
        actualizarPrecioTotal();
    }


    private void cargarPiezas() {
        PiezaUtil.cargarPiezas(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPiezaPedido.clear();
                for (DataSnapshot piezaSnapshot : snapshot.getChildren()) {
                    Pieza pieza = piezaSnapshot.getValue(Pieza.class);
                    if (pieza != null) {
                        listaPiezaPedido.add(pieza);
                    }
                }
                Log.d("Fragment", "Número de piezas cargadas: " + listaPiezaPedido.size());
                piezaPedidoAdapter.notifyDataSetChanged();

                // Actualizar las piezas en el pedido y calcular el precio total
                pedidoProveedor.setPiezasSolicitadas(listaPiezaPedido);
                actualizarPrecioTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Fragment", "Error al cargar piezas: " + error.getMessage());
            }
        });
    }

    // Método para actualizar el precio total en el TextView
    private void actualizarPrecioTotal() {
        double precioTotal = pedidoProveedor.getPrecioTotal();
        textViewMostrarPrecio.setText(String.format(getString(R.string.precioTotal), precioTotal));
    }
}
