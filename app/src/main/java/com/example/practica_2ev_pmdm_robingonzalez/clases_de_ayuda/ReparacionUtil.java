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

public class ReparacionUtil {
    //Obtener la instancia de la base de datos
    private static FirebaseDatabase database = FirebaseUtil.getFirebaseDatabase();
    //Obtener la referencia del nodo "Reparaciones"
    private static DatabaseReference databaseReference = database.getReference("Reparaciones");

    public static void cargarReparaciones(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }

    public static void cargarReparacionesPorCorreoCliente(String correoCliente, ValueEventListener listener) {
        // Realizar la consulta filtrada por correoCliente
        databaseReference.orderByChild("correoCliente").equalTo(correoCliente)
                .addListenerForSingleValueEvent(listener);
    }

    public static void guardarReparacionEnFirebase(Reparacion reparacion) {
        // Obtener una clave única para la reparación usando push() para generar un ID
        String idReparacion = databaseReference.push().getKey();

        if (idReparacion != null) {
            // Asignar el ID generado al objeto reparacion
            reparacion.setIdReparacion(idReparacion);

            // Guardar la reparación con el ID generado en Firebase
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

    // Método para actualizar el tipo de reparación en Firebase
    public static void actualizarTipoReparacion(String correoMecanicoJefe, String matriculaCoche, String nuevoDiagnostico) {
        // Obtener la referencia a las reparaciones
        DatabaseReference reparacionesRef = databaseReference;

        // Realizar una consulta para encontrar la reparación basada en el correo del mecánico jefe
        reparacionesRef.orderByChild("correoMecanicoJefe").equalTo(correoMecanicoJefe)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean encontrado = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reparacion reparacion = snapshot.getValue(Reparacion.class);
                            if (reparacion != null && reparacion.getMatriculaCoche().equals(matriculaCoche)) {
                                // Actualizar el tipo de reparación con el nuevo diagnóstico
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

    public static void actualizarPresupuesto(String correoMecanicoJefe, String matriculaCoche, double nuevoPresupuesto) {
        // Obtener la referencia a las reparaciones
        DatabaseReference reparacionesRef = databaseReference;

        // Realizar una consulta para encontrar la reparación basada en el correo del mecánico jefe
        reparacionesRef.orderByChild("correoMecanicoJefe").equalTo(correoMecanicoJefe)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean encontrado = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reparacion reparacion = snapshot.getValue(Reparacion.class);
                            if (reparacion != null && reparacion.getMatriculaCoche().equals(matriculaCoche)) {
                                // Actualizar el presupuesto con el nuevo valor
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


    public static void actualizarEstadoReparacion(String correoCliente, String respuesta) {
        // Obtener la referencia a las reparaciones
        DatabaseReference reparacionesRef = databaseReference;

        // Realizar una consulta para encontrar la reparación basada en el correo del cliente
        reparacionesRef.orderByChild("correoCliente").equalTo(correoCliente)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean encontrado = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reparacion reparacion = snapshot.getValue(Reparacion.class);
                            if (reparacion != null) {
                                // Actualizar el estado de la reparación según la respuesta
                                String nuevoEstado = "Acepta el presupuesto".equals(respuesta) ? "En proceso" : "Finalizado";
                                snapshot.getRef().child("estadoReparacion").setValue(nuevoEstado)
                                        .addOnSuccessListener(aVoid -> Log.d("ReparacionUtil", "Estado de reparación actualizado a " + nuevoEstado))
                                        .addOnFailureListener(e -> Log.e("ReparacionUtil", "Error al actualizar el estado de la reparación", e));

                                // Si el cliente no acepta el presupuesto, actualizar la fecha de finalización
                                if ("No acepta el presupuesto".equals(respuesta)) {
                                    long fechaActual = System.currentTimeMillis(); // Fecha y hora actuales
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

    public static void actualizarPresupuestoAprobado(String correoCliente, String estadoPresupuesto) {
        // Obtener la referencia a las reparaciones
        DatabaseReference reparacionesRef = databaseReference;

        // Realizar una consulta para encontrar la reparación basada en el correo del cliente
        reparacionesRef.orderByChild("correoCliente").equalTo(correoCliente)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean encontrado = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reparacion reparacion = snapshot.getValue(Reparacion.class);
                            if (reparacion != null) {
                                // Actualizar el campo presupuestoAprobado
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

    // Método para guardar la tarea en el nodo "Tareas"
    public static void guardarTareaEnFirebase(String idReparacion, Tarea tarea) {
        // Obtener la referencia al nodo "Tareas" en la base de datos de Firebase
        DatabaseReference tareasRef = FirebaseDatabase.getInstance().getReference("Tareas");

        // Generar un ID único para la tarea usando push(). Esto crea una clave única que sirve como identificador
        String tareaId = tareasRef.push().getKey();

        if (tareaId != null) {
            // Asignar el ID generado y el ID de la reparación a la tarea
            tarea.setIdTarea(tareaId);  // Asegura que cada tarea tiene un ID único
            tarea.setIdReparacion(idReparacion);  // Asocia esta tarea con la reparación específica

            // Guardar la tarea en el nodo "Tareas"
            tareasRef.child(tareaId).setValue(tarea)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ReparacionUtil", "Tarea guardada exitosamente.");
                        } else {
                            Log.e("ReparacionUtil", "Error al guardar la tarea.", task.getException());
                        }
                    });
        } else {
            // Si no se pudo generar un ID único para la tarea
            Log.e("ReparacionUtil", "No se pudo generar un ID para la tarea.");
        }
    }
}

