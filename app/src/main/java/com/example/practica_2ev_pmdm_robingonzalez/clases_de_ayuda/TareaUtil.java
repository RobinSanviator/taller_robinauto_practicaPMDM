package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Tarea;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Clase de utilidad para gestionar las operaciones relacionadas con las tareas en Firebase.
 * Proporciona métodos para guardar, actualizar y cargar tareas, así como para verificar y actualizar
 * el estado de las reparaciones asociadas.
 */
public class TareaUtil {

    // Obtener la instancia de la base de datos de Firebase
    private static FirebaseDatabase database = FirebaseUtil.getFirebaseDatabase();
    // Obtener la referencia del nodo "Tareas" en la base de datos
    private static DatabaseReference databaseReference = database.getReference("Tareas");

    /**
     * Guarda una tarea en Firebase asociada a una reparación específica.
     *
     * @param idReparacion El ID de la reparación a la que pertenece la tarea.
     * @param tarea La tarea que se va a guardar.
     */
    public static void guardarTareaEnFirebase(String idReparacion, Tarea tarea) {
        // Generar un ID único para la tarea usando push()
        String tareaId = databaseReference.push().getKey();

        if (tareaId != null) {
            // Asignar el ID generado y el ID de la reparación a la tarea
            tarea.setIdTarea(tareaId);  // Asigna un ID único a la tarea
            tarea.setIdReparacion(idReparacion);  // Asocia la tarea con la reparación

            // Guardar la tarea en el nodo "Tareas"
            databaseReference.child(tareaId).setValue(tarea)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("TareaUtil", "Tarea guardada exitosamente en Firebase.");
                        } else {
                            Log.e("TareaUtil", "Error al guardar la tarea en Firebase", task.getException());
                        }
                    });
        } else {
            Log.e("TareaUtil", "No se pudo generar un ID para la tarea.");
        }
    }

    /**
     * Actualiza una tarea en Firebase, modificando su estado y comentario.
     *
     * @param tareaId El ID de la tarea que se va a actualizar.
     * @param tarea La tarea con los nuevos datos.
     */
    public static void actualizarTareaEnFirebase(String tareaId, Tarea tarea) {
        // Actualizamos el estado y el comentario de la tarea
        databaseReference.child(tareaId).setValue(tarea)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("TareaUtil", "Tarea actualizada exitosamente en Firebase.");
                    } else {
                        Log.e("TareaUtil", "Error al actualizar la tarea en Firebase", task.getException());
                    }
                });
    }

    /**
     * Carga las tareas asociadas a un mecánico específico, filtradas por su correo.
     *
     * @param correoMecanico El correo del mecánico cuyas tareas se van a cargar.
     * @param listener El listener que maneja los eventos de cambio en los datos.
     */
    public static void cargarTareasPorCorreoMecanico(String correoMecanico, ValueEventListener listener) {
        // Realizar la consulta para obtener las tareas filtradas por el correo del mecánico
        databaseReference.orderByChild("correoMecanicoAsignado").equalTo(correoMecanico)
                .addListenerForSingleValueEvent(listener);
    }

    /**
     * Verifica si todas las tareas asociadas a una reparación están terminadas y, en ese caso,
     * actualiza el estado de la reparación a "Finalizado".
     *
     * @param idReparacion El ID de la reparación cuyas tareas se van a verificar.
     */
    public static void verificarYActualizarEstadoReparacion(String idReparacion) {
        // Referencia a la base de datos de reparaciones
        DatabaseReference reparacionesReference = database.getReference("Reparaciones");

        // Consultar todas las tareas que tienen el mismo idReparacion
        databaseReference.orderByChild("idReparacion").equalTo(idReparacion)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean todasTareasTerminadas = true;

                        // Iterar sobre todas las tareas obtenidas
                        for (DataSnapshot tareaSnapshot : snapshot.getChildren()) {
                            Tarea tarea = tareaSnapshot.getValue(Tarea.class);
                            if (tarea != null && !tarea.getEstado().equals("Terminada")) {
                                todasTareasTerminadas = false;
                                break; // Si alguna tarea no está terminada, no actualizamos la reparación
                            }
                        }

                        // Si todas las tareas están terminadas, actualizar la reparación
                        if (todasTareasTerminadas) {
                            // Actualizar el estado de la reparación a "Finalizado"
                            reparacionesReference.orderByChild("idReparacion").equalTo(idReparacion)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                                                Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                                                if (reparacion != null) {
                                                    // Actualizar el estado de la reparación a "Finalizado"
                                                    reparacion.setEstadoReparacion("Finalizado");
                                                    reparacionesReference.child(reparacionSnapshot.getKey())
                                                            .setValue(reparacion)
                                                            .addOnCompleteListener(task -> {
                                                                if (task.isSuccessful()) {
                                                                    Log.d("TareaUtil", "Estado de la reparación actualizado a 'Finalizado'.");
                                                                } else {
                                                                    Log.e("TareaUtil", "Error al actualizar el estado de la reparación", task.getException());
                                                                }
                                                            });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e("TareaUtil", "Error al cargar la reparación: " + error.getMessage());
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("TareaUtil", "Error al cargar las tareas: " + error.getMessage());
                    }
                });
    }
}

