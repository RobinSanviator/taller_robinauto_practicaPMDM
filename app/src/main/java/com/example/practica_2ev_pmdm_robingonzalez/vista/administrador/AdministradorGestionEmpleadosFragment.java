package com.example.practica_2ev_pmdm_robingonzalez.vista.administrador;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.UsuarioEmpleadoAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento que gestiona la lista de empleados (Administrativos, Mecánicos Jefes, Mecánicos).
 * Permite mostrar, agregar o modificar empleados en la aplicación.
 */
public class AdministradorGestionEmpleadosFragment extends Fragment {

    private ImageView imageViewVolver;  // Imagen para volver al menú principal
    private ImageView imageViewAdministrativos, imageViewMecanicosJefes, imageViewMecanicos;  // Imágenes para seleccionar tipo de empleado
    private FloatingActionButton fabDarDeAlta;  // Botón flotante para añadir un nuevo empleado
    private RecyclerView recyclerViewUsuarios;  // RecyclerView para mostrar la lista de usuarios
    private AdministradorActivity activityAdministrador;  // Actividad principal del administrador
    private List<Usuario> usuariosList;  // Lista de usuarios a mostrar
    private UsuarioEmpleadoAdapter usuariosEmpleadoAdapter;  // Adaptador para el RecyclerView
    private TextInputLayout layoutNombre, layoutApellidos,
            layoutCorreo, layoutTelefono, layoutContrasenya;  // Campos del formulario de alta
    private EditText editTextNombreEmpleado, editTextApellidosEmpleado,
            editTextCorreoEmpleado, editTextTelefonoEmpleado, editTextContrasenyaEmpleado;  // EditText para los campos del formulario
    private Spinner spinnerTipoUsuarioEmpleado;  // Spinner para seleccionar el tipo de usuario
    private String tipoEmpleadoActual;  // Tipo de empleado seleccionado
    private TextView textViewMostrarListaEmpleado;  // Texto para mostrar el tipo de empleados en la lista

    /**
     * Se llama cuando el fragmento es creado. No es necesario hacer nada aquí.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Se llama para inflar la vista del fragmento.
     * Aquí se inicializan los componentes visuales y se configuran las interacciones.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout de la gestión de empleados
        View vista = inflater.inflate(R.layout.administrador_gestion_empleados_fragment, container, false);

        inicializarComponentes(vista);  // Inicializa las vistas
        obtenerHelper();  // Obtiene la actividad principal
        verificarConexion();  // Verifica la conexión a Internet
        volverMenuPrincipalDesdeEmpleados();  // Configura el botón para volver al menú principal
        configurarRecyclerView();  // Configura el RecyclerView
        configurarListeners();  // Configura los listeners de los botones
        anadirEmpleado();  // Configura la acción para añadir un empleado
        seleccionPorDefecto();  // Establece el tipo de empleado por defecto

        return vista;  // Retorna la vista inflada
    }

    /**
     * Inicializa los componentes visuales del fragmento.
     * Se obtiene la referencia a las vistas (botones, RecyclerView, etc.).
     */
    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalDesdeEmpleados);
        imageViewAdministrativos = vista.findViewById(R.id.imageViewVolverAltaBajaAdministrativos);
        imageViewMecanicosJefes = vista.findViewById(R.id.imageViewVolverAltaBajaMjefes);
        imageViewMecanicos = vista.findViewById(R.id.imageViewVolverAltaBajaMecanicos);
        fabDarDeAlta = vista.findViewById(R.id.floatingBotonDarDeAlta);
        recyclerViewUsuarios = vista.findViewById(R.id.recyclerViewListaUsuariosAltaBaja);
        textViewMostrarListaEmpleado = vista.findViewById(R.id.textViewMostrarListaEmpleados);
    }

    /**
     * Obtiene la instancia de la actividad principal del administrador.
     * Asegura que la actividad sea una instancia de `AdministradorActivity`.
     */
    private void obtenerHelper() {
        if (getActivity() instanceof AdministradorActivity) {
            activityAdministrador = (AdministradorActivity) getActivity();  // Obtiene la actividad principal
        } else {
            Log.e("AdministradorGestionEmpleadosFragment", "Error al obtener helper");
        }
    }

    /**
     * Verifica la conexión a Internet. Si hay conexión, carga los usuarios de tipo "Administrativo".
     * Si no hay conexión, muestra un mensaje de error.
     */
    private void verificarConexion() {
        if (hayConexionInternet()) {
            cargarUsuarios("Administrativo");  // Cargar usuarios del tipo Administrativo
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    "No tienes conexión a Internet. Conéctate para ver las listas de empleados",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Configura la acción del botón para volver al menú principal.
     */
    private void volverMenuPrincipalDesdeEmpleados() {
        imageViewVolver.setOnClickListener(v -> activityAdministrador.volverMenuPrincipal());
    }

    /**
     * Configura el RecyclerView para mostrar la lista de usuarios.
     */
    private void configurarRecyclerView() {
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));  // Configura el LayoutManager
        usuariosList = new ArrayList<>();  // Crea la lista de usuarios
        usuariosEmpleadoAdapter = new UsuarioEmpleadoAdapter(usuariosList, getContext());  // Crea el adaptador
        recyclerViewUsuarios.setAdapter(usuariosEmpleadoAdapter);  // Asocia el adaptador al RecyclerView
    }

    /**
     * Configura los listeners para las imágenes de selección de tipo de empleado.
     */
    private void configurarListeners() {
        configurarClickListener(imageViewAdministrativos, "Administrativo");
        configurarClickListener(imageViewMecanicosJefes, "Mecanico jefe");
        configurarClickListener(imageViewMecanicos, "Mecanico");
    }

    /**
     * Configura el listener para una imagen de selección de tipo de usuario.
     * Actualiza el tipo de empleado y carga los usuarios correspondientes.
     */
    private void configurarClickListener(ImageView imageView, String tipoUsuario) {
        imageView.setOnClickListener(v -> {
            resetearFondo();  // Restablecer el fondo de todas las imágenes

            tipoEmpleadoActual = tipoUsuario;  // Establecer el tipo de empleado seleccionado
            textViewMostrarListaEmpleado.setText("Lista empleado " + tipoUsuario);  // Actualizar el texto de la lista

            // Poner el fondo verde a la imagen seleccionada
            imageView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.verde));

            if (hayConexionInternet()) {
                cargarUsuarios(tipoUsuario);  // Cargar usuarios de acuerdo al tipo seleccionado
            } else {
                Snackbar.make(v, "No tienes conexión a Internet. Conéctate para ver las listas de empleados", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Establece el tipo de empleado por defecto como "Administrativo".
     * Carga los usuarios de tipo "Administrativo" si hay conexión a Internet.
     */
    private void seleccionPorDefecto() {
        imageViewAdministrativos.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.verde));
        textViewMostrarListaEmpleado.setText("Lista empleado Administrativo");

        if (hayConexionInternet()) {
            cargarUsuarios("Administrativo");  // Cargar usuarios administrativos por defecto
        }
    }

    /**
     * Restablece el fondo de todas las imágenes de selección de tipo de empleado a su estado original.
     */
    private void resetearFondo() {
        imageViewAdministrativos.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.fondo_boton_redondo));
        imageViewMecanicosJefes.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.fondo_boton_redondo));
        imageViewMecanicos.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.fondo_boton_redondo));
    }

    /**
     * Carga los usuarios de un tipo específico desde la base de datos.
     * Actualiza el RecyclerView con los usuarios obtenidos.
     */
    private void cargarUsuarios(String tipoUsuario) {
        UsuarioUtil.cargarUsuariosPorTipo(tipoUsuario, new UsuarioUtil.usuariosCargadosListener() {
            @Override
            public void onUsuariosCargados(List<Usuario> usuarios) {
                usuariosList.clear();  // Limpiar la lista actual
                usuariosList.addAll(usuarios);  // Añadir los usuarios cargados
                usuariosEmpleadoAdapter.notifyDataSetChanged();  // Notificar que los datos han cambiado
            }

            @Override
            public void onError(Exception e) {
                Log.e("AdministradorGestionEmpleadosFragment", "Error al cargar los usuarios");
            }
        });
    }

    /**
     * Configura el botón de añadir un empleado para que muestre el formulario de alta.
     */
    private void anadirEmpleado() {
        fabDarDeAlta.setOnClickListener(v -> mostrarFormularioDeAlta(tipoEmpleadoActual));
    }

    /**
     * Muestra el formulario de alta de un nuevo empleado.
     * Permite seleccionar el tipo de usuario y llenar los campos del formulario.
     */
    private void mostrarFormularioDeAlta(String tipoEmpleadoActual) {
        if (tipoEmpleadoActual == null || tipoEmpleadoActual.isEmpty()) {
            tipoEmpleadoActual = "Administrativo";  // Asignar un valor predeterminado
        }

        // Crear el formulario de alta con un cuadro de diálogo
        AlertDialog.Builder builderFormulario = new AlertDialog.Builder(requireContext());
        builderFormulario.setTitle("Añadir empleado " + tipoEmpleadoActual.toLowerCase())
                .setIcon(R.drawable.logo_app_robinauto);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View formularioDialogView = inflater.inflate(R.layout.administrador_formulario_alta_dialog, null);
        builderFormulario.setView(formularioDialogView);

        inflarVistaElementos(formularioDialogView);  // Inflar los campos del formulario
        configurarSpinnerTipoUsuario(spinnerTipoUsuarioEmpleado, tipoEmpleadoActual);  // Configurar el spinner

        builderFormulario.setPositiveButton("Guardar", (dialog, which) -> obtenerDatosFormularioYGuardar());
        builderFormulario.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Has cancelado la alta del empleado", Snackbar.LENGTH_SHORT).show();
        });

        builderFormulario.create().show();  // Mostrar el cuadro de diálogo
    }

    /**
     * Configura el spinner para seleccionar el tipo de usuario (empleado).
     * El tipo de usuario actual está deshabilitado y se selecciona por defecto.
     */
    private void configurarSpinnerTipoUsuario(Spinner spinner, String tipoEmpleadoActual) {
        String[] perfiles = getResources().getStringArray(R.array.tipo_empleados);
        ArrayAdapter<String> adaptadorPerfiles = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, perfiles);
        adaptadorPerfiles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptadorPerfiles);

        // Seleccionar tipo actual y deshabilitar el Spinner
        int posicionActual = adaptadorPerfiles.getPosition(tipoEmpleadoActual);
        spinner.setSelection(posicionActual);
        spinner.setEnabled(false);
    }


    /**
     * Infla los elementos de la vista del formulario de alta de empleado.
     * Asocia las vistas de los campos del formulario con sus respectivos elementos de la interfaz.
     */
    private void inflarVistaElementos(View formularioDialogView) {
        // Asocia los campos del formulario con los elementos visuales correspondientes
        editTextNombreEmpleado = formularioDialogView.findViewById(R.id.editTextNombreEmpleado);
        editTextApellidosEmpleado = formularioDialogView.findViewById(R.id.editTextApellidosEmpleado);
        editTextCorreoEmpleado = formularioDialogView.findViewById(R.id.editTextCorreoEmpleado);
        editTextTelefonoEmpleado = formularioDialogView.findViewById(R.id.editTextTelefonoEmpleado);
        editTextContrasenyaEmpleado = formularioDialogView.findViewById(R.id.editTextContrasenyaEmpleado);
        spinnerTipoUsuarioEmpleado = formularioDialogView.findViewById(R.id.spinnerTipoUsuarioEmpleado);
        layoutNombre = formularioDialogView.findViewById(R.id.textInputLayoutNombreEmpleado);
        layoutApellidos = formularioDialogView.findViewById(R.id.textInputLayoutApellidosEmpleado);
        layoutCorreo = formularioDialogView.findViewById(R.id.textInputLayoutCorreoEmpleado);
        layoutTelefono = formularioDialogView.findViewById(R.id.textInputLayoutTelefonoEmpleado);
        layoutContrasenya = formularioDialogView.findViewById(R.id.textInputLayoutContrasenyaEmpleado);
        // Asigna las validaciones para cada campo
        asignarValidaciones();
    }

    /**
     * Asigna las validaciones necesarias a cada campo del formulario.
     * Se configura un `TextWatcher` para cada campo de entrada que valida su contenido.
     */
    private void asignarValidaciones() {
        // Asigna validaciones a cada campo para mostrar errores cuando corresponda
        editTextNombreEmpleado.addTextChangedListener(crearTextWatcher(layoutNombre, "El nombre es obligatorio"));
        editTextApellidosEmpleado.addTextChangedListener(crearTextWatcher(layoutApellidos, "Los apellidos son obligatorios"));
        editTextCorreoEmpleado.addTextChangedListener(crearTextWatcher(layoutCorreo, "Correo electrónico inválido, 'ejemplo@ejemplo.com'"));
        editTextTelefonoEmpleado.addTextChangedListener(crearTextWatcher(layoutTelefono, "Número de teléfono debe tener 9 dígitos"));
        editTextContrasenyaEmpleado.addTextChangedListener(crearTextWatcher(layoutContrasenya, "La contraseña debe tener al menos 6 caracteres"));
    }

    /**
     * Crea un `TextWatcher` para validar cada campo de texto.
     * El `TextWatcher` se asegura de que el contenido del campo sea válido y muestra un mensaje de error si no lo es.
     */
    private TextWatcher crearTextWatcher(TextInputLayout layout, String errorMessage) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No se necesita ninguna acción antes de que el texto cambie
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Valida el texto en el campo
                if (charSequence.toString().trim().isEmpty()) {
                    layout.setError(errorMessage);  // Muestra el error si el campo está vacío
                } else {
                    layout.setError(null);  // Limpia el error cuando el campo tiene contenido válido
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No se necesita ninguna acción después de que el texto cambie
            }
        };
    }

    /**
     * Obtiene los datos del formulario, valida que sean correctos y, si lo son, guarda el nuevo empleado.
     * Si los datos no son válidos, muestra mensajes de error.
     */
    private void obtenerDatosFormularioYGuardar() {
        // Obtener los datos de los campos del formulario
        String nombre = editTextNombreEmpleado.getText().toString().trim();
        String apellidos = editTextApellidosEmpleado.getText().toString().trim();
        String correo = editTextCorreoEmpleado.getText().toString().trim();
        String telefono = editTextTelefonoEmpleado.getText().toString().trim();
        String contrasenya = editTextContrasenyaEmpleado.getText().toString().trim();
        String tipoUsuario = tipoEmpleadoActual;

        // Validar los campos
        boolean camposValidos = true;

        // Validación de nombre
        if (nombre.isEmpty()) {
            layoutNombre.setError("El nombre es obligatorio");
            camposValidos = false;
        } else {
            layoutNombre.setError(null);
        }

        // Validación de apellidos
        if (apellidos.isEmpty()) {
            layoutApellidos.setError("Los apellidos son obligatorios");
            camposValidos = false;
        } else {
            layoutApellidos.setError(null);
        }

        // Validación de correo electrónico
        if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {
            layoutCorreo.setError("Correo electrónico inválido, 'ejemplo@ejemplo.com'");
            camposValidos = false;
        } else {
            layoutCorreo.setError(null);
        }

        // Validación de teléfono
        if (telefono.length() != 9) {
            layoutTelefono.setError("Número de teléfono debe tener 9 dígitos");
            camposValidos = false;
        } else {
            layoutTelefono.setError(null);
        }

        // Validación de contraseña
        if (contrasenya.isEmpty() || contrasenya.length() < 6) {
            layoutContrasenya.setError("La contraseña debe tener al menos 6 caracteres");
            camposValidos = false;
        } else {
            layoutContrasenya.setError(null);
        }

        // Si todos los campos son válidos, guardar el empleado
        if (camposValidos) {
            // Llamar al método para guardar los datos del empleado en Firebase
            UsuarioUtil.guardarEmpleadoEnFirebase(getContext(), nombre, apellidos, correo, telefono, contrasenya, tipoUsuario);
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Se ha guardado el empleado correctamente", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Por favor, corrige los errores antes de guardar", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Verifica si hay conexión a Internet.
     * Retorna `true` si hay conexión, de lo contrario, retorna `false`.
     */
    private boolean hayConexionInternet() {
        // Obtiene el servicio de conectividad y verifica si hay una conexión activa
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();  // Retorna si la red está conectada
        }
        return false;
    }
}
