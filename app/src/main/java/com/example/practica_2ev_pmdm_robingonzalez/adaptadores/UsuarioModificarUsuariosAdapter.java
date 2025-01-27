package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsulta;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class UsuarioModificarUsuariosAdapter extends RecyclerView.Adapter<UsuarioModificarUsuariosAdapter.UsuarioViewHolder> {

    private List<Usuario> usuarios;
    private Context contexto;

    // Constructor
    public UsuarioModificarUsuariosAdapter(List<Usuario> usuarios, Context contexto) {
        this.usuarios = usuarios;
        this.contexto = contexto;
    };

    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar layout para los usuarios
        View view = LayoutInflater.from(contexto).inflate(R.layout.lista_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);

        // Configurar las vistas con los datos del usuario
        holder.textViewNombreUsuario.setText(usuario.getNombre() + " " + usuario.getApellidos());
        holder.textViewCorreoUsuario.setText(usuario.getCorreo());
        holder.textViewTelefonoUsuario.setText(usuario.getTelefono());
        holder.textViewTipoUsuario.setText(usuario.getTipoUsuario());

        // Establecer el comportamiento del botón para modificar el usuario
        holder.textViewModificarUsuario.setOnClickListener(v -> mostrarDialogoModificacion(usuario, holder));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    };


    private void mostrarDialogoModificacion(Usuario usuario, UsuarioViewHolder holder) {
        // Crear el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        View view = LayoutInflater.from(contexto).inflate(R.layout.administrador_modificar_usuario_dialog, null);

        // Referencias a los campos del cuadro de diálogo
        TextInputEditText editTextNombre = view.findViewById(R.id.editTextNombreUsuario);
        TextInputEditText editTextApellidos = view.findViewById(R.id.editTextApellidosUsuario);
        TextInputEditText editTextTelefono = view.findViewById(R.id.editTextTelefonoUsuario);
        MaterialButton materialButtonGuardar = view.findViewById(R.id.buttonGuardarCambios);

        // Cargar los datos del usuario en los campos de texto
       cargarDatosUsuarioEnCampos(usuario, editTextNombre, editTextApellidos, editTextTelefono);

        builder.setView(view)
                .setTitle("Modificar Usuario")
                .setIcon(R.drawable.ic_modificar)
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .create();

        AlertDialog dialog = builder.show();

        // Configurar el botón Guardar para actualizar los datos
        materialButtonGuardar.setOnClickListener(v -> {
            // Actualizar el usuario con los nuevos valores
            usuario.setNombre(editTextNombre.getText().toString());
            usuario.setApellidos(editTextApellidos.getText().toString());
            usuario.setTelefono(editTextTelefono.getText().toString());


            // Llamar al método para actualizar el usuario en la base de datos local
            UsuarioConsulta usuarioConsulta = TallerRobinautoSQLite.getInstance(contexto).obtenerUsuarioConsultas();
            boolean actualizadoEnSQLite = usuarioConsulta.actualizarUsuario(usuario);

            String correoFirebase = correoFirebase(usuario.getCorreo());

            // Llamar al método para actualizar el usuario en la base de datos remota (Firebase)
            UsuarioUtil.actualizarUsuarioEnFirebase(usuario);

            // Verificar si la actualización fue exitosa en ambas bases de datos

            if (actualizadoEnSQLite) {
                // Si la actualización fue exitosa en SQLite, verificamos Firebase
                FirebaseUtil.getDatabaseReference().child(correoFirebase) // Usar correo seguro aquí
                        .setValue(usuario)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                            } else {
                                // Si la actualización en Firebase falla
                                Snackbar.make(holder.itemView, "Error al actualizar el usuario", Snackbar.LENGTH_LONG).show();
                            }
                        });
            } else {
                // Si no se pudo actualizar en SQLite, cerrar el diálogo
                dialog.dismiss();
                //Si se actualizó en Firebase
                Snackbar.make(holder.itemView, "Usuario actualizado correctamente", Snackbar.LENGTH_LONG).show();
                notifyDataSetChanged();
            }
        });
    }

    private String correoFirebase(String correo) {
        // Reemplazar el punto por guion bajo para que sea seguro para Firebase
        return correo.replace(".", "_");
    }

    private void cargarDatosUsuarioEnCampos(Usuario usuario, TextInputEditText editTextNombre, TextInputEditText editTextApellidos, TextInputEditText editTextTelefono) {
        // Cargar los datos actuales del usuario en los campos de edición
        editTextNombre.setText(usuario.getNombre());
        editTextApellidos.setText(usuario.getApellidos());
        editTextTelefono.setText(usuario.getTelefono());
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreUsuario, textViewCorreoUsuario, textViewTelefonoUsuario, textViewTipoUsuario , textViewModificarUsuario;
        MaterialButton materialButtonGuardarCambios;
        public UsuarioViewHolder(View itemView) {
            super(itemView);
            textViewNombreUsuario = itemView.findViewById(R.id.textViewNombreUsuario);
            textViewCorreoUsuario = itemView.findViewById(R.id.textViewCorreoUsuario);
            textViewTelefonoUsuario = itemView.findViewById(R.id.textViewTelefonoUsuario);
            textViewModificarUsuario = itemView.findViewById(R.id.textViewModificarUsuario);
            textViewTipoUsuario = itemView.findViewById(R.id.textViewTipoUsuario);
            materialButtonGuardarCambios = itemView.findViewById(R.id.buttonGuardarCambios);

        }
    }
}


