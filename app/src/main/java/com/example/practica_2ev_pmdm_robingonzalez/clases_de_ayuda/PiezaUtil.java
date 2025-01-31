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

/**
 * Clase de utilidad para gestionar las operaciones relacionadas con las piezas en Firebase.
 * Proporciona métodos para cargar piezas, actualizar cantidades, obtener precios y iconos asociados a las piezas.
 */
public class PiezaUtil {

    // Obtener la instancia de la base de datos de Firebase
    private static FirebaseDatabase database = FirebaseUtil.getFirebaseDatabase();
    // Obtener la referencia del nodo "Piezas" en la base de datos
    private static DatabaseReference databaseReference = database.getReference("Piezas");

    /**
     * Carga todas las piezas desde Firebase y notifica los cambios a través de un ValueEventListener.
     *
     * @param listener El listener que maneja los eventos de cambio en los datos.
     */
    public static void cargarPiezas(ValueEventListener listener) {
        databaseReference.addValueEventListener(listener);
    }

    /**
     * Carga las piezas disponibles (con cantidad mayor a 0) desde Firebase.
     *
     * @param listener El listener que maneja los eventos de cambio en los datos.
     */
    public static void cargarPiezasDisponibles(ValueEventListener listener) {
        // Realizar la consulta para obtener las piezas con cantidad mayor a 0
        databaseReference.orderByChild("cantidad")
                .startAt(1) // Solo las piezas con cantidad mayor a 0
                .addListenerForSingleValueEvent(listener);
    }

    /**
     * Actualiza la cantidad de una pieza en Firebase restando la cantidad solicitada.
     *
     * @param nombrePieza El nombre de la pieza a actualizar.
     * @param cantidadSolicitada La cantidad a restar de la pieza.
     */
    public static void actualizarCantidadPieza(String nombrePieza, int cantidadSolicitada) {
        // Realizar una consulta para buscar la pieza por nombre
        Query query = databaseReference.orderByChild("nombre").equalTo(nombrePieza);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot piezaSnapshot : snapshot.getChildren()) {
                        Integer cantidadActual = piezaSnapshot.child("cantidad").getValue(Integer.class);
                        if (cantidadActual != null) {
                            // Restar la cantidad solicitada de la cantidad actual
                            int nuevaCantidad = cantidadActual - cantidadSolicitada;
                            if (nuevaCantidad >= 0) {
                                // Actualizar la cantidad de la pieza en la base de datos
                                piezaSnapshot.getRef().child("cantidad").setValue(nuevaCantidad)
                                        .addOnSuccessListener(aVoid -> Log.d("PiezaUtil", "Cantidad actualizada correctamente."))
                                        .addOnFailureListener(e -> Log.e("PiezaUtil", "Error al actualizar cantidad: " + e.getMessage()));
                            } else {
                                Log.e("PiezaUtil", "La cantidad solicitada excede la cantidad disponible.");
                            }
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

    /**
     * Suma una cantidad específica a la cantidad actual de una pieza en Firebase.
     *
     * @param nombrePieza El nombre de la pieza a actualizar.
     * @param cantidadSeleccionada La cantidad a sumar a la pieza.
     */
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

    /**
     * Obtiene el precio de una pieza según su nombre.
     *
     * @param nombrePieza El nombre de la pieza.
     * @return El precio de la pieza. Si no se encuentra, devuelve un precio genérico (10.0).
     */
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

    /**
     * Obtiene el ID del icono correspondiente a una pieza según su nombre.
     *
     * @param nombrePieza El nombre de la pieza.
     * @return El ID del recurso de icono. Si no se encuentra, devuelve un icono genérico (R.drawable.pieza).
     */
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
                return R.drawable.pieza; // Icono genérico para piezas no especificadas
        }
    }
}
