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

/**
 * Esta clase contiene métodos estáticos para gestionar usuarios en Firebase.
 * Permite convertir un objeto Usuario a un Map, guardar usuarios en Firebase, actualizar datos de usuarios
 * y cargar usuarios desde Firebase Database.
 */
public class UsuarioUtil {

    /**
     * Convierte un objeto Usuario a un mapa de claves y valores para almacenarlo en Firebase.
     *
     * @param usuario El objeto Usuario que contiene la información a guardar.
     * @return Un mapa con las claves y valores correspondientes a los atributos del usuario.
     */
    public static Map<String, Object> anadirUsuarioFirebase(Usuario usuario) {
        // Crear un Map para almacenar los datos del usuario
        Map<String, Object> mapUsuario = new LinkedHashMap<>();
        // Añadir los atributos del usuario al Map
        mapUsuario.put("nombre", usuario.getNombre());
        mapUsuario.put("apellidos", usuario.getApellidos());
        mapUsuario.put("correo", usuario.getCorreo());
        mapUsuario.put("telefono", usuario.getTelefono());
        mapUsuario.put("contrasenya", usuario.getContrasenya());
        mapUsuario.put("tipoUsuario", usuario.getTipoUsuario());

        Log.d("Map Usuario", "Map Usuario: " + mapUsuario.toString());  // Imprimir en el log para verificar el contenido del Map

        return mapUsuario;
    }

    /**
     * Registra un nuevo empleado en Firebase Authentication y guarda sus datos en Firebase Database.
     *
     * @param contexto El contexto de la actividad donde se llama este método.
     * @param nombre El nombre del usuario.
     * @param apellidos Los apellidos del usuario.
     * @param correo El correo electrónico del usuario.
     * @param telefono El número de teléfono del usuario.
     * @param contrasenya La contraseña del usuario.
     * @param tipoUsuarioActual El tipo de usuario (ej. "administrador", "empleado", etc.).
     */
    public static void guardarEmpleadoEnFirebase(Context contexto, String nombre, String apellidos, String correo, String telefono, String contrasenya, String tipoUsuarioActual) {
        // Intentar registrar al usuario en Firebase Authentication con el correo y la contraseña proporcionados
        FirebaseUtil.registrarUsuarioConEmailYContrasena(correo, contrasenya, task -> {
            if (task.isSuccessful()) {
                // Si el registro es exitoso, obtener el ID del usuario autenticado
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid(); // Obtener el ID del usuario autenticado

                    // Crear un objeto Usuario con los detalles proporcionados
                    Usuario usuario = new Usuario(nombre, apellidos, correo, telefono, contrasenya, tipoUsuarioActual);

                    // Convertir el objeto Usuario a un Map<String, Object> para almacenarlo en Firebase
                    Map<String, Object> usuarioMap = anadirUsuarioFirebase(usuario);
                    Log.d("UsuarioUtils", "Datos del Usuario: " + usuarioMap.toString());
                    // Llamar a FirebaseUtil para guardar los datos del usuario en Firebase Database
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
                    // Si no se ha obtenido el ID del usuario, registrar un error
                    Log.e("UsuarioUtils", "Error al obtener el ID del usuario.");
                }
            } else {
                // Si el registro falla, manejar el error
                Log.e("UsuarioUtils", "Error al registrar el usuario: " + task.getException().getMessage());
            }
        });
    }

    /**
     * Actualiza los datos de un usuario en Firebase Database.
     *
     * @param usuario El objeto Usuario con los nuevos datos a actualizar.
     */
    public static void actualizarUsuarioEnFirebase(Usuario usuario) {
        // Obtener referencia a la base de datos de Firebase
        DatabaseReference databaseReference = FirebaseUtil.getDatabaseReference();

        // Buscar al usuario en Firebase utilizando su correo electrónico
        databaseReference.orderByChild("correo").equalTo(usuario.getCorreo()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verificar si el usuario existe en la base de datos
                if (dataSnapshot.exists()) {
                    // Si el usuario existe, obtener su ID
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey();  // Obtener el ID del usuario

                        // Crear un mapa con los datos que se desean actualizar
                        Map<String, Object> actualizarUsuario = new HashMap<>();
                        actualizarUsuario.put("nombre", usuario.getNombre());
                        actualizarUsuario.put("apellidos", usuario.getApellidos());
                        actualizarUsuario.put("telefono", usuario.getTelefono());

                        // Actualizar los datos del usuario en Firebase
                        databaseReference.child(userId).updateChildren(actualizarUsuario)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("ActualizarUsuarioEnFirebase", "Usuario actualizado correctamente");
                                    } else {
                                        Log.e("ActualizarUsuarioEnFirebase", "Error al actualizar usuario");
                                    }
                                });
                    }
                } else {
                    // Si el usuario no se encuentra en Firebase
                    Log.e("ActualizarUsuarioEnFirebase", "Error al obtener los usuarios");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Si ocurre un error al acceder a la base de datos
                Log.e("ActualizarUsuarioEnFirebase", "Error al acceder a la base de datos");
            }
        });
    }

    /**
     * Carga todos los usuarios de Firebase Database y los pasa a través de un listener.
     *
     * @param listener El listener que maneja los usuarios cargados o los errores.
     */
    public static void cargarUsuariosBBBDD(usuariosCargadosListener listener) {
        // Obtener referencia a la base de datos de Firebase y cargar los usuarios
        FirebaseUtil.getDatabaseReference()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Crear una lista para almacenar los usuarios
                        List<Usuario> usuariosList = new ArrayList<>();
                        // Iterar a través de los datos recuperados y convertir cada usuario
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Usuario usuario = data.getValue(Usuario.class);
                            if (usuario != null) {
                                usuariosList.add(usuario);
                            }
                        }
                        // Pasar la lista de usuarios al listener
                        listener.onUsuariosCargados(usuariosList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Manejar el error si ocurre
                        listener.onError(error.toException());
                    }
                });
    }


    /**
     * Método para cargar usuarios de Firebase filtrados por el tipo de usuario.
     *
     * @param tipoUsuario El tipo de usuario a filtrar (por ejemplo: "administrador", "mecánico").
     * @param listener El listener que manejará la respuesta con la lista de usuarios cargados o el error.
     */
    public static void cargarUsuariosPorTipo(String tipoUsuario, usuariosCargadosListener listener) {
        FirebaseUtil.getDatabaseReference()
                .orderByChild("tipoUsuario")  // Filtra por el campo "tipoUsuario"
                .equalTo(tipoUsuario)  // Igualdad con el tipo de usuario especificado
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Usuario> usuariosList = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            // Se convierte cada objeto de la base de datos en un objeto Usuario
                            Usuario usuario = data.getValue(Usuario.class);
                            if (usuario != null) {
                                usuariosList.add(usuario);  // Se agrega el usuario a la lista si no es nulo
                            }
                        }
                        // Se ejecuta el callback para devolver la lista de usuarios cargados
                        listener.onUsuariosCargados(usuariosList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // En caso de error, se ejecuta el método onError del listener
                        listener.onError(error.toException());
                    }
                });
    }

    /**
     * Interfaz utilizada para manejar los callbacks cuando los usuarios son cargados de Firebase.
     */
    public interface usuariosCargadosListener {
        /**
         * Método que se llama cuando los usuarios han sido cargados correctamente desde Firebase.
         *
         * @param usuarios Lista de usuarios cargados desde Firebase.
         */
        void onUsuariosCargados(List<Usuario> usuarios);

        /**
         * Método que se llama si ocurre un error durante la carga de usuarios.
         *
         * @param e La excepción que ocurrió durante la carga.
         */
        void onError(Exception e);
    }

    /**
     * Método para buscar los datos de un mecánico en Firebase utilizando su correo electrónico.
     *
     * @param correoMecanico El correo electrónico del mecánico.
     * @param textViewMecanico El TextView donde se mostrará el nombre y apellido del mecánico encontrado.
     */
    public static void obtenerNombreMecanicoPorCorreo(String correoMecanico, TextView textViewMecanico) {
        if (correoMecanico != null) {
            Log.d("Firebase", "correo buscado " + correoMecanico);

            DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();
            // Se busca el usuario que coincida con el correo del mecánico
            usuariosRef.orderByChild("correo").equalTo(correoMecanico).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Si existen resultados, se extraen los datos del mecánico
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String nombre = snapshot.child("nombre").getValue(String.class);
                            String apellido = snapshot.child("apellidos").getValue(String.class);

                            // Se muestra el nombre y apellido del mecánico en el TextView
                            Log.d("FirebaseSearch", "Mecánico encontrado: " + nombre + " " + apellido);
                            textViewMecanico.setText("Mjefe: " + nombre + " " + apellido);
                            break;
                        }
                    } else {
                        // Si no se encuentra el mecánico, se muestra el mensaje de "Mecánico no encontrado"
                        Log.d("FirebaseSearch", "Mecánico no encontrado");
                        textViewMecanico.setText("Mecánico no encontrado");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Si ocurre un error durante la búsqueda, se muestra el mensaje de error
                    Log.e("FirebaseSearch", "Error al buscar mecánico: " + databaseError.getMessage());
                    textViewMecanico.setText("Error al buscar mecánico");
                }
            });
        } else {
            // Si no se proporciona un correo, se muestra "Mecánico jefe" por defecto
            textViewMecanico.setText("Mecánico jefe");
        }
    }

    /**
     * Método para buscar los datos de un cliente en Firebase utilizando su correo electrónico.
     *
     * @param correoCliente El correo electrónico del cliente.
     * @param textViewCliente El TextView donde se mostrará el nombre y apellido del cliente encontrado.
     */
    public static void obtenerNombreClientePorCorreo(String correoCliente, TextView textViewCliente) {
        if (correoCliente != null) {
            Log.d("Firebase", "correo buscado " + correoCliente);

            DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();
            // Se busca el usuario que coincida con el correo del cliente
            usuariosRef.orderByChild("correo").equalTo(correoCliente).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Si existen resultados, se extraen los datos del cliente
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String nombre = snapshot.child("nombre").getValue(String.class);
                            String apellido = snapshot.child("apellidos").getValue(String.class);

                            // Se muestra el nombre y apellido del cliente en el TextView
                            Log.d("FirebaseSearch", "Cliente encontrado: " + nombre + " " + apellido);
                            textViewCliente.setText("Cliente: " + nombre + " " + apellido);
                            break;
                        }
                    } else {
                        // Si no se encuentra el cliente, se muestra el mensaje de "Cliente no encontrado"
                        Log.d("FirebaseSearch", "Cliente no encontrado");
                        textViewCliente.setText("Cliente no encontrado");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Si ocurre un error durante la búsqueda, se muestra el mensaje de error
                    Log.e("FirebaseSearch", "Error al buscar cliente: " + databaseError.getMessage());
                    textViewCliente.setText("Error al buscar cliente");
                }
            });
        } else {
            // Si no se proporciona un correo, se muestra "Cliente" por defecto
            textViewCliente.setText("Cliente");
        }
    }

    /**
     * Método para verificar si un usuario con un correo específico es un mecánico.
     *
     * @param correo El correo electrónico del usuario a verificar.
     * @param listener El listener que manejará el resultado de la verificación: si es mecánico o no.
     */
    public static void esMecanicoPorCorreo(String correo, final esMecanicoListener listener) {
        DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();
        // Se busca el usuario en la base de datos que coincida con el correo proporcionado
        usuariosRef.orderByChild("correo").equalTo(correo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Si el correo existe en la base de datos
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Obtener el tipo de usuario del registro
                        String tipoUsuario = snapshot.child("tipoUsuario").getValue(String.class);

                        // Verificar si el tipo de usuario es "Mecanico"
                        if ("Mecanico".equals(tipoUsuario)) {
                            // Es un mecánico, se ejecuta el callback con valor true
                            listener.onResultado(true);
                        } else {
                            // No es un mecánico, se ejecuta el callback con valor false
                            listener.onResultado(false);
                        }
                        break;  // Solo procesamos un usuario (en este caso solo debería haber uno)
                    }
                } else {
                    // Si no se encuentra el correo en la base de datos, se ejecuta el callback con valor false
                    listener.onResultado(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Si ocurre un error durante la búsqueda, se muestra el error en los logs y se ejecuta el callback con false
                Log.e("UsuarioUtil", "Error al buscar el tipo de usuario: " + databaseError.getMessage());
                listener.onResultado(false);
            }
        });
    }

    /**
     * Interfaz de callback para manejar el resultado de la verificación de si un usuario es mecánico.
     */
    public interface esMecanicoListener {
        /**
         * Método que se llama con el resultado de la verificación si el usuario es mecánico o no.
         *
         * @param esMecanico Resultado de la verificación (true si es mecánico, false si no lo es).
         */
        void onResultado(boolean esMecanico);
    }
}