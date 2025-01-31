package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.util.Log;


import androidx.annotation.NonNull;

import com.example.practica_2ev_pmdm_robingonzalez.modelo.Coche;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;

/**
 * Clase utilitaria para gestionar la interacción con Firebase en relación a los coches.
 * Contiene métodos para guardar coches y obtener datos de coches a partir de la matrícula.
 */
public class CocheUtil {
    // Obtener la instancia de la base de datos Firebase
    private static FirebaseDatabase database = FirebaseUtil.getFirebaseDatabase();
    // Obtener la referencia al nodo "Coches" en la base de datos Firebase
    private static DatabaseReference databaseReference = database.getReference("Coches");

    /**
     * Guarda un coche en Firebase bajo un nodo generado con un ID único.
     *
     * @param coche El objeto coche que se va a guardar en Firebase.
     */
    public static void guardarCocheEnFirebase(Coche coche) {
        // Obtener una clave única para el coche usando push() para generar un ID
        String idCoche = databaseReference.push().getKey();
        if (idCoche != null) {
            // Si se obtiene una clave, se guarda el coche con dicha clave
            databaseReference.child(idCoche).setValue(coche)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Si el coche se guarda correctamente, se logea un mensaje de éxito
                            Log.d("CocheUtil", "Coche guardado correctamente en Firebase.");
                        } else {
                            // Si hay un error, se logea un mensaje de error con detalles
                            Log.e("CocheUtil", "Error al guardar el coche en Firebase", task.getException());
                        }
                    });
        }
    }

    /**
     * Obtiene los datos de un coche de Firebase utilizando su matrícula.
     *
     * @param matricula La matrícula del coche que se busca en Firebase.
     * @param callback El callback que maneja el resultado de la búsqueda (éxito o error).
     */
    public static void obtenerDatosCoche(String matricula, DatosCocheCallback callback) {
        // Realiza una consulta a Firebase para buscar el coche por su matrícula
        Query query = databaseReference.orderByChild("matricula").equalTo(matricula);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Si el snapshot tiene datos, procesamos los resultados
                if (snapshot.exists()) {
                    for (DataSnapshot cocheSnapshot : snapshot.getChildren()) {
                        Coche coche = cocheSnapshot.getValue(Coche.class);
                        if (coche != null) {
                            // Si encontramos un coche, llamamos al callback de éxito
                            callback.onSuccess(coche);
                            return; // Salimos tras encontrar el coche
                        }
                    }
                    // Si no se encuentra un coche válido, llamamos al callback de fallo
                    callback.onFailure("Coche no encontrado");
                } else {
                    // Si no se encuentra ningún coche con esa matrícula, se llama al callback de fallo
                    callback.onFailure("Coche no encontrado para la matrícula: " + matricula);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Si la consulta falla, se llama al callback de fallo
                callback.onFailure("Error al cargar datos del coche: " + error.getMessage());
            }
        });
    }

    /**
     * Interfaz que define los métodos de éxito y fallo para manejar los resultados de la búsqueda de coches.
     */
    public interface DatosCocheCallback {
        /**
         * Método que se llama cuando se encuentra un coche exitosamente.
         *
         * @param coche El coche encontrado.
         */
        void onSuccess(Coche coche);

        /**
         * Método que se llama cuando ocurre un error o no se encuentra el coche.
         *
         * @param mensajeError El mensaje de error detallado.
         */
        void onFailure(String mensajeError);
    }
}




