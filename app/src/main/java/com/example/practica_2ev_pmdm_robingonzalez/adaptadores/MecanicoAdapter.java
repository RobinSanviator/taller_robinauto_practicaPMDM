package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.ReparacionUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Tarea;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MecanicoAdapter extends RecyclerView.Adapter<MecanicoAdapter.MecanicoViewHolder> {

    private List<Usuario> listaMecanicos;
    private Context contexto;
    private String idReparacion;

    public MecanicoAdapter(List<Usuario> listaMecanicos, Context contexto, String idReparacion) {
        this.listaMecanicos = listaMecanicos;
        this.contexto = contexto;
        this.idReparacion = idReparacion;
    }

    @NonNull
    @Override
    public MecanicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.lista_mecanico_tarea, parent, false);
        return new MecanicoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MecanicoViewHolder holder, int position) {
        Usuario mecanico = listaMecanicos.get(position);

        holder.textViewNombreApellidoMecanico.setText(mecanico.getNombre() + " " + mecanico.getApellidos());
        holder.textViewCorreoMecanico.setText(mecanico.getCorreo());

        // Establecer el click en el item
        holder.itemView.setOnClickListener(v -> {
            abrirDialogoDefinirTarea(mecanico);
        });
    }

    @Override
    public int getItemCount() {
        return listaMecanicos.size();
    }

    public static class MecanicoViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNombreApellidoMecanico;
        TextView textViewCorreoMecanico;

        public MecanicoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreApellidoMecanico = itemView.findViewById(R.id.tvNombreApellidoMecanico);
            textViewCorreoMecanico = itemView.findViewById(R.id.tvCorreoMecanico);
        }
    }

    private void abrirDialogoDefinirTarea(Usuario mecanico) {
        // Crear el diálogo
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(contexto);
        View view = LayoutInflater.from(contexto).inflate(R.layout.mecanico_jefe_tarea_dialogo, null);

        // Vincular los elementos del layout del diálogo
        TextView textViewCorreoMecanicoTarea = view.findViewById(R.id.textViewCorreoMecanicoTarea);
        AutoCompleteTextView autoCompleteDefinirTareaMecanicoJefe = view.findViewById(R.id.autoCompleteDefinirTareaMecanicoJefe);


        textViewCorreoMecanicoTarea.setText("Correo del mecánico: " + mecanico.getCorreo());
        configurarAdaptadorAutoCompletadoTareas(autoCompleteDefinirTareaMecanicoJefe);

        // Configurar el botón de acción para guardar
        builder.setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    // Obtener la descripción de la tarea del EditText
                    String descripcionTarea = autoCompleteDefinirTareaMecanicoJefe.getText().toString();

                    // Crear el objeto tarea
                    Tarea tarea = new Tarea(descripcionTarea, "Pendiente", mecanico.getCorreo(), "");

                    // Guardar la tarea en Firebase usando el idReparacion
                    ReparacionUtil.guardarTareaEnFirebase(idReparacion, tarea);

                    Snackbar.make(obtenerContexto(), "Tarea asignada correctamente al mecánico " +mecanico.getNombre(), Snackbar.LENGTH_LONG).show();
                })
                .setNegativeButton("Cancelar", (dialog, which) ->{ dialog.dismiss();
                    Snackbar.make(obtenerContexto(), "Cancelada la tarea", Snackbar.LENGTH_LONG).show();
                }).show();
    }

    private void configurarAdaptadorAutoCompletadoTareas(AutoCompleteTextView  autoCompleteDefinirTareaMecanicoJefe){
        // Configurar el adaptador para el AutoCompleteTextView
        String[] tareasArray = contexto.getResources().getStringArray(R.array.tareas_mecanicos);
        ArrayAdapter<String> tareasAdapter = new ArrayAdapter<>(contexto, android.R.layout.simple_dropdown_item_1line, tareasArray);
        autoCompleteDefinirTareaMecanicoJefe.setAdapter(tareasAdapter);
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
