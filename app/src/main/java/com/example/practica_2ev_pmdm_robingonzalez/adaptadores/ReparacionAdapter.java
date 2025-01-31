package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.content.Context;

import android.util.Log;
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
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Coche;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Notificacion;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adaptador para mostrar una lista de reparaciones en un RecyclerView.
 * Este adaptador maneja la vinculación de los datos de las reparaciones a las vistas de la interfaz de usuario.
 *
 */
public class ReparacionAdapter extends RecyclerView.Adapter<ReparacionAdapter.ReparacionViewHolder> {

    private List<Reparacion> reparaciones;  // Lista de reparaciones que se van a mostrar
    private Context contexto;  // Contexto de la actividad o fragmento
    private OnItemClickListener onItemClickListener;  // Listener para manejar clics en los ítems

    /**
     * Constructor para inicializar el adaptador con los datos y el listener de clics.
     *
     * @param reparaciones        lista de reparaciones
     * @param contexto            contexto de la aplicación
     * @param onItemClickListener listener para los clics en los ítems
     */
    public ReparacionAdapter(List<Reparacion> reparaciones, Context contexto, OnItemClickListener onItemClickListener) {
        this.reparaciones = reparaciones;
        this.contexto = contexto;
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Interfaz para manejar el clic en un ítem de la lista.
     */
    public interface OnItemClickListener {
        void onItemClick(Reparacion reparacion);  // Método que se ejecuta cuando se hace clic en un ítem
    }

    @NonNull
    @Override
    public ReparacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos la vista del ítem de la lista (cada elemento de la RecyclerView)
        View view = LayoutInflater.from(contexto).inflate(R.layout.lista_reparacion, parent, false);
        return new ReparacionViewHolder(view);  // Retornamos un nuevo ViewHolder con la vista inflada
    }

    @Override
    public void onBindViewHolder(@NonNull ReparacionViewHolder holder, int position) {
        Reparacion reparacion = reparaciones.get(position);  // Obtenemos la reparación correspondiente a la posición

        // Mostrar los datos de la reparación en las vistas
        holder.textViewReparacionNombre.setText("Reparación: " + reparacion.getTipoReparacion());
        colorearFondoDiagnosticado(reparacion, holder);  // Coloreamos el fondo según el estado de la reparación

        // Mostrar la matrícula del coche (inicialmente solo la matrícula)
        holder.textViewMatriculaCoche.setText("Matrícula: " + reparacion.getMatriculaCoche());

        // Llamamos a un método que obtiene más detalles sobre el coche usando la matrícula
        CocheUtil.obtenerDatosCoche(reparacion.getMatriculaCoche(), new CocheUtil.DatosCocheCallback() {
            @Override
            public void onSuccess(Coche coche) {
                // Si la consulta es exitosa, mostramos los datos del coche completos
                String datosCoche = "Coche: " + coche.getMarca() + " " + coche.getModelo() + " (" + coche.getMatricula() + ")";
                holder.textViewMatriculaCoche.setText(datosCoche);
            }

            @Override
            public void onFailure(String mensajeError) {
                // Si la consulta falla, mostramos un mensaje de error
                holder.textViewMatriculaCoche.setText(mensajeError);
            }
        });

        // Mostramos el estado de la reparación
        holder.textViewReparacionEstado.setText("Estado: " + reparacion.getEstadoReparacion());
        colorearFondoEstadoReparacion(reparacion, holder);  // Coloreamos el fondo según el estado de la reparación

        // Mostrar si el cliente ha aceptado el presupuesto
        mostrarSiAceptaPresupuesto(reparacion, holder);

        // Obtener el nombre del mecánico jefe y mostrarlo en el UI
        if (reparacion.getCorreoMecanicoJefe() != null) {
            UsuarioUtil.obtenerNombreMecanicoPorCorreo(reparacion.getCorreoMecanicoJefe(), holder.textViewReparacionNombreMecanicoJefe);
        } else {
            holder.textViewReparacionNombreMecanicoJefe.setText("Correo mecánico jefe no proporcionado");
        }

        // Obtener el nombre del cliente y mostrarlo en el UI
        if (reparacion.getCorreoCliente() != null) {
            UsuarioUtil.obtenerNombreClientePorCorreo(reparacion.getCorreoCliente(), holder.textViewReparacionNombreCliente);
        } else {
            holder.textViewReparacionNombreCliente.setText("Correo cliente no proporcionado");
        }

        // Configurar el botón "Mostrar más" para ver detalles de la reparación
        holder.buttonMostrarDetalleReparacion.setOnClickListener(v -> mostrarDetalleReparacion(reparacion));
    }

    @Override
    public int getItemCount() {
        return reparaciones.size();  // Retorna el tamaño de la lista de reparaciones
    }

    /**
     * ViewHolder que contiene las vistas para cada ítem de la lista.
     * Este es el encargado de asociar los datos con las vistas.
     */
    public class ReparacionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewReparacionNombre, textViewMatriculaCoche, textViewReparacionNombreCliente,
                textViewReparacionNombreMecanicoJefe, textViewReparacionEstado, textViewAprobado;
        MaterialButton buttonMostrarDetalleReparacion;

        public ReparacionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializamos las vistas dentro del ítem
            textViewReparacionNombre = itemView.findViewById(R.id.textViewReparacionNombre);
            textViewMatriculaCoche = itemView.findViewById(R.id.textViewReparacionMatriculaCoche);
            textViewReparacionNombreCliente = itemView.findViewById(R.id.textViewReparacionNombreCliente);
            textViewReparacionNombreMecanicoJefe = itemView.findViewById(R.id.textViewReparacionNombreMecanicoJefe);
            textViewReparacionEstado = itemView.findViewById(R.id.textViewReparacionEstado);
            textViewAprobado = itemView.findViewById(R.id.textViewReparacionAceptadoPresupuesto);
            buttonMostrarDetalleReparacion = itemView.findViewById(R.id.buttonMostrarDetalleReparacion);

            // Añadimos el clic en todo el item (CardView)
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Reparacion reparacion = reparaciones.get(position);
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(reparacion);  // Llamamos al método onItemClick del listener
                    }
                }
            });
        }
    }


    /**
     * Cambia el color de fondo de un TextView según el estado de la reparación.
     * Este método se utiliza para resaltar visualmente el estado de una reparación
     * (Pendiente, En proceso, Finalizado) con colores específicos.
     *
     * @param reparacion La reparación cuyo estado se va a representar.
     * @param holder     El ViewHolder que contiene las vistas a actualizar.
     */
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

    /**
     * Cambia el color de fondo de un TextView dependiendo de si la reparación ha sido diagnosticada.
     * Si la reparación no ha sido diagnosticada, se resalta con un color específico.
     *
     * @param reparacion La reparación cuyo estado de diagnóstico se va a representar.
     * @param holder     El ViewHolder que contiene las vistas a actualizar.
     */
    private void colorearFondoDiagnosticado(Reparacion reparacion, ReparacionViewHolder holder) {
        TextView textViewReparacionDiagnosticado = holder.itemView.findViewById(R.id.textViewReparacionNombre);

        if (reparacion.getTipoReparacion().equals("Sin diagnosticar")) {
            textViewReparacionDiagnosticado.setBackgroundColor(ContextCompat.getColor(contexto, R.color.color_pendiente));
        } else {
            textViewReparacionDiagnosticado.setBackgroundColor(ContextCompat.getColor(contexto, R.color.color_desactivado_fondo));
        }
    }

    /**
     * Muestra un TextView indicando si el presupuesto de la reparación ha sido aprobado o no.
     * Dependiendo del estado del presupuesto, se muestra un mensaje y se aplica un color de fondo.
     *
     * @param reparacion La reparación cuyo estado de presupuesto se va a representar.
     * @param holder     El ViewHolder que contiene las vistas a actualizar.
     */
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
            textViewPresupuestoAprobado.setVisibility(View.GONE); // Ocultar si no hay información
        }
    }

    /**
     * Muestra un diálogo con los detalles de una reparación.
     * Este diálogo incluye información como el coche asociado, el tipo de reparación,
     * el estado, el presupuesto, los correos del cliente y del mecánico, y la fecha de inicio.
     *
     * @param reparacion La reparación cuyos detalles se van a mostrar.
     */
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
        TextView dialogoAprobado = vistaDialogo.findViewById(R.id.textViewAceptadoPresupuesto);

        // Obtener los datos del coche asociado a la reparación
        CocheUtil.obtenerDatosCoche(reparacion.getMatriculaCoche(), new CocheUtil.DatosCocheCallback() {
            @Override
            public void onSuccess(Coche coche) {
                String datosCoche = " " + coche.getMarca() + " " + coche.getModelo() + " (" + coche.getMatricula() + ")";
                dialogCoche.setText(datosCoche);
            }

            @Override
            public void onFailure(String mensajeError) {
                dialogCoche.setText(mensajeError); // Mostrar mensaje de error si falla la obtención de datos
            }
        });

        // Establecer los datos de la reparación en los campos del diálogo
        dialogTipoReparacion.setText(reparacion.getTipoReparacion());
        dialogEstadoReparacion.setText(reparacion.getEstadoReparacion());
        dialogPresupuesto.setText(String.valueOf(reparacion.getPresupuesto() + "€"));
        dialogCorreoCliente.setText(reparacion.getCorreoCliente());
        dialogCorreoMecanico.setText(reparacion.getCorreoMecanicoJefe());
        dialogoAprobado.setText(reparacion.getPresupuestoAprobado());

        // Formatear y mostrar la fecha de inicio
        transformarFechaEnObjetoDate(reparacion, dialogFechaInicio);

        // Configurar el diálogo y mostrarlo
        builder.setView(vistaDialogo)
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Convierte un timestamp en una fecha legible y la muestra en un TextView.
     * Si no hay fecha disponible, se muestra un mensaje predeterminado.
     *
     * @param reparacion        La reparación que contiene el timestamp de la fecha de inicio.
     * @param dialogFechaInicio El TextView donde se mostrará la fecha formateada.
     */
    private void transformarFechaEnObjetoDate(Reparacion reparacion, TextView dialogFechaInicio) {
        // Obtener y formatear la fecha de inicio
        Long timestampInicio = reparacion.getFechaInicio();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Verificar si hay una fecha de inicio
        if (timestampInicio != null) {
            Date fechaInicio = new Date(timestampInicio); // Convertir a objeto Date
            dialogFechaInicio.setText(sdf.format(fechaInicio)); // Formatear y mostrar la fecha de inicio
        } else {
            dialogFechaInicio.setText("Fecha no disponible"); // Mostrar mensaje si no hay fecha
        }
    }
}