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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento que permite gestionar un pedido de piezas al proveedor.
 * Los usuarios pueden seleccionar las piezas, modificar cantidades y confirmar el pedido.
 */
public class AdministrativoInventarioPedidoFragment extends Fragment implements PiezaPedidoAdapter.OnCantidadChangeListener {

    private RecyclerView recyclerViewPiezaPedido;
    private List<Pieza> listaPiezaPedido;
    private PiezaPedidoAdapter piezaPedidoAdapter;
    private TextView textViewMostrarPrecio;
    private MaterialButton materialButtonConfirmarPedido;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflar el layout del fragmento y configurar los componentes del pedido.
     *
     * @param inflater El inflador para convertir el layout XML en un objeto de vista.
     * @param container El contenedor donde el fragmento será insertado.
     * @param savedInstanceState El estado guardado del fragmento, si lo hubiera.
     * @return La vista inflada con el contenido del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño para el pedido al proveedor
        View vista = inflater.inflate(R.layout.administrativo_inventario_pedido_fragment, container, false);

        // Inicializar componentes
        inicializarComponentes(vista);

        // Obtener el helper (si es necesario)
        obtenerHelper();

        // Configurar el RecyclerView con el adaptador
        configurarRecyclerView();

        // Cargar las piezas desde Firebase
        cargarPiezas();

        // Actualizar el precio total cada vez que se cambien las cantidades
        actualizarPrecioTotal();

        // Configurar el botón de confirmar pedido
        configurarBotonConfirmar();

        return vista;
    }

    /**
     * Inicializa los componentes del layout asociados con este fragmento.
     *
     * @param vista La vista inflada del fragmento.
     */
    private void inicializarComponentes(View vista) {
        recyclerViewPiezaPedido = vista.findViewById(R.id.recyclerViewPedidoProveedor);
        textViewMostrarPrecio = vista.findViewById(R.id.textViewMostrarPrecioTotalPedido);
        materialButtonConfirmarPedido = vista.findViewById(R.id.buttonConfirmarPedido);
    }

    /**
     * Obtiene los objetos Helper necesarios para la funcionalidad del fragmento.
     */
    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            // Si es necesario, obtener el helper de la actividad
        } else {
            Log.e("AdministrativoInventarioPedidoFragment", "Error al obtener helper");
        }
    }

    /**
     * Configura el RecyclerView con un adaptador y un layout manager.
     */
    private void configurarRecyclerView() {
        listaPiezaPedido = new ArrayList<>();
        piezaPedidoAdapter = new PiezaPedidoAdapter(listaPiezaPedido, getContext(), this);
        recyclerViewPiezaPedido.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPiezaPedido.setAdapter(piezaPedidoAdapter);
    }

    /**
     * Este método se llama cuando se actualiza la cantidad de una pieza.
     * Recalcula el precio total.
     *
     * @param pieza La pieza cuya cantidad ha cambiado.
     * @param cantidad La nueva cantidad seleccionada.
     */
    @Override
    public void onCantidadChanged(Pieza pieza, int cantidad) {
        actualizarPrecioTotal(); // Recalcular el total
    }

    /**
     * Este método se llama cuando se actualiza el precio total.
     *
     * @param precioTotal El nuevo precio total calculado.
     */
    @Override
    public void onPrecioTotalChanged(double precioTotal) {
        // Actualizar el precio total en la vista
        textViewMostrarPrecio.setText(String.format(getString(R.string.precioTotal), precioTotal));
    }

    /**
     * Carga las piezas disponibles desde Firebase y las agrega a la lista.
     * También reinicia las cantidades locales de cada pieza.
     */
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

    /**
     * Actualiza el precio total de las piezas seleccionadas y lo muestra en el TextView.
     */
    private void actualizarPrecioTotal() {
        double precioTotal = 0.0;
        for (Pieza pieza : listaPiezaPedido) {
            precioTotal += pieza.getCantidad() * pieza.getPrecio();
        }
        // Actualizar el precio total en el TextView
        textViewMostrarPrecio.setText(String.format(getString(R.string.precioTotal), precioTotal));
    }

    /**
     * Configura el botón de confirmar pedido, mostrando un diálogo para confirmar la acción.
     */
    private void configurarBotonConfirmar() {
        materialButtonConfirmarPedido.setOnClickListener(v -> confirmarPedido());
    }

    /**
     * Muestra un diálogo de confirmación para realizar el pedido.
     * Si el usuario confirma, actualiza las cantidades de las piezas y las guarda en Firebase.
     * Después, limpia el pedido y muestra un mensaje de éxito.
     */
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

    /**
     * Limpia el pedido actual, restableciendo las cantidades locales de las piezas y el precio total.
     */
    private void limpiarPedido() {
        for (Pieza pieza : listaPiezaPedido) {
            pieza.setCantidad(0); // Reiniciar solo la cantidad local
        }
        piezaPedidoAdapter.notifyDataSetChanged();
        textViewMostrarPrecio.setText(String.format(getString(R.string.precioTotal), 0.0)); // Restablecer el precio total
    }
}
