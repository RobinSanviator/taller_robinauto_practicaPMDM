package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UsuarioUtil {

    // Método estático que convierte un objeto Usuario a un Map<String, Object>
    public static Map<String, Object> anadirUsuarioFirebase(Usuario usuario) {
        // Obtener los datos del usuario y convertirlos a un Map
        Map<String, Object> mapUsuario = new LinkedHashMap<>();
        mapUsuario.put("nombre", usuario.getNombre());
        mapUsuario.put("apellidos", usuario.getApellidos());
        mapUsuario.put("correo", usuario.getCorreo());
        mapUsuario.put("telefono", usuario.getTelefono());
        mapUsuario.put("contrasenya", usuario.getContrasenya());
        mapUsuario.put("tipoUsuario", usuario.getTipoUsuario());

        Log.d("Map Usuario", "Map Usuario: " + mapUsuario.toString());  // Verifica el contenido del Map

        return mapUsuario;
    }

    // Método para guardar un empleado en Firebase
    public static void guardarEmpleadoEnFirebase(Context context, String nombre, String apellidos, String correo, String telefono, String contrasenya, String tipoUsuarioActual) {
        // Primero, intentamos registrar el usuario en Firebase Authentication
        FirebaseUtil.registrarUsuarioConEmailYContrasena(correo, contrasenya, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Si el registro es exitoso, obtenemos el ID del usuario autenticado
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        String userId = firebaseUser.getUid(); // Obtener el ID del usuario autenticado

                        // Crear el objeto Usuario con los detalles proporcionados
                        Usuario usuario = new Usuario(nombre, apellidos, correo, telefono, contrasenya, tipoUsuarioActual);

                        // Convertir el objeto Usuario a un Map<String, Object> para almacenarlo en Firebase
                        Map<String, Object> usuarioMap = anadirUsuarioFirebase(usuario);
                        Log.d("UsuarioUtils", "Datos del Usuario: " + usuarioMap.toString());
                        // Llamar al método para guardar los datos del usuario en Firebase Database
                        FirebaseUtil.guardarUsuarioEnFirebaseDatabase(userId, usuarioMap, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("UsuarioUtils", "Empleado guardado exitosamente.");
                                } else {
                                    Log.e("UsuarioUtils", "Error al guardar el empleado: " + task.getException().getMessage());
                                }
                            }
                        });
                    } else {
                        // El usuario no se ha autenticado correctamente
                        Log.e("UsuarioUtils", "Error al obtener el ID del usuario.");
                    }
                } else {
                    // Si el registro falla, manejar el error
                    Log.e("UsuarioUtils", "Error al registrar el usuario: " + task.getException().getMessage());
                }
            }
        });
    }

    public static void actualizarUsuarioEnFirebase(Usuario usuario) {
        // Obtener la referencia a la base de datos de Firebase
        DatabaseReference databaseReference = FirebaseUtil.getDatabaseReference();

        // Buscar al usuario en Firebase mediante su correo electrónico
        databaseReference.orderByChild("correo").equalTo(usuario.getCorreo()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Verificar si el usuario existe en Firebase
                if (dataSnapshot.exists()) {
                    // Asumimos que el usuario tiene un campo 'correo' en la base de datos
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey();  // Obtener el ID del usuario

                        // Crear un mapa con los datos que queremos actualizar
                        Map<String, Object> actualizarUsuario = new HashMap<>();
                        actualizarUsuario.put("nombre", usuario.getNombre());
                        actualizarUsuario.put("apellidos", usuario.getApellidos());
                        actualizarUsuario.put("telefono", usuario.getTelefono());

                        // Actualizar los datos del usuario en Firebase
                        databaseReference.child(userId).updateChildren(actualizarUsuario)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("ActualizarUsuarioEnFirebase","Usuario actualizado correctamente");
                                    } else {
                                        Log.e("ActualizarUsuarioEnFirebase", "Error al actualizar usuario");
                                    }
                                });
                    }
                } else {
                    // Si no se encuentra el usuario en Firebase
                    Log.e("ActualizarUsuarioEnFirebase", "Error al obtener los usuarios");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ActualizarUsuarioEnFirebase", "Error al acceder a la base de datos");
            }
        });
    }


    public static void cargarUsuariosBBBDD(usuariosCargadosListener listener){
        FirebaseUtil.getDatabaseReference()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Usuario> usuariosList = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Usuario usuario = data.getValue(Usuario.class);
                            if (usuario != null) {
                                usuariosList.add(usuario);
                            }
                        }
                        listener.onUsuariosCargados(usuariosList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.toException());
                    }
                });
    }

    // Método para cargar usuarios según el tipo
    public static void cargarUsuariosPorTipo(String tipoUsuario, usuariosCargadosListener listener) {
        FirebaseUtil.getDatabaseReference()
                .orderByChild("tipoUsuario")
                .equalTo(tipoUsuario)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Usuario> usuariosList = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Usuario usuario = data.getValue(Usuario.class);
                            if (usuario != null) {
                                usuariosList.add(usuario);
                            }
                        }
                        listener.onUsuariosCargados(usuariosList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.toException());
                    }
                });
    }

    // Interfaz para manejar callbacks de carga de usuarios
    public interface usuariosCargadosListener {
        void onUsuariosCargados(List<Usuario> usuarios);
        void onError(Exception e);
    }

    public static void obtenerNombreCompletoPorCorreo(String correo, TextView textView) {
        // Reemplazar puntos para usar correos como claves en Firebase
        String claveCorreo = correo.replace(".", ",");

        DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();

        usuariosRef.child(claveCorreo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String apellido = snapshot.child("apellido").getValue(String.class);

                    // Mostrar el nombre completo en el TextView
                    textView.setText(nombre + " " + apellido);
                } else {
                    textView.setText("Usuario no encontrado");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textView.setText("Usuario no encontrado");
            }
        });
    }


}
