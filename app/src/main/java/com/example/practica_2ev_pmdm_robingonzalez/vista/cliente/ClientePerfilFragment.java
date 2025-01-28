package com.example.practica_2ev_pmdm_robingonzalez.vista.cliente;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsulta;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperPerfil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class ClientePerfilFragment extends Fragment {

    private TextView textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono;
    private TextView textViewNombreCabecera,  textViewCorreoCabecera;
    private ImageView imageViewMenuPrincipal;
    private String correo;
    private ClienteActivity activityCliente;
    private HelperPerfil helperPerfil;
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
    private UsuarioConsulta usuarioConsulta;
    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperNavegacionInferior helperNavegacionInferior;
    private MaterialButton materialButtonEditar;
    private TextInputEditText editTextNombre, editTextApellidos,  editTextTelefono;
    MaterialButton materialButtonGuardar;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño layout perfil cliente
        View vista = inflater.inflate(R.layout.cliente_perfil_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipal();
        introducirDatosPerfilCabecera();
        introducirDatosEnPerfil();
        configurarBotonEditar();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewMenuPrincipal = vista.findViewById(R.id.imageViewVolverMenuPrincipalPerfilCliente);
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombrePerfilCliente);
        textViewCorreoCabecera = vista.findViewById(R.id.textViewCorreoPerfilCliente);
        textViewNombre = vista.findViewById(R.id.textViewDatoPerfilNombreCliente);
        textViewApellidos = vista.findViewById(R.id.textViewDatoPerfilApellidoCliente);
        textViewCorreo = vista.findViewById(R.id.textViewDatoPerfilCorreoCliente);
        textViewTelefono = vista.findViewById(R.id.textViewDatoPerfilTelefonoCliente);
        materialButtonEditar = vista.findViewById(R.id.buttonEditarPerfilCliente);
    }

    private void obtenerHelper() {
        if (getActivity() instanceof ClienteActivity) {
            activityCliente = ((ClienteActivity) getActivity());
            helperPerfil = activityCliente.getManejadorPerfil();
            helperMenuPrincipal = activityCliente.getHelperMenuPrincipal();
            helperNavegacionInferior = activityCliente.getHelperNavegacionInferior();
            baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(getContext());
            usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
        }

    }

    private void volverMenuPrincipal(){
        imageViewMenuPrincipal.setOnClickListener(v -> {
            if(helperMenuPrincipal != null){
                helperMenuPrincipal.cargarFragmento(new ClienteMenuPrincipalFragment());
                helperNavegacionInferior.seleccionarItemMenuPrincipal();
            } else {
                Log.e("Error", "ClientePerfil null");
            }

        });
    }

    private void introducirDatosPerfilCabecera(){
        correo = activityCliente.getCorreo();
        String nombre = usuarioConsulta.obtenerNombreYApellidos(correo);

        if(correo != null && nombre != null){
            textViewNombreCabecera.setText(nombre);
            textViewCorreoCabecera.setText(correo);

        } else {
            helperPerfil.cargarDatosPerfilCabeceraDesdeFirebase(correo, textViewNombreCabecera, textViewCorreoCabecera);
        }

    }

    private void introducirDatosEnPerfil() {
        correo = activityCliente.getCorreo();
        if (correo != null) {
            helperPerfil.cargarDatosPerfil(correo,textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono);
        } else {
            Log.e("ClientePerfilFragment", "Correo es null");
        }

    }

    private void configurarBotonEditar(){
        materialButtonEditar.setOnClickListener(v -> {
            mostrarDialogoParaEditar();

        });
    }

    private void mostrarDialogoParaEditar() {
        MaterialAlertDialogBuilder builderEditar = new MaterialAlertDialogBuilder(getContext());

        // Inflar el layout para el diálogo
        View vistaDialogo = LayoutInflater.from(getContext()).inflate(R.layout.administrador_modificar_usuario_dialog, null);
        iniciarComponenetesDialogo(vistaDialogo);  // Inicializar los componentes del layout

        // Cargar los datos actuales del usuario en los campos
        cargarDatosUsuarioEnCampos();

        // Crear el cuadro de diálogo
        AlertDialog dialogoEditar = builderEditar.setTitle("Editar perfil")
                .setIcon(R.drawable.cliente)
                .setView(vistaDialogo)
                .setNegativeButton("Salir", (dialog, which) -> dialog.dismiss())
                .create();

        dialogoEditar.show();

        // Configurar el botón Guardar en el layout del diálogo
        materialButtonGuardar.setOnClickListener(v -> {
            guardarModificacionEnFirebase();
            dialogoEditar.dismiss(); // Cerrar el diálogo después de guardar
        });
    }

    private void iniciarComponenetesDialogo(View vistaDialogo){
        // Referencias a los campos del cuadro de diálogo
        editTextNombre = vistaDialogo.findViewById(R.id.editTextNombreUsuario);
        editTextApellidos = vistaDialogo.findViewById(R.id.editTextApellidosUsuario);
        editTextTelefono = vistaDialogo.findViewById(R.id.editTextTelefonoUsuario);
        materialButtonGuardar = vistaDialogo.findViewById(R.id.buttonGuardarCambios);
    }



    private void cargarDatosUsuarioEnCampos() {
        // Cargar los datos actuales en los campos de edición
        String nombre = textViewNombre.getText().toString();
        String apellidos = textViewApellidos.getText().toString();
        String telefono = textViewTelefono.getText().toString();

        editTextNombre.setText(nombre);
        editTextApellidos.setText(apellidos);
        editTextTelefono.setText(telefono);
    }

    private void guardarModificacionEnFirebase() {
        // Obtener los valores de los campos de texto
        String nombre = editTextNombre.getText().toString();
        String apellidos = editTextApellidos.getText().toString();
        String telefono = editTextTelefono.getText().toString();

        // Crear un nuevo objeto Usuario con los datos modificados
        Usuario usuarioModificado = new Usuario();
        usuarioModificado.setCorreo(correo);  // Usar el correo del usuario actual
        usuarioModificado.setNombre(nombre);
        usuarioModificado.setApellidos(apellidos);
        usuarioModificado.setTelefono(telefono);

        // Llamar al método para guardar los cambios en la base de datos local (SQLite) y Firebase
        guardarModificacionEnBaseDeDatos(usuarioModificado);
    }

    private void guardarModificacionEnBaseDeDatos(Usuario usuario) {
        // Obtener instancia de consultas y realizar la actualización en SQLite
        UsuarioConsulta usuarioConsulta = TallerRobinautoSQLite.getInstance(getContext()).obtenerUsuarioConsultas();
        boolean actualizadoEnSQLite = usuarioConsulta.actualizarUsuario(usuario);


            // Actualizar usuario en Firebase si SQLite fue exitoso
            UsuarioUtil.actualizarUsuarioEnFirebase(usuario);

            // Verificar si la actualización en Firebase se realizó correctamente
            FirebaseUtil.getDatabaseReference().orderByChild("correo").equalTo(usuario.getCorreo())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Snackbar.make(getView(), "Perfil actualizado correctamente", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(getView(), "Error al editar el perfil", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Snackbar.make(getView(), "Error al editar el perfil", Snackbar.LENGTH_LONG).show();
                        }
                    });

    }

}