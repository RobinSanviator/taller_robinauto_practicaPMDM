package com.example.practica_2ev_pmdm_robingonzalez.vista.registro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsulta;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.vista.inicio_sesion.InicioSesionActivity;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;

/**
 * Actividad que gestiona el proceso de registro de un nuevo usuario.
 * Incluye la validación de campos, la selección de perfil y la conexión a la base de datos.
 */
public class RegistroActivity extends AppCompatActivity {
    // Elementos de la interfaz de usuario para el formulario de registro
    private TextInputEditText editTextNombre, editTexApellidos, editTextCorreoRegistro, editTextTelefono,
            editTextContrasenyaRegistro, editTextConfirmarContrasenya;
    private AutoCompleteTextView spinnerSeleccionarPerfil;
    private MaterialButton buttonRegistrarse;
    private TextView textViewTextoVolverInicioSesion;
    private CheckBox checkBoxUsoServicio, checkBoxPropiedadIntelectual, checkBoxPrivacidad, checkBoxPromociones,
            checkBoxAceptarTodo;
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
    private UsuarioConsulta usuarioConsulta;

    /**
     * Método que se ejecuta cuando la actividad es creada. Inicializa la interfaz y los componentes.
     *
     * @param savedInstanceState Estado guardado de la actividad (si aplica).
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Activa la funcionalidad EdgeToEdge para la interfaz.
        EdgeToEdge.enable(this);
        // Establece el layout de la actividad.
        setContentView(R.layout.registro_activity);
        // Ajusta el padding de la vista para que se vea correctamente con las barras del sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa los componentes y los servicios necesarios.
        inicializarComponentes();
        inicializarBaseDeDatos();
        registrarse();
        volverInicioSesion();
        seleccionarPerfil();
        configurarValidacionDinamica();
    }

    /**
     * Método para inicializar los componentes de la interfaz.
     */
    private void inicializarComponentes() {
        // Referencias a los elementos del formulario de registro
        editTextNombre = findViewById(R.id.editTextNombreRegistro);
        editTexApellidos = findViewById(R.id.editTextApellidosRegistro);
        editTextCorreoRegistro = findViewById(R.id.editTextCorreoRegistro);
        editTextContrasenyaRegistro = findViewById(R.id.editTextContrasenyaRegistro);
        editTextTelefono = findViewById(R.id.editTelefonoRegistro);
        editTextConfirmarContrasenya = findViewById(R.id.editTextConfirmarContrasenyaRegistro);
        spinnerSeleccionarPerfil = findViewById(R.id.spinnerSeleccionarPerfil);
        buttonRegistrarse = findViewById(R.id.buttonRegistrarse);
        textViewTextoVolverInicioSesion = findViewById(R.id.textViewVolverInicioSesion);
    }

    /**
     * Método para inicializar la conexión con la base de datos.
     */
    private void inicializarBaseDeDatos() {
        // Usar el patrón Singleton para obtener la instancia de la base de datos
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(RegistroActivity.this);
        // Obtener la instancia de UsuarioConsultas para gestionar usuarios en la base de datos
        usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
    }

    /**
     * Método para gestionar el evento de registro al pulsar el botón "Registrarse".
     */
    private void registrarse() {
        buttonRegistrarse.setOnClickListener(v -> mostrarTerminosYCondicionesEnAlertDialog());
    }

    /**
     * Método que gestiona el evento de volver a la pantalla de inicio de sesión.
     */
    private void volverInicioSesion() {
        textViewTextoVolverInicioSesion.setOnClickListener(v -> startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class)));
    }

    /**
     * Método para configurar el selector de perfil (spinner) con los tipos de usuario.
     */
    private void seleccionarPerfil() {
        // Configura el AutoCompleteTextView como un spinner para seleccionar el tipo de usuario
        String[] perfiles = getResources().getStringArray(R.array.tipo_usuarios);
        ArrayAdapter<String> adaptadorPerfiles = new ArrayAdapter<>(this, R.layout.lista_tipo_usuario, perfiles);
        spinnerSeleccionarPerfil.setAdapter(adaptadorPerfiles);

        // Oculta el teclado cuando se enfoca el spinner y muestra las opciones disponibles
        spinnerSeleccionarPerfil.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager teclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                teclado.hideSoftInputFromWindow(spinnerSeleccionarPerfil.getWindowToken(), 0);
                spinnerSeleccionarPerfil.showDropDown();
            }
        });
    }

    /**
     * Método para validar el nombre del usuario.
     *
     * @param nombre El nombre a validar.
     * @return true si el nombre es válido, false si es inválido.
     */
    private boolean validarNombre(String nombre) {
        TextInputLayout textInputLayoutNombreRegistro = findViewById(R.id.textInputNombreLayoutR);

        // Verifica si el campo nombre está vacío
        if (editTextNombre.getText().toString().trim().isEmpty()) {
            textInputLayoutNombreRegistro.setHelperText("Obligatorio*");
            return false;
        } else {
            textInputLayoutNombreRegistro.setHelperText(null);
        }
        return true;
    }

    /**
     * Método para validar los apellidos del usuario.
     *
     * @param apellidos Los apellidos a validar.
     * @return true si los apellidos son válidos, false si son inválidos.
     */
    private boolean validarApellidos(String apellidos) {
        TextInputLayout textInputLayoutApellidos = findViewById(R.id.textInputApellidosLayoutR);

        // Verifica si el campo apellidos está vacío
        if (editTexApellidos.getText().toString().trim().isEmpty()) {
            textInputLayoutApellidos.setHelperText("Obligatorio*");
            return false;
        } else {
            textInputLayoutApellidos.setHelperText(null);
        }
        return true;
    }

    /**
     * Método para validar el correo electrónico del usuario.
     *
     * @param correo El correo a validar.
     * @return true si el correo es válido, false si es inválido.
     */
    private boolean validarCorreo(String correo) {
        TextInputLayout textInputLayoutCorreoRegistro = findViewById(R.id.textInputCorreoLayoutR);

        // Verifica si el campo correo está vacío o no tiene el formato correcto
        if (editTextCorreoRegistro.getText().toString().trim().isEmpty()) {
            textInputLayoutCorreoRegistro.setHelperText("Obligatorio*");
            textInputLayoutCorreoRegistro.setError(null);
            return false;
        } else if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {
            textInputLayoutCorreoRegistro.setError("Correo electrónico inválido");
            textInputLayoutCorreoRegistro.setHelperText(null);
            return false;
        } else if (verificarCorreoEnUso(correo)) {
            textInputLayoutCorreoRegistro.setError("Correo electrónico en uso");
            textInputLayoutCorreoRegistro.setHelperText(null);
            return false;
        } else {
            textInputLayoutCorreoRegistro.setError(null);
            textInputLayoutCorreoRegistro.setHelperText(null);
        }
        return true;
    }

    /**
     * Método para verificar si el correo electrónico ya está en uso.
     *
     * @param correo El correo a verificar.
     * @return true si el correo ya está en uso, false si no.
     */
    private boolean verificarCorreoEnUso(String correo) {
        return usuarioConsulta.correoEnUso(correo);
    }

    /**
     * Método para validar el número de teléfono del usuario.
     *
     * @param telefono El teléfono a validar.
     * @return true si el teléfono es válido, false si es inválido.
     */
    private boolean validarTelefono(String telefono) {
        TextInputLayout textInputLayoutTelefonoRegistro = findViewById(R.id.textInputTelefonoLayoutR);

        // Verifica si el campo teléfono está vacío o no tiene el formato correcto
        if (editTextTelefono.getText().toString().trim().isEmpty()) {
            textInputLayoutTelefonoRegistro.setHelperText("Obligatorio*");
            textInputLayoutTelefonoRegistro.setError(null);
            return false;
        } else if (!telefono.matches("^[0-9]{9}$")) {
            textInputLayoutTelefonoRegistro.setError("El teléfono debe tener 9 dígitos");
            textInputLayoutTelefonoRegistro.setHelperText(null);
            return false;
        } else {
            textInputLayoutTelefonoRegistro.setError(null);
            textInputLayoutTelefonoRegistro.setHelperText(null);
        }
        return true;
    }

    /**
     * Valida las contraseñas introducidas por el usuario.
     *
     * @param contrasenya          La contraseña introducida por el usuario.
     * @param confirmarContrasenya La contraseña confirmada por el usuario.
     * @return true si las contraseñas son válidas, false si alguna de las validaciones falla.
     */
    private boolean validarContrasenyas(String contrasenya, String confirmarContrasenya) {
        TextInputLayout textInputLayoutContrasenya = findViewById(R.id.textInputContrasenyaLayoutR);
        TextInputLayout textInputLayoutConfirmarContrasenya = findViewById(R.id.textInputConfirmarContrasenyaLayoutR);

        // Verifica que la contraseña tenga al menos 6 caracteres
        if (editTextContrasenyaRegistro.getText().toString().trim().isEmpty() || editTextContrasenyaRegistro.length() < 6) {
            textInputLayoutContrasenya.setError("La contraseña debe contener al menos 6 caracteres");
            return false;
        } else {
            textInputLayoutContrasenya.setError(null);
            textInputLayoutContrasenya.setHelperText(null);
        }

        // Verifica que el campo de confirmar contraseña no esté vacío y que coincida con la contraseña original
        if (editTextConfirmarContrasenya.getText().toString().trim().isEmpty()) {
            textInputLayoutConfirmarContrasenya.setError("Obligatorio*");
            textInputLayoutConfirmarContrasenya.setHelperText(null);
            return false;
        } else if (!contrasenya.equals(confirmarContrasenya)) {
            textInputLayoutConfirmarContrasenya.setError("La contraseña no coincide");
            textInputLayoutConfirmarContrasenya.setHelperText(null);
            return false;
        } else {
            textInputLayoutConfirmarContrasenya.setError(null);
            textInputLayoutConfirmarContrasenya.setHelperText(null);
        }

        return true;
    }

    /**
     * Valida que el perfil seleccionado no esté vacío.
     *
     * @param perfil El perfil seleccionado en el Spinner.
     * @return true si el perfil es válido, false si el perfil está vacío.
     */
    private boolean validarSeleccionarPerfil(String perfil) {
        TextInputLayout textInputLayoutSeleccionarPerfil = findViewById(R.id.textInputSpinnerLayoutR);

        // Verifica si el perfil está vacío y muestra un mensaje de error
        if (spinnerSeleccionarPerfil.getText().toString().trim().isEmpty()) {
            textInputLayoutSeleccionarPerfil.setHelperText("Obligatorio*");
            return false;
        } else {
            textInputLayoutSeleccionarPerfil.setHelperText(null);
        }

        return true;
    }

    /**
     * Verifica todas las validaciones de los campos de entrada y habilita o deshabilita el botón de registro.
     * <p>
     * Este método se asegura de que todos los campos sean válidos antes de permitir que el usuario se registre.
     */
    private void verificarValidaciones() {
        String nombre = editTextNombre.getText().toString();
        String apellidos = editTexApellidos.getText().toString();
        String correo = editTextCorreoRegistro.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String contrasenya = editTextContrasenyaRegistro.getText().toString();
        String confirmarContrasenya = editTextConfirmarContrasenya.getText().toString();
        String perfil = spinnerSeleccionarPerfil.getText().toString().trim();

        // Verifica todas las condiciones de validación
        boolean esValido = validarNombre(nombre)
                && validarApellidos(apellidos)
                && validarCorreo(correo)
                && validarTelefono(telefono)
                && validarContrasenyas(contrasenya, confirmarContrasenya)
                && validarSeleccionarPerfil(perfil);

        // Habilita o deshabilita el botón de registro según el resultado de la validación
        buttonRegistrarse.setEnabled(esValido);

        // Cambia el color del botón según su estado de habilitación
        if (buttonRegistrarse.isEnabled()) {
            buttonRegistrarse.setBackgroundColor(getColor(R.color.color_botonIntro));
            buttonRegistrarse.setTextColor(getColor(R.color.blanco));
        } else {
            buttonRegistrarse.setBackgroundColor(getColor(R.color.color_desactivado_fondo));
            buttonRegistrarse.setTextColor(getColor(R.color.color_desactivado_texto));
        }
    }

    /**
     * Configura la validación dinámica de los campos de texto.
     * <p>
     * Este método agrega un TextWatcher a cada campo para verificar la validez de los datos a medida que el usuario los ingresa.
     */
    private void configurarValidacionDinamica() {
        // Crear un único TextWatcher que será agregado a todos los campos
        TextWatcher textWatcherCambios = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verificarValidaciones(); // Llama al método que verifica las validaciones
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        // Agregar el TextWatcher a cada campo de entrada
        editTextNombre.addTextChangedListener(textWatcherCambios);
        editTexApellidos.addTextChangedListener(textWatcherCambios);
        editTextCorreoRegistro.addTextChangedListener(textWatcherCambios);
        editTextTelefono.addTextChangedListener(textWatcherCambios);
        editTextContrasenyaRegistro.addTextChangedListener(textWatcherCambios);
        editTextConfirmarContrasenya.addTextChangedListener(textWatcherCambios);
        spinnerSeleccionarPerfil.addTextChangedListener(textWatcherCambios);

        // Verificar las validaciones al configurar
        verificarValidaciones();
    }

    /**
     * Muestra un diálogo de Términos y Condiciones antes de completar el registro.
     * <p>
     * Este método muestra un diálogo con los términos y condiciones y verifica si el usuario acepta los términos.
     */
    private void mostrarTerminosYCondicionesEnAlertDialog() {

        if (isFinishing()) {
            return; // Evita mostrar el diálogo si la actividad está en proceso de finalización
        }

        LayoutInflater inflaterTerminosCondiciones = getLayoutInflater();
        View vistaDialogo = inflaterTerminosCondiciones.inflate(R.layout.alert_dialog_terminos_condiciones, null);

        // Crear el diálogo y configurarlo
        AlertDialog.Builder builderTyC = new AlertDialog.Builder(this);
        builderTyC.setTitle("Términos y Condiciones");
        builderTyC.setIcon(R.drawable.ic_terminos_condiciones);
        builderTyC.setView(vistaDialogo);

        // Configurar los checkboxes en el diálogo
        configurarCheckBoxesAlertDialog(vistaDialogo);

        // Botones del diálogo
        builderTyC.setPositiveButton("Aceptar", (dialog, which) -> {
            if (checkBoxUsoServicio.isChecked() && checkBoxPropiedadIntelectual.isChecked() && checkBoxPrivacidad.isChecked()) {
                guardarUsuario();
                startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));
                finish();
            } else {
                Snackbar.make(buttonRegistrarse, "Debes aceptar todos los términos para registrarte", Snackbar.LENGTH_SHORT).show();
            }
        });

        builderTyC.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builderTyC.show(); // Muestra el diálogo
    }

    /**
     * Configura los checkboxes de los términos y condiciones en el diálogo.
     * <p>
     * Este método configura el comportamiento de los checkboxes en el diálogo de términos y condiciones.
     */
    private void configurarCheckBoxesAlertDialog(View vistaDialogo) {
        checkBoxUsoServicio = vistaDialogo.findViewById(R.id.checkBoxUsoServicio);
        checkBoxPropiedadIntelectual = vistaDialogo.findViewById(R.id.checkBoxPropiedadIntelectual);
        checkBoxPrivacidad = vistaDialogo.findViewById(R.id.checkBoxPrivacidad);
        checkBoxPromociones = vistaDialogo.findViewById(R.id.checkBoxPromociones);
        checkBoxAceptarTodo = vistaDialogo.findViewById(R.id.checkBoxAceptarTodo);

        // Si el usuario marca "Aceptar todo", se marcan todos los checkboxes
        checkBoxAceptarTodo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkBoxUsoServicio.setChecked(isChecked);
            checkBoxPropiedadIntelectual.setChecked(isChecked);
            checkBoxPrivacidad.setChecked(isChecked);
            checkBoxPromociones.setChecked(isChecked);
        });
    }

    /**
     * Guarda los datos del nuevo usuario en la base de datos local (SQLite) y en Firebase.
     * <p>
     * Este método crea un nuevo objeto de usuario, lo guarda en SQLite y luego lo sube a Firebase.
     */
    private void guardarUsuario() {
        // Obtener los datos de los campos
        String nombre = editTextNombre.getText().toString();
        String apellidos = editTexApellidos.getText().toString();
        String correo = editTextCorreoRegistro.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String contrasenya = editTextContrasenyaRegistro.getText().toString();
        String tipoUsuario = spinnerSeleccionarPerfil.getText().toString();

        // Crear un objeto Usuario con los datos obtenidos
        Usuario nuevoUsuario = new Usuario(nombre, apellidos, correo, telefono, contrasenya, tipoUsuario);

        // Guardar en SQLite y Firebase
        long idUsuario = usuarioConsulta.insertarUsuario(nuevoUsuario);
        Log.d("ID Usuario", "Id usuario" + idUsuario);

        if (idUsuario != -1) {
            // Guardar en Firebase
            Map<String, Object> mapUsuario = UsuarioUtil.anadirUsuarioFirebase(nuevoUsuario);
            guardarUsuarioEnFirebase(correo, contrasenya, mapUsuario);
        } else {
            Log.e("Error", "Error al insertar usuario ");
            Snackbar.make(buttonRegistrarse, "Error en el registro", Snackbar.LENGTH_LONG).show();
            recargarInterfaz();
        }
    }

    /**
     * Guarda los datos del usuario en Firebase después de su registro exitoso.
     *
     * @param correo      El correo electrónico del usuario.
     * @param contrasenya La contraseña del usuario.
     * @param mapUsuario  Los datos del usuario a guardar en Firebase.
     */
    private void guardarUsuarioEnFirebase(String correo, String contrasenya, Map<String, Object> mapUsuario) {
        // Usar FirebaseUtils para registrar al usuario en Firebase
        FirebaseUtil.registrarUsuarioConEmailYContrasena(correo, contrasenya, task -> {
            if (task.isSuccessful()) {
                String userId = FirebaseUtil.obtenerUserId();
                if (userId != null) {
                    FirebaseUtil.guardarUsuarioEnFirebaseDatabase(userId, mapUsuario, dbTask -> {
                        if (dbTask.isSuccessful()) {
                            Log.d("Firebase guardarusuario", "Se completó el registro");
                            Snackbar.make(buttonRegistrarse, "Usuario registrado con éxito", Snackbar.LENGTH_LONG).show();
                            startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));
                            finish();
                        } else {
                            Log.e("Firebase guardarusuario", "Error al guardar datos en Firebase", dbTask.getException());
                            Snackbar.make(buttonRegistrarse, "Error al guardar datos del usuario", Snackbar.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Log.e("Firebase", "No se pudo obtener el ID del usuario");
                    Snackbar.make(buttonRegistrarse, "Error en el registro", Snackbar.LENGTH_LONG).show();
                }
            } else {
                Log.e("Firebase", "Error al registrar usuario", task.getException());
                Snackbar.make(buttonRegistrarse, "Error en el registro: " + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Recarga la interfaz de usuario después de un error en el registro.
     * Este método limpia los campos de correo y contraseña para que el usuario pueda intentar registrarse nuevamente.
     */
    private void recargarInterfaz() {
        editTextCorreoRegistro.getText().clear();
        editTextContrasenyaRegistro.getText().clear();
        editTextConfirmarContrasenya.getText().clear();
    }
}

