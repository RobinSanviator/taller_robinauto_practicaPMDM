package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.PiezaUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Pieza;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class PiezaPedidoAdapter extends RecyclerView.Adapter<PiezaPedidoAdapter.PiezaPedidoViewHolder> {

    private List<Pieza> listaPiezaPedido;
    private Context contexto;
    private OnCantidadChangeListener cantidadChangeListener;

    // Interfaz para comunicar cambios al Fragment
    public interface OnCantidadChangeListener {
        void onCantidadChanged(Pieza pieza, int cantidad);
    }

    public PiezaPedidoAdapter(List<Pieza> listaPiezaPedido, Context contexto, OnCantidadChangeListener listener) {
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

        // Lógica para mostrar el texto recomendado si es necesario
        if (pieza.getCantidad() < pieza.getUmbralMinimo()) {
            holder.textViewRecomendado.setVisibility(View.VISIBLE);
        } else {
            holder.textViewRecomendado.setVisibility(View.GONE);
        }

        // Establecer la imagen de la pieza usando el método de PiezaUtil
        int iconoPieza = PiezaUtil.obtenerIconoPorNombre(pieza.getNombre());
        holder.imageViewPieza.setImageResource(iconoPieza);

      //  holder.materialButtonAnadir.setOnClickListener(view -> mostrarDialogoCantidad(pieza));

    }

    @Override
    public int getItemCount() {
        return listaPiezaPedido.size();
    }

    // ViewHolder para los items del RecyclerView
    public static class PiezaPedidoViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNombre, textViewPrecio, textViewRecomendado;
        ImageView imageViewPieza;
        MaterialButton materialButtonAnadir;

        public PiezaPedidoViewHolder(View itemView) {
            super(itemView);

            textViewNombre = itemView.findViewById(R.id.textViewNombrePieza);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecioPiezaPedido);
            textViewRecomendado = itemView.findViewById(R.id.textViewRecomendado);
            imageViewPieza = itemView.findViewById(R.id.imageViewIconoPiezaPedido);
            materialButtonAnadir = itemView.findViewById(R.id.buttonAnadirPiezaPedido);
        }
    }

    /*private void mostrarDialogoCantidad(Pieza pieza) {
        MaterialAlertDialogBuilder builderCantidad = new MaterialAlertDialogBuilder(contexto);
        LayoutInflater inflater = LayoutInflater.from(contexto);
       View dialogView = inflater.inflate(R.layout.dialogo_cantidad_pedido, null);

        EditText editTextCantidad = dialogView.findViewById(R.id.editTextCantidad);
        TextView textViewError = dialogView.findViewById(R.id.textViewError);

        builderCantidad.setView(dialogView)
                .setTitle("Seleccionar cantidad")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String cantidadStr = editTextCantidad.getText().toString();
                    if (!cantidadStr.isEmpty()) {
                        int cantidad = Integer.parseInt(cantidadStr);
                        if (cantidad > 0) {
                            cantidadChangeListener.onCantidadChanged(pieza, cantidad);
                        } else {
                            textViewError.setText("La cantidad debe ser mayor a 0");
                        }
                    }
                })
                .setNegativeButton("Cancelar", null).show();

    }*/
}
