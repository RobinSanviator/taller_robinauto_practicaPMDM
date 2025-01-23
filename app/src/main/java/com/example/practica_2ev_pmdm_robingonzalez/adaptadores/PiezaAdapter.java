package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Pieza;
import com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo.AdministrativoActivity;
import com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo.AdministrativoInventarioPedidoFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class PiezaAdapter extends RecyclerView.Adapter<PiezaAdapter.PiezaViewHolder> {

    private List<Pieza> piezas;
    private Context contexto;

    public PiezaAdapter(List<Pieza> piezas, Context contexto) {
        this.piezas = piezas;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public PiezaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.lista_pieza, parent, false);
        return new PiezaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PiezaViewHolder holder, int position) {
        Pieza pieza = piezas.get(position);

        // Configurar datos básicos
        holder.textNombre.setText(pieza.getNombre());
        holder.textCantidad.setText(contexto.getString(R.string.cantidadPieza, pieza.getCantidad()));
        // Asignar el icono de la pieza desde el atributo imagenPieza
        holder.imageViewIconoPieza.setImageResource(pieza.getImagenPieza());

        // Mostrar advertencia si el stock es bajo
        if (pieza.getCantidad() < pieza.getUmbralMinimo()) {
            holder.advertenciaStock.setVisibility(View.VISIBLE);
            holder.advertenciaStock.setText(contexto.getString(
                    R.string.pocasPiezasEnStock,
                    pieza.getUmbralMinimo()
            ));
            holder.indicadorStock.setBackgroundColor(
                    ContextCompat.getColor(contexto, R.color.color_error)
            );
        } else {
            holder.advertenciaStock.setVisibility(View.GONE);
            holder.indicadorStock.setBackgroundColor(
                    ContextCompat.getColor(contexto, R.color.color_principal)
            );
        }
        // Manejar el click sobre el item
        holder.itemView.setOnClickListener(v -> {
            // Mostrar un dialog con los detalles de la pieza
            mostrarDetallesPieza(pieza);
        });

    }

    @Override
    public int getItemCount() {
        return piezas.size();
    }

    public static class PiezaViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textCantidad, advertenciaStock;
        View indicadorStock;
        ImageView imageViewIconoPieza;

        public PiezaViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textViewNombrePieza);
            textCantidad = itemView.findViewById(R.id.textViewCantidadPieza);
            advertenciaStock = itemView.findViewById(R.id.textViewAdvertenciaStock);
            indicadorStock = itemView.findViewById(R.id.indicadorStock);
            imageViewIconoPieza = itemView.findViewById(R.id.imageViewIconoPieza);
        }
    }

    private void mostrarDetallesPieza(Pieza pieza) {
        // Crear el LayoutInflater para inflar el diseño personalizado
        LayoutInflater inflater = LayoutInflater.from(contexto);
        View vistaDialogo = inflater.inflate(R.layout.administrativo_inventario_pieza_detalle_dialog, null);

        // Obtener las referencias de los elementos del diseño
        ImageView imageView = vistaDialogo.findViewById(R.id.imageViewPieza);
        TextView textViewNombre = vistaDialogo.findViewById(R.id.textViewNombrePieza);
        TextView textViewCantidad = vistaDialogo.findViewById(R.id.textViewCantidadPieza);
        TextView textViewPrecio = vistaDialogo.findViewById(R.id.textViewPrecioPieza);

        // Asignar los valores al layout del Dialog
        imageView.setImageResource(pieza.getImagenPieza());
        textViewNombre.setText(pieza.getNombre());
        textViewCantidad.setText(contexto.getString(R.string.cantidadPieza, pieza.getCantidad()));
<<<<<<< HEAD
        
=======
        textViewPrecio.setText(contexto.getString(R.string.precioPieza, pieza.getPrecio()));
>>>>>>> d51f64ac82e504f89b05b59a16540a53208ff611

        // Crear el Dialog
        MaterialAlertDialogBuilder builderPieza = new MaterialAlertDialogBuilder(contexto);
        builderPieza.setView(vistaDialogo)
                .setTitle("Detalle pieza")
                .setIcon(R.drawable.ic_piezas)
                .setNegativeButton("Cerrar", (dialog, which) -> dialog.dismiss());

        // Mostrar el dialog
        builderPieza.show();

    }
<<<<<<< HEAD

}
=======
}
>>>>>>> d51f64ac82e504f89b05b59a16540a53208ff611
