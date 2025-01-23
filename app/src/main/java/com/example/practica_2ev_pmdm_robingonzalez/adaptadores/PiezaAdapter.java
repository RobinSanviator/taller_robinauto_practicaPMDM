package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Pieza;

import java.util.List;

public class PiezaAdapter extends RecyclerView.Adapter<PiezaAdapter.PiezaViewHolder> {

    private List<Pieza> piezas;
    private Context context;

    public PiezaAdapter(List<Pieza> piezas, Context contexto) {
        this.piezas = piezas;
    }

    @NonNull
    @Override
    public PiezaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_pieza, parent, false);
        context = parent.getContext();
        return new PiezaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PiezaViewHolder holder, int position) {
        Pieza pieza = piezas.get(position);

        // Configurar datos básicos
        holder.textNombre.setText(pieza.getNombre());
        holder.textCantidad.setText(context.getString(R.string.cantidadPieza, pieza.getCantidad()));

        // Mostrar advertencia si el stock es bajo
        if (pieza.getCantidad() < pieza.getUmbralMinimo()) {
            holder.advertenciaStock.setVisibility(View.VISIBLE);
            holder.advertenciaStock.setText(context.getString(
                    R.string.pocasPiezasEnStock,
                    pieza.getUmbralMinimo()
            ));
            holder.indicadorStock.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.color_error)
            );
        } else {
            holder.advertenciaStock.setVisibility(View.GONE);
            holder.indicadorStock.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.color_principal)
            );
        }
    }

    @Override
    public int getItemCount() {
        return piezas.size();
    }

    public static class PiezaViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textCantidad, advertenciaStock;
        View indicadorStock;

        public PiezaViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textViewNombrePieza);
            textCantidad = itemView.findViewById(R.id.textViewCantidadPieza);
            advertenciaStock = itemView.findViewById(R.id.textViewAdvertenciaStock);
            indicadorStock = itemView.findViewById(R.id.indicadorStock);
        }
    }
}
