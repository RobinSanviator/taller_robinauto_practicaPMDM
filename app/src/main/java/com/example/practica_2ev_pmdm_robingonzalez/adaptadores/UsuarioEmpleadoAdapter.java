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

public class UsuarioEmpleadoAdapter extends RecyclerView.Adapter<UsuarioEmpleadoAdapter.UsuarioViewHolder> {

    private List<Usuario> usuarios;
    private Context contexto;

    // Constructor
    public UsuarioEmpleadoAdapter(List<Usuario> usuarios, Context contexto) {
        this.usuarios = usuarios;
        this.contexto = contexto;
    }

    //Se llama cuando se crea un nuevo ViewHolder e inflar la vista de los layouts
    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        // Se infla el layout correspondiente según el tipo de usuario
        if (viewType == 1) {
            view = LayoutInflater.from(contexto).inflate(R.layout.lista_administrativo, parent, false);
        } else if (viewType == 2) {
            view = LayoutInflater.from(contexto).inflate(R.layout.lista_mecanico_jefe, parent, false);
        } else {
            view = LayoutInflater.from(contexto).inflate(R.layout.lista_mecanico, parent, false);
        }
        return new UsuarioViewHolder(view);
    }

    // Se llama para enlazar los datos del usuario con el ViewHolder
    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);

        // Establecer los datos del usuario en las vistas del item
        holder.textViewNombre.setText(usuario.getNombre() + " " + usuario.getApellidos());
        holder.textViewCorreo.setText(usuario.getCorreo());
        holder.textViewTelefono.setText(usuario.getTelefono());

        //Al hacer clic en el textView llamará al alert dialog para eliminar el empleado
        holder.textViewEliminarEmpleado.setOnClickListener(v ->{
            String correo = usuario.getCorreo();
            alertDialogEliminarEmpleado(correo);
        });

    }

    // Devuelve el número de elementos en la lista
    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    //Determina el tipo de vista según el tipo de empleado
    @Override
    public int getItemViewType(int position) {
        String tipoUsuario = usuarios.get(position).getTipoUsuario();
        if (tipoUsuario.equals("Administrativo")) {
            return 1;
        } else if (tipoUsuario.equals("Mecanico jefe")) {
            return 2;
        } else {
            return 3;
        }
    }

    // Clase view holder para inicializar las vistas de los item texView
    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewCorreo, textViewTelefono, textViewEliminarEmpleado;

        // Constructor del ViewHolder
        public UsuarioViewHolder(View itemView) {
            super(itemView);

            // Inicializamos las vistas que estarán en cada item
            textViewNombre = itemView.findViewById(R.id.textViewNombreEmpleado);
            textViewCorreo = itemView.findViewById(R.id.textViewCorreoEmpleado);
            textViewTelefono = itemView.findViewById(R.id.textViewTelefonoEmpleado);
            textViewEliminarEmpleado = itemView.findViewById(R.id.textViewEliminarEmpleado);
        }
    }

    // Método para eliminar un usuario de la lista del RecyclerView
    public void eliminarEmpleadoPorCorreo(String correo) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCorreo().equals(correo)) {
                usuarios.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    // Método para eliminar un usuario de la base de datos de Firebase
    public void eliminarEmpleadoFirebase(String correoEmpleado) {
        // Referencia a la base de datos en Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseUtil.getDatabaseReference();
        // Obtener todos los usuarios sin usar un índice
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


    // Método para eliminar un usuario de la base de datos local SQLite
    private void eliminarDeSQLite(String correo) {
        // Obtener la instancia de UsuarioConsultas a través de TallerRobinautoSQLite
        UsuarioConsulta usuarioConsultas = TallerRobinautoSQLite.getInstance(contexto.getApplicationContext()).obtenerUsuarioConsultas();

        boolean eliminado = usuarioConsultas.eliminarUsuarioSQLite(correo);

        if (eliminado) {
            Log.d("SQLite", "Usuario eliminado de la base de datos local");
        } else{
            Log.d("SQLite", "Usuario no registrado en la base de datos local");
        }

        if(correo == null){
            Log.e("SQLite", "Error al eliminar el usuario en la base de datos local");
        }
    }

    // Método crear un cuadro de diálogo para preguntar si realmente quiere eliminar el usuario
    private void alertDialogEliminarEmpleado(String correoEmpleado){

        MaterialAlertDialogBuilder builderEliminar = new MaterialAlertDialogBuilder(contexto);
        builderEliminar.setTitle("Eliminar usuario")
                .setMessage("¿Estás seguro de que deseas eliminar al usuario con el correo: " + correoEmpleado + "? Esta acción no se puede deshacer.")
                .setIcon(R.drawable.ic_alerta)
                .setPositiveButton("Si", (dialog, which) -> {
                    //Llamar a los métodos para eliminar el empleado seleccionado
                    eliminarEmpleadoPorCorreo(correoEmpleado);
                    eliminarDeSQLite(correoEmpleado);
                    eliminarEmpleadoFirebase(correoEmpleado);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

}
