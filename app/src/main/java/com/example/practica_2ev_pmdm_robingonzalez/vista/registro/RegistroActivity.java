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

public class RegistroActivity extends AppCompatActivity {
  private  TextInputEditText editTextNombre, editTexApellidos, editTextCorreoRegistro, editTextTelefono,
            editTextContrasenyaRegistro, editTextConfirmarContrasenya;
  private AutoCompleteTextView spinnerSeleccionarPerfil;
  private  MaterialButton buttonRegistrarse;
  private  TextView textViewTextoVolverInicioSesion;
  private  CheckBox checkBoxUsoServicio, checkBoxPropiedadIntelectual, checkBoxPrivacidad, checkBoxPromociones,
            checkBoxAceptarTodo;
  private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
  private UsuarioConsulta usuarioConsulta;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        inicializarComponentes();
        inicializarBaseDeDatos();
        registrarse();
        volverInicioSesion();
        seleccionarPerfil();
        configurarValidacionDinamica();

    }

    private void inicializarComponentes(){
        // Referencias a los elementos del formulario
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

    private void inicializarBaseDeDatos(){
        // Usar el Singleton para obtener la instancia de la base de datos
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(RegistroActivity.this);
        // Obtener la instancia de UsuarioConsultas
        usuarioConsulta = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
    }

    private void registrarse(){
        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarTerminosYCondicionesEnAlertDialog();
            }
        });
    }

    private void volverInicioSesion(){
        textViewTextoVolverInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));
            }
        });
    }

    private void seleccionarPerfil(){
        // Configurar el autocompletetext como spinner para seleccionar el tipo de usuario

        String[] perfiles = getResources().getStringArray(R.array.tipo_usuarios);
        ArrayAdapter<String> adaptadorPerfiles = new ArrayAdapter<>(this, R.layout.lista_tipo_usuario, perfiles);
        spinnerSeleccionarPerfil.setAdapter(adaptadorPerfiles);

        spinnerSeleccionarPerfil.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //Ocultar teclado cuando se foculiza en el spinner
                    InputMethodManager teclado = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    teclado.hideSoftInputFromWindow(spinnerSeleccionarPerfil.getWindowToken(),0);
                    spinnerSeleccionarPerfil.showDropDown();
                }
            }
        });


    }

    private boolean validarNombre(String nombre){
        TextInputLayout textInputLayoutNombreRegistro = findViewById(R.id.textInputNombreLayoutR);

        if(editTextNombre.getText().toString().trim().isEmpty()){

            textInputLayoutNombreRegistro.setHelperText("Obligatorio*");
            return false;

        } else {
            textInputLayoutNombreRegistro.setHelperText(null);
        }
        return true;
    }

    private boolean validarApellidos(String apellidos){

        TextInputLayout textInputLayoutApellidos = findViewById(R.id.textInputApellidosLayoutR);

        if(editTexApellidos.getText().toString().trim().isEmpty()){

            textInputLayoutApellidos.setHelperText("Obligatorio*");
            return false;

        } else {
            textInputLayoutApellidos.setHelperText(null);
        }

        return true;
    }

    private boolean validarCorreo(String correo){
        TextInputLayout textInputLayoutCorreoRegistro = findViewById(R.id.textInputCorreoLayoutR);

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

    private boolean verificarCorreoEnUso(String correo) {

        // Llamar al método de la base de datos para verificar el correo
        boolean correoEncontrado = usuarioConsulta.correoEnUso(correo);
        // Retornar true si el correo ya está en uso, false si no
        return correoEncontrado;
    }

    private boolean validarTelefono(String telefono){
        TextInputLayout textInputLayoutTelefonoRegistro = findViewById(R.id.textInputTelefonoLayoutR);

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

    private boolean validarContrasenyas(String contrasenya, String confirmarContrasenya){
        TextInputLayout textInputLayoutContrasenya = findViewById(R.id.textInputContrasenyaLayoutR);
        TextInputLayout textInputLayoutConfirmarContrasenya = findViewById(R.id.textInputConfirmarContrasenyaLayoutR);

        if (editTextContrasenyaRegistro.getText().toString().trim().isEmpty() || editTextContrasenyaRegistro.length() < 6) {
            textInputLayoutContrasenya.setError("La contraseña debe contener al menos 6 caracteres");
            return false;
        } else {
            textInputLayoutContrasenya.setError(null);
            textInputLayoutContrasenya.setHelperText(null);
        }

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

    private boolean validarSeleccionarPerfil(String perfil){
        TextInputLayout textInputLayoutSeleccionarPerfil = findViewById(R.id.textInputSpinnerLayoutR);

        if(spinnerSeleccionarPerfil.getText().toString().trim().isEmpty()){
            textInputLayoutSeleccionarPerfil.setHelperText("Obligatorio*");
            return false;

        } else {
            textInputLayoutSeleccionarPerfil.setHelperText(null);
        }

        return true;
    }

    // Método para verificar todas las validaciones y habilitar/deshabilitar el botón
    private void verificarValidaciones() {
        String nombre = editTextNombre.getText().toString();
        String apellidos = editTexApellidos.getText().toString();
        String correo = editTextCorreoRegistro.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String contrasenya = editTextContrasenyaRegistro.getText().toString();
        String confirmarContrasenya = editTextConfirmarContrasenya.getText().toString();
        String perfil = spinnerSeleccionarPerfil.getText().toString().trim();

        // Verificar todas las condiciones de validación
        boolean esValido = validarNombre(nombre)
                && validarApellidos(apellidos)
                && validarCorreo(correo)
                && validarTelefono(telefono)
                && validarContrasenyas(contrasenya, confirmarContrasenya)
                && validarSeleccionarPerfil(perfil);

        // Habilitar o deshabilitar el botón según el resultado
        buttonRegistrarse.setEnabled(esValido);

        if(buttonRegistrarse.isEnabled()){
            buttonRegistrarse.setBackgroundColor(getColor(R.color.color_botonIntro));
            buttonRegistrarse.setTextColor(getColor(R.color.blanco));

        } else {
            buttonRegistrarse.setBackgroundColor(getColor(R.color.color_desactivado_fondo));
            buttonRegistrarse.setTextColor(getColor(R.color.color_desactivado_texto));
        }

    }

    private void configurarValidacionDinamica() {
        // Crear un único TextWatcher
        TextWatcher textWatcherCambios = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verificarValidaciones(); // Llamar al método de verificación
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Agregar el TextWatcher a cada campo
        editTextNombre.addTextChangedListener(textWatcherCambios);
        editTexApellidos.addTextChangedListener(textWatcherCambios);
        editTextCorreoRegistro.addTextChangedListener(textWatcherCambios);
        editTextTelefono.addTextChangedListener(textWatcherCambios);
        editTextContrasenyaRegistro.addTextChangedListener(textWatcherCambios);
        editTextConfirmarContrasenya.addTextChangedListener(textWatcherCambios);
        spinnerSeleccionarPerfil.addTextChangedListener(textWatcherCambios);

        // Verificar el estado inicial al configurar
        verificarValidaciones();
    }

    private void mostrarTerminosYCondicionesEnAlertDialog() {

        if (isFinishing()) {
            return; // Evita mostrar el diálogo si la actividad está en proceso de finalización
        }

        LayoutInflater inflaterTerminosCondiciones = getLayoutInflater();
        View vistaDialogo = inflaterTerminosCondiciones.inflate(R.layout.alert_dialog_terminos_condiciones, null);

        AlertDialog.Builder builderTyC = new AlertDialog.Builder(this);
        builderTyC.setTitle("Términos y Condiciones");
        builderTyC.setView(vistaDialogo);

        // Configuración de Checkboxes
        configurarCheckBoxesAlertDialog(vistaDialogo);

        // Botones del diálogo
        builderTyC.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkBoxUsoServicio.isChecked() && checkBoxPropiedadIntelectual.isChecked() && checkBoxPrivacidad.isChecked()) {
                    guardarUsuario();
                    startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));
                    finish();
                } else {
                    Snackbar.make(buttonRegistrarse, "Debes aceptar todos los términos para registrarte", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        builderTyC.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderTyC.show(); // Muestra el diálogo
    }

    private void configurarCheckBoxesAlertDialog(View vistaDialogo) {
        checkBoxUsoServicio = vistaDialogo.findViewById(R.id.checkBoxUsoServicio);
        checkBoxPropiedadIntelectual = vistaDialogo.findViewById(R.id.checkBoxPropiedadIntelectual);
        checkBoxPrivacidad = vistaDialogo.findViewById(R.id.checkBoxPrivacidad);
        checkBoxPromociones = vistaDialogo.findViewById(R.id.checkBoxPromociones);
        checkBoxAceptarTodo = vistaDialogo.findViewById(R.id.checkBoxAceptarTodo);

        checkBoxAceptarTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxUsoServicio.setChecked(isChecked);
                checkBoxPropiedadIntelectual.setChecked(isChecked);
                checkBoxPrivacidad.setChecked(isChecked);
                checkBoxPromociones.setChecked(isChecked);
            }
        });
    }

    private void guardarUsuario() {
        // Obtener texto de los editText
        String nombre = editTextNombre.getText().toString();
        String apellidos = editTexApellidos.getText().toString();
        String correo = editTextCorreoRegistro.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String contrasenya = editTextContrasenyaRegistro.getText().toString();
        String tipoUsuario = spinnerSeleccionarPerfil.getText().toString();

        // Crear un objeto Usuario con los datos obtenidos
        Usuario nuevoUsuario = new Usuario(nombre, apellidos, correo, telefono, contrasenya, tipoUsuario);

        // Guardar en la base de datos local SQLite
        long idUsuario = usuarioConsulta.insertarUsuario(nuevoUsuario);
        Log.d("ID Usuario", "Id usuario" + idUsuario);

        if (idUsuario != -1) {
            // Inserción exitosa en SQLite, ahora guardar en Firebase
            Map<String, Object> mapUsuario = UsuarioUtil.anadirUsuarioFirebase(nuevoUsuario);
            guardarUsuarioEnFirebase(correo, contrasenya, mapUsuario);

        } else {
            // Error al registrar en la base de datos local
            Log.e("Error", "Error al insertar usuario ");
            Snackbar.make(buttonRegistrarse, "Error en el registro", Snackbar.LENGTH_LONG).show();
            recargarInterfaz();
        }
    }

    private void guardarUsuarioEnFirebase(String correo, String contrasenya, Map<String, Object> mapUsuario) {
        // Usar FirebaseUtils para registrar al usuario
        FirebaseUtil.registrarUsuarioConEmailYContrasena(correo, contrasenya, task -> {
            if (task.isSuccessful()) {
                // El usuario fue registrado con éxito, ahora obtenemos el userId
                String userId = FirebaseUtil.obtenerUserId();
                if (userId != null) {
                    // Guardar los datos del usuario en la base de datos
                    FirebaseUtil.guardarUsuarioEnFirebaseDatabase(userId, mapUsuario, dbTask -> {
                        if (dbTask.isSuccessful()) {
                            // Datos guardados correctamente
                            Log.d("Firebase guardarusuario", "Se completó el registro");
                            Snackbar.make(buttonRegistrarse, "Usuario registrado con éxito", Snackbar.LENGTH_LONG).show();
                            // Redirigir a la pantalla de inicio de sesión
                            startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));
                            finish();  // Opcional: para asegurarse de que no se puede volver a esta actividad
                        } else {
                            // Error al guardar los datos del usuario
                            Log.e("Firebase guardarusuario", "Error al guardar datos en Firebase", dbTask.getException());
                            Snackbar.make(buttonRegistrarse, "Error al guardar datos del usuario", Snackbar.LENGTH_LONG).show();
                        }
                    });
                } else {
                    // Error al obtener el ID del usuario
                    Log.e("Firebase", "No se pudo obtener el ID del usuario");
                    Snackbar.make(buttonRegistrarse, "Error en el registro", Snackbar.LENGTH_LONG).show();
                }
            } else {
                // Error en el registro
                Log.e("Firebase", "Error al registrar usuario", task.getException());
                Snackbar.make(buttonRegistrarse, "Error en el registro: " + task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

}

    private void recargarInterfaz(){
        editTextCorreoRegistro.getText().clear();
        editTextContrasenyaRegistro.getText().clear();
        editTextConfirmarContrasenya.getText().clear();
    }


}

