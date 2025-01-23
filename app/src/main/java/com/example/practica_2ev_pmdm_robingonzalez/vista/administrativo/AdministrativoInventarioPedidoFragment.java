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
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.PiezaPedidoAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.PiezaUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Pieza;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdministrativoInventarioPedidoFragment extends Fragment  implements PiezaPedidoAdapter.OnCantidadChangeListener {



    private RecyclerView recyclerViewPiezaPedido;
    private List<Pieza> listaPiezaPedido;
    private PiezaPedidoAdapter piezaPedidoAdapter;
    private TextView textViewMostrarPrecio;
    private MaterialButton materialButtonConfirmarPedido;




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
        actualizarPrecioTotal();
        configurarBotonConfirmar();


        return vista;
    }

    private void inicializarComponentes(View vista) {
        recyclerViewPiezaPedido = vista.findViewById(R.id.recyclerViewPedidoProveedor);
        textViewMostrarPrecio = vista.findViewById(R.id.textViewMostrarPrecioTotalPedido);
        materialButtonConfirmarPedido = vista.findViewById(R.id.buttonConfirmarPedido);
    }


    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {

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

    // Este método se llama cuando se actualiza la cantidad de una pieza
    @Override
    public void onCantidadChanged(Pieza pieza, int cantidad) {
        actualizarPrecioTotal(); // Recalcular el total

    }

    // Este método se llama cuando se actualiza el precio total
    @Override
    public void onPrecioTotalChanged(double precioTotal) {
        // Actualizar el precio total en la vista
        textViewMostrarPrecio.setText(String.format(getString(R.string.precioTotal), precioTotal));
    }

    private void cargarPiezas() {
        PiezaUtil.cargarPiezas(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPiezaPedido.clear();
                for (DataSnapshot piezaSnapshot : snapshot.getChildren()) {
                    Pieza pieza = piezaSnapshot.getValue(Pieza.class);
                    if (pieza != null) {
                        pieza.setCantidad(0); // Reinicia la cantidad local al cargar las piezas
                        listaPiezaPedido.add(pieza);
                    }
                }
                piezaPedidoAdapter.notifyDataSetChanged();
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
        double precioTotal = 0.0;
        for (Pieza pieza : listaPiezaPedido) {
            precioTotal += pieza.getCantidad() * pieza.getPrecio();
        }
        // Actualizar el precio total en el TextView
        textViewMostrarPrecio.setText(String.format(getString(R.string.precioTotal), precioTotal));
    }



    private void configurarBotonConfirmar() {
        materialButtonConfirmarPedido.setOnClickListener(v -> confirmarPedido());
    }

    private void confirmarPedido() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirmar Pedido")
                .setIcon(R.drawable.pieza)
                .setMessage("¿Seguro que quieres confirmar el pedido?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Actualizar las cantidades de las piezas directamente
                    for (Pieza pieza : listaPiezaPedido) {
                        int cantidadSeleccionada = pieza.getCantidad();
                        if (cantidadSeleccionada > 0) {
                            // Llamar a la función para actualizar la cantidad en Firebase
                            PiezaUtil.sumarCantidadPieza(pieza.getNombre(), cantidadSeleccionada);
                        }
                    }

                    // Mostrar mensaje de éxito
                    Snackbar.make(getView(), "Pedido realizado con éxito", Snackbar.LENGTH_SHORT).show();

                    // Limpiar el pedido
                    limpiarPedido();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void limpiarPedido() {
        for (Pieza pieza : listaPiezaPedido) {
            pieza.setCantidad(0); // Reiniciar solo la cantidad local
        }
        piezaPedidoAdapter.notifyDataSetChanged();
        textViewMostrarPrecio.setText(String.format(getString(R.string.precioTotal), 0.0)); // Restablecer el precio total
    }

}
