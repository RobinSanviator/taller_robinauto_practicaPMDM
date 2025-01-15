package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.util.Log;

import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
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



}
