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
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.PiezaUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Pieza;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;


import java.util.List;

/**
 * Adapter para la lista de piezas en un RecyclerView.
 * Este adaptador gestiona las piezas, mostrando su información y permitiendo la interacción con ellas.
 */
public class PiezaAdapter extends RecyclerView.Adapter<PiezaAdapter.PiezaViewHolder> {

    private List<Pieza> piezas;
    private Context contexto;
    private String correoUsuarioActual;

    /**
     * Constructor del adaptador.
     *
     * @param piezas Lista de piezas a mostrar.
     * @param contexto Contexto de la aplicación.
     * @param correoUsuarioActual Correo del usuario actual para determinar su rol.
     */
    public PiezaAdapter(List<Pieza> piezas, Context contexto, String correoUsuarioActual) {
        this.piezas = piezas;
        this.contexto = contexto;
        this.correoUsuarioActual = correoUsuarioActual;
    }

    @NonNull
    @Override
    public PiezaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para cada item de la lista de piezas.
        View view = LayoutInflater.from(contexto).inflate(R.layout.lista_pieza, parent, false);
        return new PiezaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PiezaViewHolder holder, int position) {
        Pieza pieza = piezas.get(position);

        // Configurar los datos básicos de la pieza.
        holder.textNombre.setText(pieza.getNombre());
        holder.textCantidad.setText(contexto.getString(R.string.cantidadPieza, pieza.getCantidad()));
        holder.imageViewIconoPieza.setImageResource(pieza.getImagenPieza());

        // Mostrar advertencia si el stock es bajo (menos que el umbral mínimo).
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

        // Manejar el click sobre el item para mostrar los detalles de la pieza.
        holder.itemView.setOnClickListener(v -> {
            mostrarDetallesPieza(pieza);
        });
    }

    @Override
    public int getItemCount() {
        return piezas.size(); // Devolver la cantidad de piezas que se van a mostrar.
    }

    /**
     * ViewHolder para los elementos de la lista de piezas.
     */
    public static class PiezaViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textCantidad, advertenciaStock;
        View indicadorStock;
        ImageView imageViewIconoPieza;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView Vista del item en el RecyclerView.
         */
        public PiezaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar las vistas del item.
            textNombre = itemView.findViewById(R.id.textViewNombrePieza);
            textCantidad = itemView.findViewById(R.id.textViewCantidadPieza);
            advertenciaStock = itemView.findViewById(R.id.textViewAdvertenciaStock);
            indicadorStock = itemView.findViewById(R.id.indicadorStock);
            imageViewIconoPieza = itemView.findViewById(R.id.imageViewIconoPieza);
        }
    }

    /**
     * Mostrar los detalles de una pieza en un diálogo.
     *
     * @param pieza La pieza cuya información se mostrará.
     */
    private void mostrarDetallesPieza(Pieza pieza) {
        LayoutInflater inflater = LayoutInflater.from(contexto);
        // Inflar el layout personalizado para el diálogo.
        View vistaDialogo = inflater.inflate(R.layout.administrativo_inventario_pieza_detalle_dialog, null);

        // Obtener las referencias de los elementos en el layout del diálogo.
        ImageView imageView = vistaDialogo.findViewById(R.id.imageViewPieza);
        TextView textViewNombre = vistaDialogo.findViewById(R.id.textViewNombrePieza);
        TextView textViewCantidad = vistaDialogo.findViewById(R.id.textViewCantidadPieza);
        TextView textViewPrecio = vistaDialogo.findViewById(R.id.textViewPrecioPieza);
        TextInputEditText editTextCantidad = vistaDialogo.findViewById(R.id.editTextCantidadSolicitar);
        MaterialButton buttonSolicitar = vistaDialogo.findViewById(R.id.buttonSolicitarPieza);

        // Asignar los valores al layout del diálogo.
        imageView.setImageResource(pieza.getImagenPieza());
        textViewNombre.setText(pieza.getNombre());
        textViewCantidad.setText(contexto.getString(R.string.cantidadPieza, pieza.getCantidad()));
        textViewPrecio.setText(contexto.getString(R.string.precioPieza, pieza.getPrecio()));

        // Verificar la visibilidad de los elementos solo para los mecánicos.
        verificarVisibilidadMecanico(correoUsuarioActual, editTextCantidad, textViewPrecio, buttonSolicitar);

        // Crear y mostrar el diálogo.
        MaterialAlertDialogBuilder builderPieza = new MaterialAlertDialogBuilder(contexto);
        builderPieza.setView(vistaDialogo)
                .setTitle("Detalle pieza")
                .setIcon(R.drawable.ic_piezas)
                .setNegativeButton("Cerrar", (dialog, which) -> dialog.dismiss());

        builderPieza.show();

        // Configurar el comportamiento del botón "Solicitar".
        buttonSolicitar.setOnClickListener(v -> {
            String cantidadStr = editTextCantidad.getText().toString();
            // Verificar si la cantidad ingresada es válida.
            if (!cantidadStr.isEmpty()) {
                int cantidadSolicitada = Integer.parseInt(cantidadStr);

                // Verificar que la cantidad solicitada no sea mayor que el stock.
                if (cantidadSolicitada > pieza.getCantidad()) {
                    Snackbar.make(vistaDialogo, "No hay suficiente stock", Snackbar.LENGTH_SHORT).show();
                } else {
                    // Actualizar la cantidad en la base de datos.
                    PiezaUtil.actualizarCantidadPieza(pieza.getNombre(), cantidadSolicitada);
                    Snackbar.make(vistaDialogo, "Pieza solicitada con éxito", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(vistaDialogo, "Por favor, ingresa una cantidad válida", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Verifica la visibilidad de ciertos elementos del diálogo si el usuario es mecánico.
     *
     * @param correoUsuarioActual Correo del usuario actual.
     * @param editTextCantidad Campo de texto para ingresar la cantidad a solicitar.
     * @param textViewPrecio Etiqueta para mostrar el precio de la pieza.
     * @param buttonSolicitar Botón para realizar la solicitud de la pieza.
     */
    private void verificarVisibilidadMecanico(String correoUsuarioActual, TextInputEditText editTextCantidad,
                                              TextView textViewPrecio, MaterialButton buttonSolicitar) {
        // Verificar si el usuario es mecánico y mostrar/ocultar elementos en consecuencia.
        UsuarioUtil.esMecanicoPorCorreo(correoUsuarioActual, new UsuarioUtil.esMecanicoListener() {
            @Override
            public void onResultado(boolean esMecanico) {
                if (esMecanico) {
                    // Mostrar el campo de cantidad y ocultar el precio.
                    editTextCantidad.setVisibility(View.VISIBLE);
                    textViewPrecio.setVisibility(View.GONE);
                    buttonSolicitar.setVisibility(View.VISIBLE);
                } else {
                    // Ocultar el campo de cantidad y mostrar el precio.
                    editTextCantidad.setVisibility(View.GONE);
                    textViewPrecio.setVisibility(View.VISIBLE);
                    buttonSolicitar.setVisibility(View.GONE);
                }
            }
        });
    }
}





