package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Map;

public class FirebaseUtil {
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://gestortaller-b9308-default-rtdb.europe-west1.firebasedatabase.app");
    private static DatabaseReference databaseReference = firebaseDatabase.getReference("Usuarios");



    // Método para registrar un nuevo usuario en Firebase Authentication
    public static void registrarUsuarioConEmailYContrasena(String correo, String contrasena, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(listener);
    }

    // Método para autenticar un usuario en Firebase
    public static void autenticarUsuarioEnFirebase(String correo, String contrasenya, OnCompleteListener<AuthResult> listener) {
        firebaseAuth.signInWithEmailAndPassword(correo, contrasenya)
                .addOnCompleteListener(listener);
    }

    // Método para guardar un usuario en Firebase Realtime Database
    public static void guardarUsuarioEnFirebaseDatabase(String userId, Map<String, Object> usuarioMap, OnCompleteListener<Void> listener) {
        databaseReference.child(userId).setValue(usuarioMap)
                .addOnCompleteListener(listener);
    }

    // Método para obtener el usuario actual autenticado
    public static FirebaseUser obtenerUsuarioAutenticado() {
        return firebaseAuth.getCurrentUser();
    }

    // Método para obtener el ID de usuario de Firebase
    public static String obtenerUserId() {
        FirebaseUser user = obtenerUsuarioAutenticado();
        return user != null ? user.getUid() : null;
    }



    // Métodos para obtener instancias principales
    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public static FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

}
