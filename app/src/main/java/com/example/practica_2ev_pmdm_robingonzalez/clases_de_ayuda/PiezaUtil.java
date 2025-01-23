package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
}
