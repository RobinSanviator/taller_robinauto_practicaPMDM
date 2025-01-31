package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsulta;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import java.util.List;

/**
 * Adaptador para mostrar una lista de usuarios (empleados) en un RecyclerView.
 * Este adaptador maneja diferentes tipos de vistas según el tipo de usuario (Administrativo, Mecánico Jefe, Mecánico).
 * Además, permite eliminar usuarios tanto de la lista como de las bases de datos local (SQLite) y remota (Firebase).
 */
public class UsuarioEmpleadoAdapter extends RecyclerView.Adapter<UsuarioEmpleadoAdapter.UsuarioViewHolder> {

    private List<Usuario> usuarios; // Lista de usuarios a mostrar
    private Context contexto; // Contexto de la aplicación

    /**
     * Constructor del adaptador.
     *
     * @param usuarios Lista de usuarios a mostrar.
     * @param contexto Contexto de la aplicación.
     */
    public UsuarioEmpleadoAdapter(List<Usuario> usuarios, Context contexto) {
        this.usuarios = usuarios;
        this.contexto = contexto;
    }

    /**
     * Se llama cuando se necesita crear un nuevo ViewHolder.
     * Infla el layout correspondiente según el tipo de usuario.
     *
     * @param parent El ViewGroup al que se añadirá la nueva vista.
     * @param viewType El tipo de vista, que depende del tipo de usuario.
     * @return Un nuevo UsuarioViewHolder que contiene la vista inflada.
     */
    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        // Inflar el layout correspondiente según el tipo de usuario
        if (viewType == 1) {
            view = LayoutInflater.from(contexto).inflate(R.layout.lista_administrativo, parent, false);
        } else if (viewType == 2) {
            view = LayoutInflater.from(contexto).inflate(R.layout.lista_mecanico_jefe, parent, false);
        } else {
            view = LayoutInflater.from(contexto).inflate(R.layout.lista_mecanico, parent, false);
        }
        return new UsuarioViewHolder(view);
    }

    /**
     * Se llama para enlazar los datos de un usuario con las vistas del ViewHolder.
     *
     * @param holder El ViewHolder que contiene las vistas a actualizar.
     * @param position La posición del usuario en la lista.
     */
    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);

        // Establecer los datos del usuario en las vistas del item
        holder.textViewNombre.setText(usuario.getNombre() + " " + usuario.getApellidos());
        holder.textViewCorreo.setText(usuario.getCorreo());
        holder.textViewTelefono.setText(usuario.getTelefono());

        // Configurar el clic en el TextView para eliminar el empleado
        holder.textViewEliminarEmpleado.setOnClickListener(v -> {
            String correo = usuario.getCorreo();
            alertDialogEliminarEmpleado(correo); // Mostrar diálogo de confirmación para eliminar
        });
    }

    /**
     * Devuelve el número de elementos en la lista de usuarios.
     *
     * @return El número de usuarios en la lista.
     */
    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    /**
     * Determina el tipo de vista según el tipo de usuario.
     *
     * @param position La posición del usuario en la lista.
     * @return Un entero que representa el tipo de vista (1: Administrativo, 2: Mecánico Jefe, 3: Mecánico).
     */
    @Override
    public int getItemViewType(int position) {
        String tipoUsuario = usuarios.get(position).getTipoUsuario();
        if (tipoUsuario.equals("Administrativo")) {
            return 1;
        } else if (tipoUsuario.equals("Mecánico jefe")) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * ViewHolder para los items del RecyclerView.
     * Contiene las vistas que representan cada usuario en la lista.
     */
    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewCorreo, textViewTelefono, textViewEliminarEmpleado;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView La vista que representa un item en el RecyclerView.
         */
        public UsuarioViewHolder(View itemView) {
            super(itemView);

            // Inicializar las vistas que estarán en cada item
            textViewNombre = itemView.findViewById(R.id.textViewNombreEmpleado);
            textViewCorreo = itemView.findViewById(R.id.textViewCorreoEmpleado);
            textViewTelefono = itemView.findViewById(R.id.textViewTelefonoEmpleado);
            textViewEliminarEmpleado = itemView.findViewById(R.id.textViewEliminarEmpleado);
        }
    }

    /**
     * Elimina un usuario de la lista del RecyclerView por su correo.
     *
     * @param correo El correo del usuario a eliminar.
     */
    public void eliminarEmpleadoPorCorreo(String correo) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCorreo().equals(correo)) {
                usuarios.remove(i); // Eliminar de la lista
                notifyItemRemoved(i); // Notificar al adaptador
                break;
            }
        }
    }

    /**
     * Elimina un usuario de la base de datos de Firebase por su correo.
     *
     * @param correoEmpleado El correo del usuario a eliminar.
     */
    public void eliminarEmpleadoFirebase(String correoEmpleado) {
        // Referencia a la base de datos en Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseUtil.getDatabaseReference();
        // Obtener todos los usuarios
        databaseRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    // Iterar a través de los usuarios y buscar el que coincida con el correo
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Usuario usuario = snapshot.getValue(Usuario.class);
                        if (usuario != null && usuario.getCorreo().equals(correoEmpleado)) {
                            String uid = snapshot.getKey(); // Obtener el UID del usuario
                            if (uid != null) {
                                // Eliminar del nodo "Usuarios"
                                databaseRef.child(uid).removeValue().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d("EliminarUsuario", "Usuario eliminado correctamente de la base de datos.");
                                    } else {
                                        Log.e("EliminarUsuario", "Error al eliminar usuario de la base de datos.", task1.getException());
                                    }
                                });
                            }
                            return; // Detener el bucle cuando encontremos el usuario
                        }
                    }
                    Log.e("EliminarUsuario", "Usuario no encontrado en la base de datos.");
                } else {
                    Log.e("EliminarUsuario", "No existen usuarios en la base de datos.");
                }
            } else {
                Log.e("EliminarUsuario", "Error al obtener los usuarios de la base de datos: " + task.getException());
            }
        });
    }

    /**
     * Elimina un usuario de la base de datos local SQLite por su correo.
     *
     * @param correo El correo del usuario a eliminar.
     */
    private void eliminarDeSQLite(String correo) {
        // Obtener la instancia de UsuarioConsultas a través de TallerRobinautoSQLite
        UsuarioConsulta usuarioConsultas = TallerRobinautoSQLite.getInstance(contexto.getApplicationContext()).obtenerUsuarioConsultas();

        boolean eliminado = usuarioConsultas.eliminarUsuarioSQLite(correo);

        if (eliminado) {
            Log.d("SQLite", "Usuario eliminado de la base de datos local");
        } else {
            Log.d("SQLite", "Usuario no registrado en la base de datos local");
        }

        if (correo == null) {
            Log.e("SQLite", "Error al eliminar el usuario en la base de datos local");
        }
    }

    /**
     * Muestra un diálogo de confirmación para eliminar un usuario.
     *
     * @param correoEmpleado El correo del usuario a eliminar.
     */
    private void alertDialogEliminarEmpleado(String correoEmpleado) {
        MaterialAlertDialogBuilder builderEliminar = new MaterialAlertDialogBuilder(contexto);
        builderEliminar.setTitle("Eliminar usuario")
                .setMessage("¿Estás seguro de que deseas eliminar al usuario con el correo: " + correoEmpleado + "? Esta acción no se puede deshacer.")
                .setIcon(R.drawable.ic_alerta)
                .setPositiveButton("Si", (dialog, which) -> {
                    // Llamar a los métodos para eliminar el empleado seleccionado
                    eliminarEmpleadoPorCorreo(correoEmpleado);
                    eliminarDeSQLite(correoEmpleado);
                    eliminarEmpleadoFirebase(correoEmpleado);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
