package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<Usuario> usuarios;
    private Context contexto;

    // Constructor
    public UsuarioAdapter(List<Usuario> usuarios, Context contexto) {
        this.usuarios = usuarios;
        this.contexto = contexto;
    }

    // Se llama cuando se crea un nuevo ViewHolder
    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        // Se infla el layout correspondiente según el tipo de usuario
        if (viewType == 1) {
            view = LayoutInflater.from(contexto).inflate(R.layout.lista_administrativos, parent, false);
        } else if (viewType == 2) {
            view = LayoutInflater.from(contexto).inflate(R.layout.lista_mecanico_jefes, parent, false);
        } else {
            view = LayoutInflater.from(contexto).inflate(R.layout.lista_mecanicos, parent, false);
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
    }

    // Devuelve el número de elementos en la lista
    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    // Determina el tipo de vista según el tipo de usuario
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

    // ViewHolder para cada item de la lista
    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre, textViewCorreo, textViewTelefono;

        // Constructor del ViewHolder
        public UsuarioViewHolder(View itemView) {
            super(itemView);

            // Inicializamos las vistas que estarán en cada item
            textViewNombre = itemView.findViewById(R.id.textViewNombreEmpleado);
            textViewCorreo = itemView.findViewById(R.id.textViewCorreoEmpleado);
            textViewTelefono = itemView.findViewById(R.id.textViewTelefonoEmpleado);
        }
    }
}
