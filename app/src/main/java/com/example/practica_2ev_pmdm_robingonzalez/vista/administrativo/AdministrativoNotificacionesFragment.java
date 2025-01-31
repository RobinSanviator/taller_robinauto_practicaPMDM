package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Notificacion;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class AdministrativoNotificacionesFragment extends Fragment {

    private ImageView imageViewVolver;
    private AdministrativoActivity activityAdministrativo;
    private Spinner spinnerClientes;
    private MultiAutoCompleteTextView autoCompleteTextViewEnviarMensaje;
    private MaterialButton buttonEnviarMensaje;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout para las notificaciones
        View vista = inflater.inflate(R.layout.administrativo_notificaciones_fragment, container, false);

        inicializarComponenetes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeNotificacion();
        cargarClientesEnSpinner();
        cargarAutoCompleteTextView();
        enviarMensaje();

        return vista;
    }

    private void inicializarComponenetes(View vista){
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalNotificaciones);
        spinnerClientes = vista.findViewById(R.id.spinnerCargarClientesNotificacion);
        autoCompleteTextViewEnviarMensaje = vista.findViewById(R.id.autoCompleteMensajeNotificacion);
        buttonEnviarMensaje = vista.findViewById(R.id.buttonEnviarMensaje);


    }

    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
        } else {
            Log.e("AdministrativoNotificacionesFragment", "Error al obtener helper");
        }
    }

    private void volverMenuPrincipalDesdeNotificacion(){
        imageViewVolver.setOnClickListener(v -> activityAdministrativo.volverMenuPrincipal());
    }

    private void cargarClientesEnSpinner() {
        UsuarioUtil.cargarUsuariosPorTipo("Cliente", new UsuarioUtil.usuariosCargadosListener() {
            @Override
            public void onUsuariosCargados(List<Usuario> usuarios) {
                // Crear una lista de cadenas en el spinner
                List<String> clientesConFormato = new ArrayList<>();
                for (Usuario usuario : usuarios) {
                    String clienteConFormato = usuario.getNombre() + " " +
                            usuario.getApellidos() +
                            " (" + usuario.getCorreo() + ")";
                    clientesConFormato.add(clienteConFormato);
                }
                // Ahora que tenemos la lista con los clientes formateados, la pasamos al ArrayAdapter
                configurarArrayAdapterCliente(clientesConFormato);

            }

            @Override
            public void onError(Exception e) {
                // Manejar errores, por ejemplo, mostrar un mensaje al usuario
              Log.d("AdministrativoNotificacionesFragment", "Error al cargar spinner de clientes: ");
            }
        });
    }

    private void configurarArrayAdapterCliente(List<String> clientesConFormato){
        // Crear un ArrayAdapter para el Spinner con la lista de clientes
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, clientesConFormato);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Configurar el Spinner con el adaptador
        spinnerClientes.setAdapter(adaptador);
        //Mostrar en un Snacbar el cliente seleccionado para enviar el mensaje
        mostrarMensajeDelClienteSeleccionado();


    }

    private void mostrarMensajeDelClienteSeleccionado(){
        // Establecer un listener para saber qué cliente ha seleccionado el usuario
        spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtener el cliente seleccionado
                String clienteSeleccionado = (String) parentView.getItemAtPosition(position);
                // Mostrar el nombre del cliente seleccionado en un Snackbar
                Snackbar.make(parentView, "Has seleccionado a : " + clienteSeleccionado, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void cargarAutoCompleteTextView() {
        String[] mensajes = getResources().getStringArray(R.array.mensaje_notificacion_administrativo_array);
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, mensajes);

        autoCompleteTextViewEnviarMensaje.setAdapter(adaptador);
        autoCompleteTextViewEnviarMensaje.setThreshold(1); // Mostrar sugerencias desde el primer carácter

        manejarTokenizerParaDelimitarYMostrarOpcionEnMultiAutoCompleteText(autoCompleteTextViewEnviarMensaje);
    }

    private void manejarTokenizerParaDelimitarYMostrarOpcionEnMultiAutoCompleteText(MultiAutoCompleteTextView mactv) {
        mactv.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
            @Override
            public int findTokenStart(CharSequence text, int cursor) {
                int i = cursor;
                while (i > 0 && !isDelimiter(text.charAt(i - 1))) {
                    i--;
                }
                return i;
            }

            @Override
            public int findTokenEnd(CharSequence text, int cursor) {
                int i = cursor;
                while (i < text.length() && !isDelimiter(text.charAt(i))) {
                    i++;
                }
                return i;
            }

            @Override
            public CharSequence terminateToken(CharSequence text) {
                // Retorna el texto
                return text;
            }

            private boolean isDelimiter(char c) {
                return c == ' '; // Usa espacio como delimitador
            }
        });
    }

    private void enviarMensaje(){
        buttonEnviarMensaje.setOnClickListener(v -> crearNotificacion());
    }

    private void crearNotificacion() {
        String clienteSeleccionado = (String) spinnerClientes.getSelectedItem();
        if (clienteSeleccionado == null || clienteSeleccionado.isEmpty()) {
            Snackbar.make(spinnerClientes, "Por favor, seleccione un cliente.", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Extraer el correo del cliente
        String correoReceptor = clienteSeleccionado.substring(clienteSeleccionado.indexOf("(") + 1, clienteSeleccionado.indexOf(")"));
        String mensaje = autoCompleteTextViewEnviarMensaje.getText().toString().trim();

        if (mensaje.isEmpty()) {
            Snackbar.make(autoCompleteTextViewEnviarMensaje, "Por favor, escriba un mensaje.", Snackbar.LENGTH_LONG).show();
            return;
        }

        String correoEmisor = activityAdministrativo.getCorreo(); // Obtener correo del emisor desde la actividad

        // Generar ID único antes de crear la notificación
        DatabaseReference databaseReference = FirebaseUtil.getFirebaseDatabase().getReference("Notificaciones");
        String idNotificacion = databaseReference.push().getKey();

        // Crear el objeto Notificacion con el ID generado
        Notificacion notificacion = new Notificacion(idNotificacion, correoEmisor, correoReceptor, mensaje, null);

        // Guardar la notificación en Firebase
        guardarNotificacionEnFirebase(notificacion, idNotificacion);
    }

    private void guardarNotificacionEnFirebase(Notificacion notificacion, String idNotificacion) {
        // Obtener la instancia de la base de datos
        DatabaseReference databaseReference = FirebaseUtil.getFirebaseDatabase().getReference("Notificaciones");

        if (idNotificacion != null) {
            databaseReference.child(idNotificacion).setValue(notificacion)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(buttonEnviarMensaje, "Mensaje enviado correctamente", Snackbar.LENGTH_LONG).show();
                            autoCompleteTextViewEnviarMensaje.getText().clear(); // Limpiar el campo de texto
                        } else {
                            Snackbar.make(buttonEnviarMensaje, "Error al enviar el mensaje", Snackbar.LENGTH_LONG).show();
                        }
                    });
        } else {
            Snackbar.make(buttonEnviarMensaje, "Error al generar el ID de la notificación", Snackbar.LENGTH_LONG).show();
        }
    }

    }
