package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    public static void guardarEmpleadoEnFirebase(Context contexto, String nombre, String apellidos, String correo, String telefono, String contrasenya, String tipoUsuarioActual) {
        // Primero, intentamos registrar el usuario en Firebase Authentication
        FirebaseUtil.registrarUsuarioConEmailYContrasena(correo, contrasenya, task -> {
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
                    FirebaseUtil.guardarUsuarioEnFirebaseDatabase(userId, usuarioMap, new OnCompleteListener<>() {
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
        });
    }


    public static void actualizarUsuarioEnFirebase(Usuario usuario) {
        // Obtener la referencia a la base de datos de Firebase
        DatabaseReference databaseReference = FirebaseUtil.getDatabaseReference();


        // Buscar al usuario en Firebase mediante su correo electrónico
        databaseReference.orderByChild("correo").equalTo(usuario.getCorreo()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
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


    // Método para buscar los datos del mecánico por correo
    public static void obtenerNombreMecanicoPorCorreo(String correoMecanico, TextView textViewMecanico) {
        if (correoMecanico != null) {
            Log.d("Firebase", "correo buscado " +correoMecanico);

            DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();
            usuariosRef.orderByChild("correo").equalTo(correoMecanico).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String nombre = snapshot.child("nombre").getValue(String.class);
                            String apellido = snapshot.child("apellidos").getValue(String.class);


                            Log.d("FirebaseSearch", "Mecánico encontrado: " + nombre + " " + apellido);
                            textViewMecanico.setText("Mjefe: "+ nombre + " " + apellido);
                            break;
                        }
                    } else {
                        Log.d("FirebaseSearch", "Mecánico no encontrado");
                        textViewMecanico.setText("Mecánico no encontrado");
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseSearch", "Error al buscar mecánico: " + databaseError.getMessage());
                    textViewMecanico.setText("Error al buscar mecánico");
                }
            });
        } else {
            textViewMecanico.setText("Mecánico jefe");
        }
    }


    // Método para buscar los datos del cliente por correo
    public static void obtenerNombreClientePorCorreo(String correoCliente, TextView textViewCliente) {
        if (correoCliente != null) {
            Log.d("Firebase", "correo buscado " +correoCliente);
            DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();
            usuariosRef.orderByChild("correo").equalTo(correoCliente).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String nombre = snapshot.child("nombre").getValue(String.class);
                            String apellido = snapshot.child("apellidos").getValue(String.class);


                            Log.d("FirebaseSearch", "Cliente encontrado: " + nombre + " " + apellido);
                            textViewCliente.setText("Cliente: " + nombre + " " + apellido);
                            break;
                        }
                    } else {
                        Log.d("FirebaseSearch", "Cliente no encontrado");
                        textViewCliente.setText("Cliente no encontrado");
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FirebaseSearch", "Error al buscar cliente: " + databaseError.getMessage());
                    textViewCliente.setText("Error al buscar cliente");
                }
            });
        } else {
            textViewCliente.setText("Cliente");
        }
    }

    public static void esMecanicoPorCorreo(String correo, final esMecanicoListener listener) {
        DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();
        usuariosRef.orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Obtener el tipo de usuario del registro
                        String tipoUsuario = snapshot.child("tipoUsuario").getValue(String.class);

                        // Verificar si el tipo de usuario es "Mecanico"
                        if ("Mecanico".equals(tipoUsuario)) {
                            // Es un mecánico
                            listener.onResultado(true);
                        } else {
                            // No es un mecánico
                            listener.onResultado(false);
                        }
                        break;  // Solo procesamos un usuario
                    }
                } else {
                    // El correo no existe en la base de datos
                    listener.onResultado(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("UsuarioUtil", "Error al buscar el tipo de usuario: " + databaseError.getMessage());
                listener.onResultado(false);
            }
        });
    }

    // Interfaz de callback para manejar el resultado
    public interface esMecanicoListener {
        void onResultado(boolean esMecanico);
    }

}
