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

public class PiezaPedidoAdapter extends RecyclerView.Adapter<PiezaPedidoAdapter.PiezaPedidoViewHolder> {

    private List<Pieza> listaPiezaPedido;
    private Context contexto;
    private OnCantidadChangeListener cantidadChangeListener;


    // Interfaz para comunicar cambios al Fragment
    public interface OnCantidadChangeListener {
        void onCantidadChanged(Pieza pieza, int cantidad);
        void onPrecioTotalChanged(double precioTotal);
    }


    public PiezaPedidoAdapter(List<Pieza> listaPiezaPedido, Context contexto,
                              OnCantidadChangeListener listener) {
        this.listaPiezaPedido = listaPiezaPedido;
        this.contexto = contexto;
        this.cantidadChangeListener = listener;
    }

    @NonNull
    @Override
    public PiezaPedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout de cada item en el RecyclerView
        View vista = LayoutInflater.from(contexto).inflate(R.layout.lista_pieza_pedido, parent, false);
        return new PiezaPedidoViewHolder(vista);
    }


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

    @Override
    public int getItemCount() {
        return listaPiezaPedido.size();
    }

    // ViewHolder para los items del RecyclerView
    public static class PiezaPedidoViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNombre, textViewPrecio, textViewCantidad, textViewPrecioCantidad;
        ImageView imageViewPieza;
        MaterialButton materialButtonAnadir, materialButtonEliminar;

        public PiezaPedidoViewHolder(View itemView) {
            super(itemView);

            textViewNombre = itemView.findViewById(R.id.textViewNombrePieza);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecioPiezaPedido);
            textViewCantidad = itemView.findViewById(R.id.textViewCantidadSeleccionada);
            textViewPrecioCantidad = itemView.findViewById(R.id.textViewPrecioCantidadSeleccionada);
            imageViewPieza = itemView.findViewById(R.id.imageViewIconoPiezaPedido);
            materialButtonAnadir = itemView.findViewById(R.id.buttonAnadirPiezaPedido);
            materialButtonEliminar = itemView.findViewById(R.id.buttonEliminarPiezaPedido);
        }
    }

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

    public double calcularPrecioTotal() {
        double precioTotal = 0.0;
        for (Pieza pieza : listaPiezaPedido) {
            precioTotal += pieza.getCantidad() * pieza.getPrecio();
        }
        return precioTotal;
    }

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
