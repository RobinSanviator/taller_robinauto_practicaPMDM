package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.util.Log;



import com.example.practica_2ev_pmdm_robingonzalez.modelo.Coche;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


}


