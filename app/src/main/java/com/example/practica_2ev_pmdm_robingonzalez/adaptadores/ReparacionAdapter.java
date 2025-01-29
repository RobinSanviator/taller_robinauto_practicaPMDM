package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.CocheUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Coche;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReparacionAdapter extends RecyclerView.Adapter<ReparacionAdapter.ReparacionViewHolder> {

    private List<Reparacion> reparaciones;
    private Context contexto;
    private OnItemClickListener onItemClickListener;


    // Constructor
    public ReparacionAdapter(List<Reparacion> reparaciones, Context contexto, OnItemClickListener onItemClickListener) {
        this.reparaciones = reparaciones;
        this.contexto = contexto;
        this.onItemClickListener = onItemClickListener;

    }

    // Interfaz para manejar el clic en el item
    public interface OnItemClickListener {
        void onItemClick(Reparacion reparacion);
    }

    @NonNull
    @Override
    public ReparacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.lista_reparacion, parent, false);
        return new ReparacionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReparacionViewHolder holder, int position) {
        Reparacion reparacion = reparaciones.get(position);


        // Mostrar datos básicos de la reparación
        holder.textViewReparacionNombre.setText("Reparación: " + reparacion.getTipoReparacion());
        colorearFondoDiagnosticado(reparacion, holder);

        holder.textViewMatriculaCoche.setText("Matrícula: " + reparacion.getMatriculaCoche());
        // Mostrar inicialmente solo la matrícula
        holder.textViewMatriculaCoche.setText("Matrícula: " + reparacion.getMatriculaCoche());

        // Usar el método de CocheUtil para obtener datos adicionales
        CocheUtil.obtenerDatosCoche(reparacion.getMatriculaCoche(), new CocheUtil.DatosCocheCallback() {
            @Override
            public void onSuccess(Coche coche) {
                String datosCoche = "Coche: " + coche.getMarca() + " " + coche.getModelo() + " (" + coche.getMatricula() + ")";
                holder.textViewMatriculaCoche.setText(datosCoche);
            }

            @Override
            public void onFailure(String mensajeError) {
                holder.textViewMatriculaCoche.setText(mensajeError);
            }
        });

        holder.textViewReparacionEstado.setText("Estado: " + reparacion.getEstadoReparacion());
        colorearFondoEstadoReparacion(reparacion, holder);

        //Mostrar textView una vez que el cliente responde a la notificaión
        mostrarSiAceptaPresupuesto(reparacion, holder);

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
                textViewReparacionNombreMecanicoJefe, textViewReparacionEstado, textViewAprobado;
        MaterialButton buttonMostrarDetalleReparacion;

        public ReparacionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReparacionNombre = itemView.findViewById(R.id.textViewReparacionNombre);
            textViewMatriculaCoche = itemView.findViewById(R.id.textViewReparacionMatriculaCoche);
            textViewReparacionNombreCliente = itemView.findViewById(R.id.textViewReparacionNombreCliente);
            textViewReparacionNombreMecanicoJefe = itemView.findViewById(R.id.textViewReparacionNombreMecanicoJefe);
            textViewReparacionEstado = itemView.findViewById(R.id.textViewReparacionEstado);
            textViewAprobado = itemView.findViewById(R.id.textViewReparacionAceptadoPresupuesto);
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

    private void colorearFondoEstadoReparacion(Reparacion reparacion, ReparacionViewHolder holder) {
        TextView textViewReparacionEstado = holder.itemView.findViewById(R.id.textViewReparacionEstado);
        switch (reparacion.getEstadoReparacion()) {
            case "Pendiente":
                textViewReparacionEstado.setBackgroundColor(ContextCompat.getColor(contexto, R.color.color_pendiente));
                break;
            case "En proceso":
                textViewReparacionEstado.setBackgroundColor(ContextCompat.getColor(contexto, R.color.color_enProceso));
                break;
            case "Finalizado":
                textViewReparacionEstado.setBackgroundColor(ContextCompat.getColor(contexto, R.color.color_finalizado));
                break;
            default:
                textViewReparacionEstado.setBackgroundColor(ContextCompat.getColor(contexto, R.color.fondo));
                break;
        }
    }

    private void colorearFondoDiagnosticado(Reparacion reparacion, ReparacionViewHolder holder) {
        TextView textViewReparacionDiagnosticado = holder.itemView.findViewById(R.id.textViewReparacionNombre);

        if(reparacion.getTipoReparacion().equals("Sin diagnosticar")){
            textViewReparacionDiagnosticado.setBackgroundColor(ContextCompat.getColor(contexto, R.color.color_pendiente));
        } else {
            textViewReparacionDiagnosticado.setBackgroundColor(ContextCompat.getColor(contexto, R.color.color_desactivado_fondo));
        }

    }

    private void mostrarSiAceptaPresupuesto(Reparacion reparacion, ReparacionViewHolder holder) {
        TextView textViewPresupuestoAprobado = holder.textViewAprobado;

        if (reparacion.getPresupuestoAprobado().equals(Reparacion.PRESUPUESTO_APROBADO)) {
            textViewPresupuestoAprobado.setVisibility(View.VISIBLE);
            textViewPresupuestoAprobado.setText("Presupuesto aceptado");
            textViewPresupuestoAprobado.setBackgroundColor(
                    ContextCompat.getColor(contexto, R.color.color_finalizado)
            );
            textViewPresupuestoAprobado.setTextColor(
                    ContextCompat.getColor(contexto, R.color.color_texto)
            );
        } else if (reparacion.getPresupuestoAprobado().equals(Reparacion.PRESUPUESTO_NO_APROBADO)) {
            textViewPresupuestoAprobado.setVisibility(View.VISIBLE);
            textViewPresupuestoAprobado.setText("Presupuesto no aceptado");
            textViewPresupuestoAprobado.setBackgroundColor(
                    ContextCompat.getColor(contexto, R.color.color_pendiente)
            );
            textViewPresupuestoAprobado.setTextColor(
                    ContextCompat.getColor(contexto, R.color.color_texto)
            );
        } else {
            textViewPresupuestoAprobado.setVisibility(View.GONE);
        }
    }



    // Método para mostrar el AlertDialog
    private void mostrarDetalleReparacion(Reparacion reparacion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        LayoutInflater inflater = LayoutInflater.from(contexto);
        View vistaDialogo = inflater.inflate(R.layout.reparacion_mostrar_detalle_dialog, null);


        // Configurar los campos del AlertDialog
        TextView dialogCoche = vistaDialogo.findViewById(R.id.textViewCocheDetalleReparacion);
        TextView dialogTipoReparacion = vistaDialogo.findViewById(R.id.textViewTipoReparacionDetalleReparacion);
        TextView dialogEstadoReparacion = vistaDialogo.findViewById(R.id.textViewEstadoDetalleReparacion);
        TextView dialogPresupuesto = vistaDialogo.findViewById(R.id.textViewPresupuestoDetalleReparacion);
        TextView dialogCorreoCliente = vistaDialogo.findViewById(R.id.textViewClienteDetalleReparacion);
        TextView dialogCorreoMecanico = vistaDialogo.findViewById(R.id.textViewMecanicoDetalleReparacion);
        TextView dialogFechaInicio = vistaDialogo.findViewById(R.id.textViewFechaInicioDetalleReparacion);
        TextView dialogFechaFin = vistaDialogo.findViewById(R.id.textViewFechaFinDetalleReparacion);
        TextView dialogoAprobado = vistaDialogo.findViewById(R.id.textViewAceptadoPresupuesto);


        // Usar el método para obtener los datos del coche
        CocheUtil.obtenerDatosCoche(reparacion.getMatriculaCoche(), new CocheUtil.DatosCocheCallback() {
            @Override
            public void onSuccess(Coche coche) {
                String datosCoche = " " +coche.getMarca() + " " + coche.getModelo() + " (" + coche.getMatricula() + ")";
                dialogCoche.setText(datosCoche);
            }

            @Override
            public void onFailure(String mensajeError) {
                dialogCoche.setText(mensajeError);
            }
        });

        dialogTipoReparacion.setText(reparacion.getTipoReparacion());
        dialogEstadoReparacion.setText(reparacion.getEstadoReparacion());
        dialogPresupuesto.setText(String.valueOf(reparacion.getPresupuesto() + "€"));
        dialogCorreoCliente.setText(reparacion.getCorreoCliente());
        dialogCorreoMecanico.setText(reparacion.getCorreoMecanicoJefe());
        dialogoAprobado.setText(reparacion.getPresupuestoAprobado());


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
