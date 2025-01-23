package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PiezaUtil {
    // Obtener la instancia de la base de datos
    private static FirebaseDatabase database = FirebaseUtil.getFirebaseDatabase();
    // Obtener la referencia del nodo "Piezas"
    private static DatabaseReference databaseReference = database.getReference("Piezas");

    // Método para cargar las piezas desde Firebase
    public static void cargarPiezas(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }

    // Nuevo método para actualizar la cantidad de una pieza
    public static void actualizarCantidadPieza(String nombrePieza, int nuevaCantidad) {
        databaseReference.orderByChild("nombre").equalTo(nombrePieza).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot piezaSnapshot : snapshot.getChildren()) {
                    piezaSnapshot.getRef().child("cantidad").setValue(nuevaCantidad); // Actualizar a la nueva cantidad
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PiezaUtil", "Error al actualizar cantidad de la pieza: " + error.getMessage());
            }
        });
    }

    public static void sumarCantidadPieza(String nombrePieza, int cantidadSeleccionada) {
        // Realizar una consulta para buscar la pieza por nombre
        Query query = databaseReference.orderByChild("nombre").equalTo(nombrePieza);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot piezaSnapshot : snapshot.getChildren()) {
                        Integer cantidadActual = piezaSnapshot.child("cantidad").getValue(Integer.class);
                        if (cantidadActual != null) {
                            int nuevaCantidad = cantidadActual + cantidadSeleccionada;
                            piezaSnapshot.getRef().child("cantidad").setValue(nuevaCantidad)
                                    .addOnSuccessListener(aVoid -> Log.d("PiezaUtil", "Cantidad actualizada correctamente."))
                                    .addOnFailureListener(e -> Log.e("PiezaUtil", "Error al actualizar cantidad: " + e.getMessage()));
                        } else {
                            Log.e("PiezaUtil", "Cantidad actual es nula.");
                        }
                    }
                } else {
                    Log.e("PiezaUtil", "No se encontró la pieza con el nombre especificado.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PiezaUtil", "Error en la consulta: " + error.getMessage());
            }
        });
    }

    public static double obtenerPrecioPorNombre(String nombrePieza) {
        switch (nombrePieza) {
            case "Pastillas de freno":
                return 40.0;
            case "Líquido de frenos":
                return 12.0;
            case "Filtro de aceite":
                return 8.0;
            case "Aceite de motor":
                return 45.0;
            case "Batería":
                return 120.0;
            case "Pesas de equilibrado":
                return 3.0;
            case "Fusibles":
                return 1.5;
            case "Relés":
                return 10.0;
            case "Amortiguadores":
                return 60.0;
            case "Filtro de aire":
                return 15.0;
            case "Silenciador":
                return 75.0;
            case "Aceite de transmisión":
                return 50.0;
            case "Gas refrigerante":
                return 25.0;
            default:
                return 10.0; // Precio genérico para piezas no especificadas
        }
    }

    // Método que retorna el ID del icono correspondiente según el nombre de la pieza
    public static int obtenerIconoPorNombre(String nombrePieza) {
        switch (nombrePieza) {
            case "Pastillas de freno":
                return R.drawable.ic_pastilla_frenos;
            case "Líquido de frenos":
                return R.drawable.ic_liquido_frenos;
            case "Filtro de aceite":
                return R.drawable.ic_filtro_aceite;
            case "Aceite de motor":
                return R.drawable.ic_aceite_motor;
            case "Batería":
                return R.drawable.ic_bateria;
            case "Pesas de equilibrado":
                return R.drawable.ic_pesas_equilibrado;
            case "Fusibles":
                return R.drawable.ic_fusibles;
            case "Relés":
                return R.drawable.ic_reles;
            case "Amortiguadores":
                return R.drawable.ic_amortiguadores;
            case "Filtro de aire":
                return R.drawable.ic_filtro_aire;
            case "Silenciador":
                return R.drawable.ic_silenciador;
            case "Aceite de transmisión":
                return R.drawable.ic_aceite_transmision;
            case "Gas refrigerante":
                return R.drawable.ic_gas_refrigerante;
            default:
                return R.drawable.pieza;
        }
    }
}
