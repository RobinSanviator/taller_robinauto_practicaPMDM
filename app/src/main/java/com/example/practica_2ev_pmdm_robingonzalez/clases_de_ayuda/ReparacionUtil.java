package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
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

    public static void guardarReparacionEnFirebase(Reparacion reparacion){
        // Obtener una clave única para la reparacion usando push() para generar un ID
        String idReparacion = databaseReference.push().getKey();
        if (idReparacion != null) {
            // Guardar el coche con la clave generada
            databaseReference.child(idReparacion).setValue(reparacion)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ReparacionUtil", "Reparación guardado correctamente en Firebase.");
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

    public static void actualizarFechaFinDiagnostico(Long timestamp, String correoMecanicoJefe, String matriculaCoche) {
        // Realizar la actualización en Firebase
        // Nota: Este es un ejemplo, debes asegurarte de que encuentres la reparación correctamente
        databaseReference.orderByChild("correoMecanicoJefe").equalTo(correoMecanicoJefe)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean encontrado = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reparacion reparacion = snapshot.getValue(Reparacion.class);
                            if (reparacion != null && reparacion.getMatriculaCoche().equals(matriculaCoche)) {
                                // Actualizar la fecha de finalización con el timestamp
                                snapshot.getRef().child("fechaFin").setValue(timestamp)
                                        .addOnSuccessListener(aVoid -> Log.d("FechaGuardada", "Fecha de finalización actualizada correctamente."))
                                        .addOnFailureListener(e -> Log.e("FechaGuardada", "Error al actualizar la fecha de finalización", e));
                                encontrado = true;
                                break;
                            }
                        }

                        if (!encontrado) {
                            Log.e("FechaGuardada", "Reparación no encontrada para este correo y matrícula.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("FechaGuardada", "Error al acceder a la base de datos: " + databaseError.getMessage());
                    }
                });
    }
}


