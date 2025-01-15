package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ReparacionAdapter extends RecyclerView.Adapter<ReparacionAdapter.ReparacionViewHolder> {

    private List<Reparacion> reparaciones;
    private Context context;

    // Constructor
    public ReparacionAdapter(List<Reparacion> reparaciones, Context context) {
        this.reparaciones = reparaciones;
        this.context = context;
    }

    @NonNull
    @Override
    public ReparacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_reparacion, parent, false);
        return new ReparacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReparacionViewHolder holder, int position) {
        Reparacion reparacion = reparaciones.get(position);

        holder.textViewReparacionNombre.setText("Reparación: " + reparacion.getTipoReparacion());
        holder.textViewReparacionEstado.setText("Estado: " + reparacion.getEstadoReparacion());

        // Obtener y mostrar el nombre completo del cliente usando el correo
        if (reparacion.getCorreoCliente() != null) {
            // Llamar al método de obtención de nombre completo para el cliente
            UsuarioUtil.obtenerNombreCompletoPorCorreo(reparacion.getCorreoCliente(), holder.textViewReparacionNombreCliente);
        } else {
            holder.textViewReparacionNombreCliente.setText("Correo cliente no proporcionado");
        }

        // Obtener y mostrar el nombre completo del mecánico jefe usando el correo
        if (reparacion.getCorreoMecanicoJefe() != null) {
            // Llamar al método de obtención de nombre completo para el mecánico jefe
            UsuarioUtil.obtenerNombreCompletoPorCorreo(reparacion.getCorreoMecanicoJefe(), holder.textViewReparacionNombreMecanicoJefe);
        } else {
            holder.textViewReparacionNombreMecanicoJefe.setText("Correo mecánico jefe no proporcionado");
        }

        // Configurar el botón "Mostrar más"
        holder.buttonMostrarDetalleReparación.setOnClickListener(v -> mostrarDetalleReparacion(reparacion));
    }

    @Override
    public int getItemCount() {
        return reparaciones.size();
    }

    // ViewHolder
    static class ReparacionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewReparacionNombre, textViewReparacionNombreCliente, textViewReparacionNombreMecanicoJefe, textViewReparacionEstado;
        MaterialButton buttonMostrarDetalleReparación;

        public ReparacionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReparacionNombre = itemView.findViewById(R.id.textViewReparacionNombre);
            textViewReparacionNombreCliente = itemView.findViewById(R.id.textViewReparacionNombreCliente);
            textViewReparacionNombreMecanicoJefe = itemView.findViewById(R.id.textViewReparacionNombreMecanicoJefe);
            textViewReparacionEstado = itemView.findViewById(R.id.textViewReparacionEstado);
            buttonMostrarDetalleReparación = itemView.findViewById(R.id.buttonMostrarDetalleReparación);
        }
    }

    // Método para mostrar el AlertDialog
    private void mostrarDetalleReparacion(Reparacion reparacion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.reparacion_mostrar_detalle_dialog, null);

        // Configurar los campos del AlertDialog
        TextView dialogMatricula = dialogView.findViewById(R.id.textViewMatriculaDetalleReparacion);
        TextView dialogTipoReparacion = dialogView.findViewById(R.id.textViewTipoReparacionDetalleReparacion);
        TextView dialogEstadoReparacion = dialogView.findViewById(R.id.textViewEstadoDetalleReparacion);
        TextView dialogPresupuesto = dialogView.findViewById(R.id.textViewPresupuestoDetalleReparacion);
        TextView dialogCorreoCliente = dialogView.findViewById(R.id.textViewClienteDetalleReparacion);
        TextView dialogCorreoMecanico = dialogView.findViewById(R.id.textViewMecanicoDetalleReparacion);

        dialogMatricula.setText(reparacion.getMatriculaCoche());
        dialogTipoReparacion.setText(reparacion.getTipoReparacion());
        dialogEstadoReparacion.setText(reparacion.getEstadoReparacion());
        dialogPresupuesto.setText(String.valueOf(reparacion.getPresupuesto()));
        dialogCorreoCliente.setText(reparacion.getCorreoCliente());
        dialogCorreoMecanico.setText(reparacion.getCorreoMecanicoJefe());

        builder.setView(dialogView)
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
