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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.practica_2ev_pmdm_robingonzalez.inicio_sesion.InicioSesionActivity;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);



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


        volverInicioSesion();
        seleccionarPerfil();
        configurarValidacionDinamica();

            //Cuando se haga clic en registrarse se mostrarán los términos y condiciones
            buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mostrarTerminosYCondicionesEnAlertDialog();
                }
            });

    }


    public void volverInicioSesion(){
        textViewTextoVolverInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));
            }
        });
    }

    public void seleccionarPerfil(){
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

    public boolean validarNombre(String nombre){
        TextInputLayout textInputLayoutNombreRegistro = findViewById(R.id.textInputNombreLayoutR);

        if(editTextNombre.getText().toString().trim().isEmpty()){

            textInputLayoutNombreRegistro.setHelperText("Obligatorio*");
           return false;

        }

        textInputLayoutNombreRegistro.setHelperText("¡Muy bien!");
        return true;
    }

    public boolean validarApellidos(String apellidos){

        TextInputLayout textInputLayoutApellidos = findViewById(R.id.textInputApellidosLayoutR);

        if(editTexApellidos.getText().toString().trim().isEmpty()){

            textInputLayoutApellidos.setHelperText("Obligatorio*");
            return false;

        }

        textInputLayoutApellidos.setHelperText("¡Muy bien!");
        return true;
    }

    public boolean validarTelefono(String telefono){
        TextInputLayout textInputLayoutTelefonoRegistro = findViewById(R.id.textInputTelefonoLayoutR);

        if(editTextTelefono.getText().toString().trim().isEmpty()){
            textInputLayoutTelefonoRegistro.setHelperText("Obligatorio*");
            return false;

        } else if(!telefono.matches("^[0-9]{9}$")){

            textInputLayoutTelefonoRegistro.setError("El teléfono debe tener 9 dígitos");
            return false;

        }

        textInputLayoutTelefonoRegistro.setHelperText("¡Muy bien!");
        return true;
    }

    public boolean verificarCorreoEnUso(String correo) {
        // Crear o usar una instancia de la base de datos
        BBDDUsuariosSQLite baseDeDatos = new BBDDUsuariosSQLite(this, "gestion_usuario_taller", null, 3);

        // Llamar al método de la base de datos para verificar el correo
        String correoEncontrado = baseDeDatos.correoEnUso(correo);

        // Retornar true si el correo ya está en uso, false si no
        return correoEncontrado != null;
    }

    public boolean validarCorreo(String correo){
        TextInputLayout textInputLayoutCorreoRegistro = findViewById(R.id.textInputCorreoLayoutR);

        if(editTextCorreoRegistro.getText().toString().trim().isEmpty()){

            textInputLayoutCorreoRegistro.setHelperText("Obligatorio*");
            return false;

        }else if(!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {

            textInputLayoutCorreoRegistro.setError("Correo inválido. Formato deseado:'example@gmail.com'");
            return false;

        } else if (verificarCorreoEnUso(correo)) {
            textInputLayoutCorreoRegistro.setError("Este correo ya está en uso. Cambie el correo");
            return false;
        }

        textInputLayoutCorreoRegistro.setHelperText("¡Muy bien!");
        return true;
    }

    public boolean validarContrasenyas(String contrasenya, String confirmarContrasenya){
        TextInputLayout textInputLayoutConfirmarContrasenya = findViewById(R.id.textInputConfirmarContrasenyaLayoutR);
        TextInputLayout textInputLayoutLayoutContrasenya = findViewById(R.id.textInputContrasenyaLayoutR);

        if (editTextContrasenyaRegistro.getText().toString().trim().isEmpty()){

            textInputLayoutLayoutContrasenya.setHelperText("Obligatorio*");
            return false;

        }

        if(editTextConfirmarContrasenya.getText().toString().trim().isEmpty()) {

            textInputLayoutLayoutContrasenya.setHelperText("Obligatorio*");
            return false;

        }else if(!contrasenya.equals(confirmarContrasenya)){

            textInputLayoutConfirmarContrasenya.setError("La contraseña no coincide");
            return false;
        }

        textInputLayoutLayoutContrasenya.setHelperText("¡Muy bien!");
        textInputLayoutConfirmarContrasenya.setHelperText("¡Muy bien!");
        return  true;
    }

    public boolean validarSeleccionarPerfil(String perfil){
       TextInputLayout textInputLayoutSeleccionarPerfil = findViewById(R.id.textInputSpinnerLayoutR);

       if(spinnerSeleccionarPerfil.getText().toString().trim().isEmpty()){
           textInputLayoutSeleccionarPerfil.setHelperText("Obligatorio*");
           return false;

       }

       textInputLayoutSeleccionarPerfil.setHelperText("¡Muy bien!");
        return true;
    }

    // Método para verificar todas las validaciones y habilitar/deshabilitar el botón
    public void verificarValidaciones() {
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

    }

    public void configurarValidacionDinamica() {


        // Crear un único TextWatcher
        TextWatcher textWatcherCambios = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verificarValidaciones(); // Llamar directamente al método de verificación
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


    public void mostrarTerminosYCondicionesEnAlertDialog(){

        //Inflar el diseño xml para mostrarlo al pulsar el botón de Registrarse
        LayoutInflater inflaterTerminosCondiciones = getLayoutInflater();
        View vistaDialogo = inflaterTerminosCondiciones.inflate(R.layout.alert_dialog_terminos_condiciones, null);

        //Configurar el AlertDialog
        AlertDialog.Builder builderTyC = new AlertDialog.Builder(this);
        builderTyC.setTitle("Términos y Condiciones");
        builderTyC.setView(vistaDialogo);

        //Configurar checkboxes para aceptar todos al seleccionar el checkbox 'aceptar todos'
        CheckBox checkBoxUsoServicio = vistaDialogo.findViewById(R.id.checkBoxUsoServicio);
        CheckBox checkBoxPropiedadIntelectual = vistaDialogo.findViewById(R.id.checkBoxPropiedadIntelectual);
        CheckBox checkBoxPrivacidad = vistaDialogo.findViewById(R.id.checkBoxPrivacidad);
        CheckBox checkBoxPromociones = vistaDialogo.findViewById(R.id.checkBoxPromociones);
        CheckBox checkBoxAceptarTodo = vistaDialogo.findViewById(R.id.checkBoxAceptarTodo);

        checkBoxAceptarTodo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxUsoServicio.setChecked(isChecked);
                checkBoxPropiedadIntelectual.setChecked(isChecked);
                checkBoxPrivacidad.setChecked(isChecked);
                checkBoxPromociones.setChecked(isChecked);

            }
        });

        //Configurar botones de Aceptar y Cancelar
        builderTyC.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkBoxUsoServicio.isChecked() && checkBoxPropiedadIntelectual.isChecked() && checkBoxPrivacidad.isChecked()) {
                    //Guardar el usuario en la base de datos
                    guardarUsuariosEnBaseDeDatos();
                    finish();

                } else {
                    // Mostrar mensaje de error
                    Snackbar.make(buttonRegistrarse, "Debes aceptar todos los términos para registrarte.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        builderTyC.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Cerrar el diálogo en caso de pulsar en cancelar
                dialog.dismiss();
            }
        });

        //Crear y mostrar el alertDialog
        AlertDialog alertDialogTyC = builderTyC.create();
        alertDialogTyC.show();
    }

    //Este método se llama al aceptar los términos y condiciones del alert dialog
    public void guardarUsuariosEnBaseDeDatos(){
        String nombre = editTextNombre.getText().toString();
        String apellidos = editTexApellidos.getText().toString();
        String correo = editTextCorreoRegistro.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String contrasenya = editTextContrasenyaRegistro.getText().toString();
        String tipoUsuario = spinnerSeleccionarPerfil.getText().toString();

        BBDDUsuariosSQLite baseDeDatosGestionUsuarios = new BBDDUsuariosSQLite(RegistroActivity.this, "gestion_usuario_taller", null, 3);

        //Insertar un nuevo registro
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put("nombre", nombre);
        nuevoRegistro.put("apellidos", apellidos);
        nuevoRegistro.put("correo", correo);
        nuevoRegistro.put("telefono", telefono);
        nuevoRegistro.put("contrasenya", contrasenya);
        nuevoRegistro.put("tipo_usuario", tipoUsuario);


        SQLiteDatabase baseDeDatos = baseDeDatosGestionUsuarios.getReadableDatabase();

        long id = baseDeDatos.insert("usuarios", null, nuevoRegistro);


        if(id != -1){
            Log.d("RegistroActivity","Usuario registrado correctamente: " +id);

            startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));

        } else {
            Log.d("RegistroActivity","Error al registrar el usuario: " +id);
        }

        baseDeDatos.close();

    }

}

