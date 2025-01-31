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
 * Clase utilitaria para manejar las operaciones relacionadas con las reparaciones en Firebase.
 * Esta clase permite cargar reparaciones, guardar reparaciones, y actualizar datos como el diagnóstico,
 * el presupuesto, el estado de la reparación, y las tareas asociadas a las reparaciones.
 */
public class ReparacionUtil {
    private static FirebaseDatabase database = FirebaseUtil.getFirebaseDatabase();
    private static DatabaseReference databaseReference = database.getReference("Reparaciones");

    /**
     * Carga todas las reparaciones desde Firebase y ejecuta el listener pasado.
     *
     * @param listener El listener que manejará los datos obtenidos desde Firebase.
     */
    public static void cargarReparaciones(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }

    /**
     * Carga las reparaciones filtradas por el correo del cliente desde Firebase
     * y ejecuta el listener pasado.
     *
     * @param correoCliente El correo del cliente para filtrar las reparaciones.
     * @param listener El listener que manejará los datos obtenidos desde Firebase.
     */
    public static void cargarReparacionesPorCorreoCliente(String correoCliente, ValueEventListener listener) {
        databaseReference.orderByChild("correoCliente").equalTo(correoCliente)
                .addListenerForSingleValueEvent(listener);
    }

    /**
     * Guarda una reparación en Firebase.
     *
     * @param reparacion El objeto de la reparación a guardar en Firebase.
     */
    public static void guardarReparacionEnFirebase(Reparacion reparacion) {
        String idReparacion = databaseReference.push().getKey();

        if (idReparacion != null) {
            reparacion.setIdReparacion(idReparacion);

            databaseReference.child(idReparacion).setValue(reparacion)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ReparacionUtil", "Reparación guardada correctamente en Firebase.");
                        } else {
                            Log.e("ReparacionUtil", "Error al guardar la reparación en Firebase", task.getException());
                        }
                    });
        }
    }

    /**
     * Actualiza el tipo de reparación (diagnóstico) en Firebase para una reparación específica.
     *
     * @param correoMecanicoJefe El correo del mecánico jefe que está asociada con la reparación.
     * @param matriculaCoche La matrícula del coche para identificar la reparación.
     * @param nuevoDiagnostico El nuevo diagnóstico a establecer en la reparación.
     */
    public static void actualizarTipoReparacion(String correoMecanicoJefe, String matriculaCoche, String nuevoDiagnostico) {
        DatabaseReference reparacionesRef = databaseReference;

        reparacionesRef.orderByChild("correoMecanicoJefe").equalTo(correoMecanicoJefe)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean encontrado = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reparacion reparacion = snapshot.getValue(Reparacion.class);
                            if (reparacion != null && reparacion.getMatriculaCoche().equals(matriculaCoche)) {
                                snapshot.getRef().child("tipoReparacion").setValue(nuevoDiagnostico)
                                        .addOnSuccessListener(aVoid -> Log.d("ReparacionUtil", "Diagnóstico actualizado correctamente."))
                                        .addOnFailureListener(e -> Log.e("ReparacionUtil", "Error al actualizar diagnóstico", e));
                                encontrado = true;
                                break;
                            }
                        }

                        if (!encontrado) {
                            Log.d("ReparacionUtil", "Reparación no encontrada para el mecánico jefe y matrícula.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("ReparacionUtil", "Error al consultar reparaciones", databaseError.toException());
                    }
                });
    }

    /**
     * Actualiza el presupuesto en Firebase para una reparación específica.
     *
     * @param correoMecanicoJefe El correo del mecánico jefe que está asociada con la reparación.
     * @param matriculaCoche La matrícula del coche para identificar la reparación.
     * @param nuevoPresupuesto El nuevo presupuesto a establecer en la reparación.
     */
    public static void actualizarPresupuesto(String correoMecanicoJefe, String matriculaCoche, double nuevoPresupuesto) {
        DatabaseReference reparacionesRef = databaseReference;

        reparacionesRef.orderByChild("correoMecanicoJefe").equalTo(correoMecanicoJefe)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean encontrado = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reparacion reparacion = snapshot.getValue(Reparacion.class);
                            if (reparacion != null && reparacion.getMatriculaCoche().equals(matriculaCoche)) {
                                snapshot.getRef().child("presupuesto").setValue(nuevoPresupuesto)
                                        .addOnSuccessListener(aVoid -> Log.d("ReparacionUtil", "Presupuesto actualizado correctamente."))
                                        .addOnFailureListener(e -> Log.e("ReparacionUtil", "Error al actualizar presupuesto", e));
                                encontrado = true;
                                break;
                            }
                        }

                        if (!encontrado) {
                            Log.d("ReparacionUtil", "Reparación no encontrada para el mecánico jefe y matrícula.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("ReparacionUtil", "Error al consultar reparaciones", databaseError.toException());
                    }
                });
    }

    /**
     * Actualiza el estado de la reparación en Firebase en función de la respuesta del cliente.
     *
     * @param correoCliente El correo del cliente asociado con la reparación.
     * @param respuesta La respuesta del cliente (aceptación o rechazo del presupuesto).
     */
    public static void actualizarEstadoReparacion(String correoCliente, String respuesta) {
        DatabaseReference reparacionesRef = databaseReference;

        reparacionesRef.orderByChild("correoCliente").equalTo(correoCliente)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean encontrado = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reparacion reparacion = snapshot.getValue(Reparacion.class);
                            if (reparacion != null) {
                                String nuevoEstado = "Acepta el presupuesto".equals(respuesta) ? "En proceso" : "Finalizado";
                                snapshot.getRef().child("estadoReparacion").setValue(nuevoEstado)
                                        .addOnSuccessListener(aVoid -> Log.d("ReparacionUtil", "Estado de reparación actualizado a " + nuevoEstado))
                                        .addOnFailureListener(e -> Log.e("ReparacionUtil", "Error al actualizar el estado de la reparación", e));

                                if ("No acepta el presupuesto".equals(respuesta)) {
                                    long fechaActual = System.currentTimeMillis();
                                    snapshot.getRef().child("fechaFin").setValue(fechaActual)
                                            .addOnSuccessListener(aVoid -> Log.d("ReparacionUtil", "Fecha de finalización actualizada correctamente"))
                                            .addOnFailureListener(e -> Log.e("ReparacionUtil", "Error al actualizar la fecha de finalización", e));
                                }

                                encontrado = true;
                                break;
                            }
                        }

                        if (!encontrado) {
                            Log.d("ReparacionUtil", "Reparación no encontrada para el cliente.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("ReparacionUtil", "Error al consultar reparaciones", databaseError.toException());
                    }
                });
    }

    /**
     * Actualiza el estado del presupuesto aprobado para una reparación.
     *
     * @param correoCliente El correo del cliente asociado con la reparación.
     * @param estadoPresupuesto El estado del presupuesto aprobado (aprobado o no).
     */
    public static void actualizarPresupuestoAprobado(String correoCliente, String estadoPresupuesto) {
        DatabaseReference reparacionesRef = databaseReference;

        reparacionesRef.orderByChild("correoCliente").equalTo(correoCliente)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean encontrado = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reparacion reparacion = snapshot.getValue(Reparacion.class);
                            if (reparacion != null) {
                                snapshot.getRef().child("presupuestoAprobado").setValue(estadoPresupuesto)
                                        .addOnSuccessListener(aVoid -> Log.d("ReparacionUtil", "Presupuesto aprobado actualizado correctamente."))
                                        .addOnFailureListener(e -> Log.e("ReparacionUtil", "Error al actualizar presupuesto aprobado", e));
                                encontrado = true;
                                break;
                            }
                        }

                        if (!encontrado) {
                            Log.d("ReparacionUtil", "Reparación no encontrada para el cliente.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("ReparacionUtil", "Error al consultar reparaciones", databaseError.toException());
                    }
                });
    }

    /**
     * Guarda una tarea en el nodo "Tareas" de Firebase.
     *
     * @param idReparacion El ID de la reparación asociada a la tarea.
     * @param tarea El objeto de la tarea a guardar en Firebase.
     */
    public static void guardarTareaEnFirebase(String idReparacion, Tarea tarea) {
        DatabaseReference tareasRef = FirebaseDatabase.getInstance().getReference("Tareas");

        String tareaId = tareasRef.push().getKey();

        if (tareaId != null) {
            tarea.setIdTarea(tareaId);
            tarea.setIdReparacion(idReparacion);

            tareasRef.child(tareaId).setValue(tarea)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ReparacionUtil", "Tarea guardada exitosamente.");
                        } else {
                            Log.e("ReparacionUtil", "Error al guardar la tarea.", task.getException());
                        }
                    });
        } else {
            Log.e("ReparacionUtil", "No se pudo generar un ID para la tarea.");
        }
    }
}

