package com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.TareaAdaptador;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.TareaUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Tarea;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MecanicoTareasFragment extends Fragment {

    private ImageView imageViewVolver;
    private MecanicoActivity mecanicoActivity;
    private RecyclerView recyclerViewTareas;
    private TareaAdaptador tareaAdaptador;
    private List<Tarea> listaTareas;
    private TextView textViewNoHayTareas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout tareas del mecánico
        View vista = inflater.inflate(R.layout.mecanico_tareas_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeTareas();
        configurarRecyclerView();
        cargarTareas();


        return vista;
    }


    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalTareasMecanico);
        recyclerViewTareas = vista.findViewById(R.id.recyclerViewTareas);
        textViewNoHayTareas = vista.findViewById(R.id.textViewNoHayTareas);
    }

    private void obtenerHelper() {
        if (getActivity() instanceof MecanicoActivity) {
            mecanicoActivity = (MecanicoActivity) getActivity();
        } else {
            Log.e("MecanicoTareasFragment", "Error al obtener helper");
        }
    }

    private void volverMenuPrincipalDesdeTareas() {
        imageViewVolver.setOnClickListener(v -> mecanicoActivity.volverMenuPrincipal());
    }


    private void configurarRecyclerView() {
        listaTareas = new ArrayList<>();
        tareaAdaptador = new TareaAdaptador(listaTareas, getContext(), null);
        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTareas.setAdapter(tareaAdaptador);
    }

    private void cargarTareas() {
        // Asegúrate de que el correo del mecánico está disponible
        if (mecanicoActivity == null) {
            Log.e("MecanicoTareasFragment", "mecanicoActivity es nulo");
            return;
        }

        // Obtener el correo del mecánico desde la actividad
        String correoMecanico = mecanicoActivity.getCorreo();

        // Crear el ValueEventListener para cargar las tareas
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaTareas.clear(); // Limpiar la lista antes de agregar nuevas tareas

                // Iterar sobre todas las tareas en el nodo global "Tareas"
                for (DataSnapshot tareaSnapshot : snapshot.getChildren()) {
                    Tarea tarea = tareaSnapshot.getValue(Tarea.class);
                    // Verificar si la tarea tiene un mecánico asignado y si es el mecánico actual
                    if (tarea != null && correoMecanico.equals(tarea.getCorreoMecanicoAsignado())) {
                        listaTareas.add(tarea); // Solo agregar tareas asignadas al mecánico actual
                    }
                }

                // Notificar al adaptador que los datos han cambiado
                tareaAdaptador.notifyDataSetChanged();

                // Mostrar o ocultar el TextView dependiendo de si hay tareas o no
                if (listaTareas.isEmpty()) {
                    // Si no hay tareas, mostrar el TextView
                    textViewNoHayTareas.setVisibility(View.VISIBLE);
                } else {
                    // Si hay tareas, ocultar el TextView
                    textViewNoHayTareas.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MecanicoTareasFragment", "Error al cargar tareas: " + error.getMessage());
            }
        };

        // Llamar al método para cargar las tareas desde Firebase
        TareaUtil.cargarTareasPorCorreoMecanico(correoMecanico, listener);
        // Notificar al adaptador que los datos han cambiado
        tareaAdaptador.notifyDataSetChanged();

    }



    // Método para verificar si todas las tareas de una reparación están en "Terminado"
    private void verificarEstadoDeReparacion(String idReparacion) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance().getReference("Reparaciones").child(idReparacion).child("Tareas");

        tareasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean todasTerminado = true;

                // Recorremos todas las tareas de la reparación
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Tarea tarea = snapshot.getValue(Tarea.class);
                    if (tarea != null && !"Terminado".equals(tarea.getEstado())) {
                        todasTerminado = false; // Si alguna tarea no está terminada, lo marcamos como false
                        break; // Si encontramos una tarea pendiente, no continuamos revisando
                    }
                }

                if (todasTerminado) {
                    // Si todas las tareas están en "Terminado", cambiamos el estado de la reparación
                    actualizarEstadoReparacion(idReparacion);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("EstadoReparacion", "Error al verificar el estado de las tareas", databaseError.toException());
            }
        });
    }

    // Método para actualizar el estado de la reparación a "Finalizado"
    private void actualizarEstadoReparacion(String idReparacion) {
        DatabaseReference reparacionRef = FirebaseDatabase.getInstance().getReference("Reparaciones").child(idReparacion);

        reparacionRef.child("estadoReparacion").setValue("Finalizado")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i("EstadoReparacion", "Estado de la reparación actualizado a Finalizado");
                    } else {
                        Log.e("EstadoReparacion", "Error al actualizar el estado de la reparación");
                    }
                });
    }
}
