package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.PiezaUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Pieza;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

/**
 * Adaptador para manejar la lista de piezas en un RecyclerView.
 * Este adaptador se encarga de mostrar la información de cada pieza, permitiendo al usuario
 * añadir o eliminar cantidades de las mismas. Además, notifica a un listener cuando hay cambios
 * en la cantidad o en el precio total.
 */
public class PiezaPedidoAdapter extends RecyclerView.Adapter<PiezaPedidoAdapter.PiezaPedidoViewHolder> {

    private List<Pieza> listaPiezaPedido; // Lista de piezas a mostrar
    private Context contexto; // Contexto de la aplicación
    private OnCantidadChangeListener cantidadChangeListener; // Listener para cambios en la cantidad

    /**
     * Interfaz para comunicar cambios en la cantidad de piezas y el precio total.
     */
    public interface OnCantidadChangeListener {
        /**
         * Método llamado cuando cambia la cantidad de una pieza.
         *
         * @param pieza La pieza cuya cantidad ha cambiado.
         * @param cantidad La nueva cantidad de la pieza.
         */
        void onCantidadChanged(Pieza pieza, int cantidad);

        /**
         * Método llamado cuando cambia el precio total de las piezas.
         *
         * @param precioTotal El nuevo precio total.
         */
        void onPrecioTotalChanged(double precioTotal);
    }

    /**
     * Constructor del adaptador.
     *
     * @param listaPiezaPedido Lista de piezas a mostrar.
     * @param contexto Contexto de la aplicación.
     * @param listener Listener para manejar cambios en la cantidad y el precio total.
     */
    public PiezaPedidoAdapter(List<Pieza> listaPiezaPedido, Context contexto,
                              OnCantidadChangeListener listener) {
        this.listaPiezaPedido = listaPiezaPedido;
        this.contexto = contexto;
        this.cantidadChangeListener = listener;
    }

    /**
     * Crea un nuevo ViewHolder inflando el layout de cada item del RecyclerView.
     *
     * @param parent El ViewGroup al que se añadirá la nueva vista.
     * @param viewType El tipo de vista del nuevo ViewHolder.
     * @return Un nuevo PiezaPedidoViewHolder que contiene la vista del item.
     */
    @NonNull
    @Override
    public PiezaPedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout de cada item en el RecyclerView
        View vista = LayoutInflater.from(contexto).inflate(R.layout.lista_pieza_pedido, parent, false);
        return new PiezaPedidoViewHolder(vista);
    }

    /**
     * Vincula los datos de una pieza con las vistas del ViewHolder.
     *
     * @param holder El ViewHolder que contiene las vistas a actualizar.
     * @param position La posición del item en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull PiezaPedidoViewHolder holder, int position) {
        Pieza pieza = listaPiezaPedido.get(position);

        // Configura los datos de cada pieza en el ViewHolder
        holder.textViewNombre.setText(pieza.getNombre());
        holder.textViewPrecio.setText(contexto.getString(R.string.precioPieza, pieza.getPrecio()));

        // Mostrar la cantidad seleccionada
        holder.textViewCantidad.setText(contexto.getString(R.string.cantidadPiezaSeleccionada, pieza.getCantidad()));

        // Calcular y mostrar el precio total de la pieza
        double precioTotal = pieza.getCantidad() * pieza.getPrecio();
        holder.textViewPrecioCantidad.setText(contexto.getString(R.string.precioPiezasAnadido, precioTotal));

        // Establecer la imagen de la pieza usando el método de PiezaUtil
        int iconoPieza = PiezaUtil.obtenerIconoPorNombre(pieza.getNombre());
        holder.imageViewPieza.setImageResource(iconoPieza);

        // Acción del botón "Añadir"
        holder.materialButtonAnadir.setOnClickListener(view -> mostrarDialogoCantidad(pieza, position));
        // Acción del botón "Eliminar"
        holder.materialButtonEliminar.setOnClickListener(view -> eliminarCantidad(pieza, position));
    }

    /**
     * Devuelve el número de items en la lista.
     *
     * @return El número de items en la lista.
     */
    @Override
    public int getItemCount() {
        return listaPiezaPedido.size();
    }

    /**
     * ViewHolder para los items del RecyclerView.
     * Contiene las vistas que representan cada pieza en la lista.
     */
    public static class PiezaPedidoViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNombre, textViewPrecio, textViewCantidad, textViewPrecioCantidad;
        ImageView imageViewPieza;
        MaterialButton materialButtonAnadir, materialButtonEliminar;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista que representa un item en el RecyclerView.
         */
        public PiezaPedidoViewHolder(View itemView) {
            super(itemView);

            // Inicializar las vistas
            textViewNombre = itemView.findViewById(R.id.textViewNombrePieza);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecioPiezaPedido);
            textViewCantidad = itemView.findViewById(R.id.textViewCantidadSeleccionada);
            textViewPrecioCantidad = itemView.findViewById(R.id.textViewPrecioCantidadSeleccionada);
            imageViewPieza = itemView.findViewById(R.id.imageViewIconoPiezaPedido);
            materialButtonAnadir = itemView.findViewById(R.id.buttonAnadirPiezaPedido);
            materialButtonEliminar = itemView.findViewById(R.id.buttonEliminarPiezaPedido);
        }
    }

    /**
     * Muestra un diálogo para que el usuario introduzca la cantidad de una pieza.
     *
     * @param pieza La pieza cuya cantidad se va a modificar.
     * @param position La posición de la pieza en la lista.
     */
    private void mostrarDialogoCantidad(Pieza pieza, int position) {
        MaterialAlertDialogBuilder builderCantidad = new MaterialAlertDialogBuilder(contexto);
        LayoutInflater inflater = LayoutInflater.from(contexto);
        View dialogView = inflater.inflate(R.layout.administrativo_inventario_cantidad_dialog, null);

        // Configurar el layout inflado en el diálogo
        builderCantidad.setView(dialogView);

        TextInputEditText editTextCantidad = dialogView.findViewById(R.id.editTextCantidadPiezaPedido);

        // Establecer la cantidad actual si ya existe
        if (pieza.getCantidad() > 0) {
            editTextCantidad.setText(String.valueOf(pieza.getCantidad()));
        }

        // Obtener el icono correspondiente usando PiezaUtil
        int iconoPieza = PiezaUtil.obtenerIconoPorNombre(pieza.getNombre());

        builderCantidad.setPositiveButton("Aceptar", (dialog, which) -> {
                    String cantidadStr = editTextCantidad.getText().toString().trim();

                    if (cantidadStr.isEmpty()) {
                        Snackbar.make(obtenerContexto(), "Debes introducir una cantidad", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int cantidad = Integer.parseInt(cantidadStr);
                        if (cantidad <= 0) {
                            Snackbar.make(obtenerContexto(), "La cantidad debe ser mayor a 0", Snackbar.LENGTH_SHORT).show();
                        } else {
                            // Actualizar la cantidad de la pieza
                            pieza.setCantidad(cantidad);

                            // Actualizar la cantidad seleccionada en la lista
                            notifyItemChanged(position);
                            Snackbar.make(obtenerContexto(), "Has añadido " + cantidad + " " + pieza.getNombre(),
                                    Snackbar.LENGTH_SHORT).show();

                            // Notificar al fragmento o actividad
                            cantidadChangeListener.onCantidadChanged(pieza, cantidad);

                            // Recalcular el precio total
                            double precioTotal = calcularPrecioTotal();
                            cantidadChangeListener.onPrecioTotalChanged(precioTotal);  // Notificar sobre el nuevo precio total
                        }
                    } catch (NumberFormatException e) {
                        Snackbar.make(obtenerContexto(), "Debes introducir un número válido", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Elimina la cantidad seleccionada de una pieza.
     *
     * @param pieza La pieza cuya cantidad se va a eliminar.
     * @param position La posición de la pieza en la lista.
     */
    private void eliminarCantidad(Pieza pieza, int position) {
        // Reiniciar la cantidad de la pieza
        pieza.setCantidad(0);

        // Notificar al adaptador que el elemento ha cambiado
        notifyItemChanged(position);

        Snackbar.make(obtenerContexto(), "Has eliminado el pedido de " + pieza.getNombre(), Snackbar.LENGTH_SHORT).show();

        // Notificar al Fragment o Actividad sobre el cambio, si es necesario
        cantidadChangeListener.onCantidadChanged(pieza, 0);

        // Recalcular el precio total
        double precioTotal = calcularPrecioTotal();
        cantidadChangeListener.onPrecioTotalChanged(precioTotal);  // Notificar sobre el nuevo precio total
    }

    /**
     * Calcula el precio total de todas las piezas en la lista.
     *
     * @return El precio total de todas las piezas.
     */
    public double calcularPrecioTotal() {
        double precioTotal = 0.0;
        for (Pieza pieza : listaPiezaPedido) {
            precioTotal += pieza.getCantidad() * pieza.getPrecio();
        }
        return precioTotal;
    }

    /**
     * Obtiene el contexto de la actividad actual.
     *
     * @return La vista raíz de la actividad.
     */
    private View obtenerContexto(){
        // Obtener la vista raíz de la actividad
        View rootView = null;
        if (contexto instanceof Activity) {
            Activity activity = (Activity) contexto;
            rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        }
        return rootView;
    }
}