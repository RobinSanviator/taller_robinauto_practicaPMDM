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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReparacionAdapter extends RecyclerView.Adapter<ReparacionAdapter.ReparacionViewHolder> {

    private List<Reparacion> reparaciones;
    private Context context;
    private OnItemClickListener onItemClickListener;


    // Constructor
    public ReparacionAdapter(List<Reparacion> reparaciones, Context context, OnItemClickListener onItemClickListener) {
        this.reparaciones = reparaciones;
        this.context = context;
        this.onItemClickListener = onItemClickListener;

    }

    // Interfaz para manejar el clic en el item
    public interface OnItemClickListener {
        void onItemClick(Reparacion reparacion);
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


        // Mostrar datos básicos de la reparación
        holder.textViewReparacionNombre.setText("Reparación: " + reparacion.getTipoReparacion());
        holder.textViewMatriculaCoche.setText("Matrícula: " + reparacion.getMatriculaCoche());
        holder.textViewReparacionEstado.setText("Estado: " + reparacion.getEstadoReparacion());


        // Obtener y mostrar el nombre completo del mecánico jefe
        if (reparacion.getCorreoMecanicoJefe() != null) {
            UsuarioUtil.obtenerNombreMecanicoPorCorreo(reparacion.getCorreoMecanicoJefe(), holder.textViewReparacionNombreMecanicoJefe);
        } else {
            holder.textViewReparacionNombreMecanicoJefe.setText("Correo mecánico jefe no proporcionado");
        }


        // Obtener y mostrar el nombre completo del cliente
        if (reparacion.getCorreoCliente() != null) {
            UsuarioUtil.obtenerNombreClientePorCorreo(reparacion.getCorreoCliente(), holder.textViewReparacionNombreCliente);
        } else {
            holder.textViewReparacionNombreCliente.setText("Correo cliente no proporcionado");
        }


        // Configurar el botón "Mostrar más"
        holder.buttonMostrarDetalleReparacion.setOnClickListener(v -> mostrarDetalleReparacion(reparacion));
    }


    @Override
    public int getItemCount() {
        return reparaciones.size();
    }


    // ViewHolder
    public class ReparacionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewReparacionNombre, textViewMatriculaCoche, textViewReparacionNombreCliente,
                textViewReparacionNombreMecanicoJefe, textViewReparacionEstado;
        MaterialButton buttonMostrarDetalleReparacion;

        public ReparacionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReparacionNombre = itemView.findViewById(R.id.textViewReparacionNombre);
            textViewMatriculaCoche = itemView.findViewById(R.id.textViewReparacionMatriculaCoche);
            textViewReparacionNombreCliente = itemView.findViewById(R.id.textViewReparacionNombreCliente);
            textViewReparacionNombreMecanicoJefe = itemView.findViewById(R.id.textViewReparacionNombreMecanicoJefe);
            textViewReparacionEstado = itemView.findViewById(R.id.textViewReparacionEstado);
            buttonMostrarDetalleReparacion = itemView.findViewById(R.id.buttonMostrarDetalleReparacion);

            // Añadir el clic en todo el item (CardView)
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Reparacion reparacion = reparaciones.get(position);
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(reparacion); // Llamamos al onItemClick() del fragmento
                    }
                }
            });
        }
    }


    // Método para mostrar el AlertDialog
    private void mostrarDetalleReparacion(Reparacion reparacion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View vistaDialogo = inflater.inflate(R.layout.reparacion_mostrar_detalle_dialog, null);


        // Configurar los campos del AlertDialog
        TextView dialogMatricula = vistaDialogo.findViewById(R.id.textViewMatriculaDetalleReparacion);
        TextView dialogTipoReparacion = vistaDialogo.findViewById(R.id.textViewTipoReparacionDetalleReparacion);
        TextView dialogEstadoReparacion = vistaDialogo.findViewById(R.id.textViewEstadoDetalleReparacion);
        TextView dialogPresupuesto = vistaDialogo.findViewById(R.id.textViewPresupuestoDetalleReparacion);
        TextView dialogCorreoCliente = vistaDialogo.findViewById(R.id.textViewClienteDetalleReparacion);
        TextView dialogCorreoMecanico = vistaDialogo.findViewById(R.id.textViewMecanicoDetalleReparacion);
        TextView dialogFechaInicio = vistaDialogo.findViewById(R.id.textViewFechaInicioDetalleReparacion);
        TextView dialogFechaFin = vistaDialogo.findViewById(R.id.textViewFechaFinDetalleReparacion);


        dialogMatricula.setText(reparacion.getMatriculaCoche());
        dialogTipoReparacion.setText(reparacion.getTipoReparacion());
        dialogEstadoReparacion.setText(reparacion.getEstadoReparacion());
        dialogPresupuesto.setText(String.valueOf(reparacion.getPresupuesto() + "€"));
        dialogCorreoCliente.setText(reparacion.getCorreoCliente());
        dialogCorreoMecanico.setText(reparacion.getCorreoMecanicoJefe());

        transformarFechaEnObjetoDate(reparacion, dialogFechaInicio, dialogFechaFin);



        builder.setView(vistaDialogo)
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());


        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void transformarFechaEnObjetoDate(Reparacion reparacion,
                                              TextView dialogFechaInicio, TextView dialogFechaFin) {
        // Obtener y formatear la fecha de inicio
        Long timestampInicio = reparacion.getFechaInicio();
        Long timestampFin = reparacion.getFechaFin();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Verificar si hay una fecha de inicio
        if (timestampInicio != null) {
            Date fechaInicio = new Date(timestampInicio); // Convertir a objeto Date
            dialogFechaInicio.setText(sdf.format(fechaInicio)); // Formatear y mostrar la fecha de inicio
        } else {
            dialogFechaInicio.setText("Fecha no disponible");
        }

        // Verificar si hay una fecha de fin
        if (timestampFin != null) {
            Date fechaFin = new Date(timestampFin); // Convertir a objeto Date
            dialogFechaFin.setText(sdf.format(fechaFin)); // Formatear y mostrar la fecha de fin
        } else {
            dialogFechaFin.setText("Fecha no asignada");
        }
    }
}
