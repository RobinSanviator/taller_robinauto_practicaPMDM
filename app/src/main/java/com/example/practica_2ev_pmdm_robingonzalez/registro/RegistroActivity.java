package com.example.practica_2ev_pmdm_robingonzalez.registro;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.practica_2ev_pmdm_robingonzalez.inicio_sesion.InicioSesionActivity;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegistroActivity extends AppCompatActivity {
    TextInputEditText editTextNombre, editTexApellidos, editTextCorreoRegistro, editTextTelefono,
            editTextContrasenyaRegistro, editTextConfirmarContrasenya;
    AutoCompleteTextView spinnerSeleccionarPerfil;
    MaterialButton buttonRegistrarse;
    TextView textViewTextoVolverInicioSesion;
    CheckBox checkBoxUsoServicio, checkBoxPropiedadIntelectual, checkBoxPrivacidad, checkBoxPromociones,
            checkBoxAceptarTodo;
    TallerRobinautoSQLite baseDeDatosGestionUsuarios;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registro_activity);

        inicializarComponentes();
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

    public TallerRobinautoSQLite obtenerInstanciaBaseDeDatos() {
        baseDeDatosGestionUsuarios = new TallerRobinautoSQLite(RegistroActivity.this, "gestion_usuario_taller", null, 5);
        return baseDeDatosGestionUsuarios;
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
        // Crear o usar una instancia de la base de datos
        TallerRobinautoSQLite baseDeDatos = new TallerRobinautoSQLite(this, "gestion_usuario_taller", null, 3);
        // Llamar al método de la base de datos para verificar el correo
        String correoEncontrado = baseDeDatos.correoEnUso(correo);
        // Retornar true si el correo ya está en uso, false si no
        return correoEncontrado != null;
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

        if (editTextContrasenyaRegistro.getText().toString().trim().isEmpty()) {
            textInputLayoutContrasenya.setHelperText("Obligatorio*");
            textInputLayoutContrasenya.setError(null);
            return false;
        } else {
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
                    guardarUsuariosEnBaseDeDatos();
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


    //Este método se llama al aceptar los términos y condiciones del alert dialog
    private void guardarUsuariosEnBaseDeDatos(){

        baseDeDatosGestionUsuarios = obtenerInstanciaBaseDeDatos();
        //Obtener texto de los editText
        String nombre = editTextNombre.getText().toString();
        String apellidos = editTexApellidos.getText().toString();
        String correo = editTextCorreoRegistro.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String contrasenya = editTextContrasenyaRegistro.getText().toString();
        String tipoUsuario = spinnerSeleccionarPerfil.getText().toString();

        //Insertar un nuevo registro
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("nombre", nombre);
        nuevoRegistro.put("apellidos", apellidos);
        nuevoRegistro.put("correo", correo);
        nuevoRegistro.put("telefono", telefono);
        nuevoRegistro.put("contrasenya", contrasenya);
        nuevoRegistro.put("tipo_usuario", tipoUsuario);


        //Leer la base de datos
        SQLiteDatabase baseDeDatosSQLite = baseDeDatosGestionUsuarios.getReadableDatabase();

        long id = baseDeDatosSQLite.insert("usuarios", null, nuevoRegistro);

        if(id != -1){
            startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));

        } else {
            Snackbar.make(buttonRegistrarse, "Error en el registro", Snackbar.LENGTH_LONG).show();
        }

        baseDeDatosGestionUsuarios.close();


    }


}

