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

public class CocheUtil {
    //Obtener la instancia de la base de datos
    private static FirebaseDatabase database = FirebaseUtil.getFirebaseDatabase();
    //Obtener la referencia del nodo "coches"
    private static DatabaseReference databaseReference = database.getReference("Coches");


    public static void guardarCocheEnFirebase(Coche coche){
        // Obtener una clave única para el coche usando push() para generar un ID
        String idCoche = databaseReference.push().getKey();
        if (idCoche != null) {
            // Guardar el coche con la clave generada
            databaseReference.child(idCoche).setValue(coche)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("CocheUtil", "Coche guardado correctamente en Firebase.");
                        } else {
                            Log.e("CocheUtil", "Error al guardar el coche en Firebase", task.getException());
                        }
                    });
        }
    }

    // Método para obtener datos de un coche por matrícula
    public static void obtenerDatosCoche(String matricula, DatosCocheCallback callback) {
        Query query = databaseReference.orderByChild("matricula").equalTo(matricula);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot cocheSnapshot : snapshot.getChildren()) {
                        Coche coche = cocheSnapshot.getValue(Coche.class);
                        if (coche != null) {
                            callback.onSuccess(coche);
                            return; // Salimos tras encontrar el coche
                        }
                    }
                    // Si no encuentra el coche válido (aunque existe el nodo)
                    callback.onFailure("Coche no encontrado");
                } else {
                    callback.onFailure("Coche no encontrado para la matrícula: " + matricula);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure("Error al cargar datos del coche: " + error.getMessage());
            }
        });
    }

    // Interfaz Callback para manejar los resultados
    public interface DatosCocheCallback {
        void onSuccess(Coche coche);
        void onFailure(String mensajeError);
    }
}




