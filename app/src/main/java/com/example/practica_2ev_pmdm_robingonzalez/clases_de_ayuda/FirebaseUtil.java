package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import java.util.Map;

/**
 * Clase utilitaria para gestionar la interacción con Firebase Authentication y Firebase Realtime Database.
 * Contiene métodos para registrar, autenticar, y gestionar usuarios, así como acceder a instancias de Firebase.
 */
public class FirebaseUtil {
    // Instancia de FirebaseAuth para gestionar autenticaciones de usuario
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    // Instancia de FirebaseDatabase para gestionar la base de datos Realtime
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://gestortaller-b9308-default-rtdb.europe-west1.firebasedatabase.app");
    // Referencia al nodo "Usuarios" en la base de datos
    private static DatabaseReference databaseReference = firebaseDatabase.getReference("Usuarios");

    /**
     * Registra un nuevo usuario en Firebase Authentication con email y contraseña.
     *
     * @param correo El correo electrónico del usuario.
     * @param contrasena La contraseña del usuario.
     * @param listener El listener que maneja la respuesta de la operación de registro.
     */
    public static void registrarUsuarioConEmailYContrasena(String correo, String contrasena, OnCompleteListener<AuthResult> listener) {
        // Crear usuario con email y contraseña
        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(listener); // Llamar al listener cuando se complete la operación
    }

    /**
     * Autentica un usuario en Firebase con email y contraseña.
     *
     * @param correo El correo electrónico del usuario.
     * @param contrasenya La contraseña del usuario.
     * @param listener El listener que maneja la respuesta de la operación de autenticación.
     */
    public static void autenticarUsuarioEnFirebase(String correo, String contrasenya, OnCompleteListener<AuthResult> listener) {
        // Autenticar usuario con email y contraseña
        firebaseAuth.signInWithEmailAndPassword(correo, contrasenya)
                .addOnCompleteListener(listener); // Llamar al listener cuando se complete la operación
    }

    /**
     * Guarda un usuario en Firebase Realtime Database bajo un ID único.
     *
     * @param userId El ID del usuario que se va a guardar.
     * @param usuarioMap Los datos del usuario en forma de un mapa de clave-valor.
     * @param listener El listener que maneja la respuesta de la operación de guardado.
     */
    public static void guardarUsuarioEnFirebaseDatabase(String userId, Map<String, Object> usuarioMap, OnCompleteListener<Void> listener) {
        // Guardar los datos del usuario en Firebase Database
        databaseReference.child(userId).setValue(usuarioMap)
                .addOnCompleteListener(listener); // Llamar al listener cuando se complete la operación
    }

    /**
     * Obtiene el usuario actual autenticado en Firebase.
     *
     * @return El usuario autenticado en Firebase, o null si no hay usuario autenticado.
     */
    public static FirebaseUser obtenerUsuarioAutenticado() {
        return firebaseAuth.getCurrentUser();
    }

    /**
     * Obtiene el ID del usuario actual autenticado.
     *
     * @return El ID del usuario autenticado, o null si no hay usuario autenticado.
     */
    public static String obtenerUserId() {
        FirebaseUser user = obtenerUsuarioAutenticado();
        return user != null ? user.getUid() : null;
    }

    // Métodos para obtener instancias principales de Firebase
    /**
     * Obtiene la instancia de FirebaseAuth.
     *
     * @return La instancia de FirebaseAuth.
     */
    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    /**
     * Obtiene la instancia de FirebaseDatabase.
     *
     * @return La instancia de FirebaseDatabase.
     */
    public static FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    /**
     * Obtiene la referencia al nodo "Usuarios" en Firebase Realtime Database.
     *
     * @return La referencia al nodo "Usuarios".
     */
    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}
