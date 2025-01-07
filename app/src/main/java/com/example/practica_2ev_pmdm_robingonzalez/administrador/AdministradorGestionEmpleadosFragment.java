package com.example.practica_2ev_pmdm_robingonzalez.administrador;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
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
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.UsuarioAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtils;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AdministradorGestionEmpleadosFragment extends Fragment {

    private ImageView imageViewVolver;
    private ImageView imageViewAdministrativos, imageViewMecanicosJefes, imageViewMecanicos;
    private FloatingActionButton fabDarDeAlta;
    private RecyclerView recyclerViewUsuarios;
    private AdministradorActivity activityAdministrador;
    private List<Usuario> usuariosList;
    private UsuarioAdapter usuariosAdapter;
    private TextInputLayout layoutNombre, layoutApellidos,
            layoutCorreo, layoutTelefono, layoutContrasenya;
    private EditText editTextNombreEmpleado, editTextApellidosEmpleado,
            editTextCorreoEmpleado, editTextTelefonoEmpleado, editTextContrasenyaEmpleado;
    private Spinner spinnerTipoUsuarioEmpleado;
    private String tipoEmpleadoActual;
    private TextView textViewDarDeBajaAdministrativo, textViewDarDeBajaMjefe,
            textViewDarDeBajaMecanico;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout gestion de empleados
        View vista = inflater.inflate(R.layout.administrador_gestion_empleados_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        verificarConexion();
        volverMenuPrincipalDesdeEmpleados();
        configurarRecyclerView();
        configurarListeners();
        anadirEmpleado();


        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalDesdeEmpleados);
        imageViewAdministrativos = vista.findViewById(R.id.imageViewVolverAltaBajaAdministrativos);
        imageViewMecanicosJefes = vista.findViewById(R.id.imageViewVolverAltaBajaMjefes);
        imageViewMecanicos = vista.findViewById(R.id.imageViewVolverAltaBajaMecanicos);
        fabDarDeAlta = vista.findViewById(R.id.floatingBotonDarDeAlta);
        recyclerViewUsuarios = vista.findViewById(R.id.recyclerViewListaUsuariosAltaBaja);
        textViewDarDeBajaAdministrativo = vista.findViewById(R.id.textViewEliminarEmpleadoAdministrativo);
        textViewDarDeBajaMjefe = vista.findViewById(R.id.textViewEliminarEmpleadoMjefe);
        textViewDarDeBajaMecanico = vista.findViewById(R.id.textViewEliminarEmpleadoMecanico);
    }

    private void obtenerHelper(){
        if(getActivity() instanceof AdministradorActivity) {
            activityAdministrador = ((AdministradorActivity) getActivity());
        } else {
            Log.e("AdministradorGestionEmpleadosFragment", "Error al obtener helper");
        }
    }

    private void verificarConexion(){
       if(hayConexionInternet()){
        cargarUsuarios("Administrativo");
       } else {
           // Usa un contenedor genérico si no está disponible
           Snackbar.make(getActivity().findViewById(android.R.id.content),
                   "No tienes conexión a Internet. Conéctate para ver las listas de empleados",
                   Snackbar.LENGTH_LONG).show();
       }
    }

    private void volverMenuPrincipalDesdeEmpleados(){
        imageViewVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityAdministrador.volverMenuPrincipal();
            }
        });
    }

    private void configurarRecyclerView(){
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        usuariosList = new ArrayList<>();
        usuariosAdapter = new UsuarioAdapter(usuariosList, getContext());
        recyclerViewUsuarios.setAdapter(usuariosAdapter);
    }

    private void configurarListeners() {
        configurarClickListener(imageViewAdministrativos, "Administrativo");
        configurarClickListener(imageViewMecanicosJefes, "Mecanico jefe");
        configurarClickListener(imageViewMecanicos, "Mecanico");
    }

    private void configurarClickListener(ImageView imageView, String tipoUsuario) {
      imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //Actualizar el tipo de empleado al tipo de usuario seleccionado
              tipoEmpleadoActual = tipoUsuario;

              if (hayConexionInternet()) {
                  cargarUsuarios(tipoUsuario);
              } else {
                  Snackbar.make(v, "No tienes conexión a Internet. Conéctate para ver las listas de empleados", Snackbar.LENGTH_LONG).show();
              }

          }
      });
    }

    private void cargarUsuarios(String tipoUsuario) {

            UsuarioUtils.cargarUsuariosPorTipo(tipoUsuario, new UsuarioUtils.usuariosCargadosListener() {
                @Override
                public void onUsuariosCargados(List<Usuario> usuarios) {
                    // Actualiza el RecyclerView con los usuarios obtenidos
                    usuariosList.clear();
                    usuariosList.addAll(usuarios);
                    usuariosAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Exception e) {
                      Log.e("AdministradorGestionEmpleadosFragment", "Error al cargar los usuarios");
                }
            });

        }

    private void anadirEmpleado(){
        fabDarDeAlta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarFormularioDeAlta(tipoEmpleadoActual);
            }
        });
        }

    private void mostrarFormularioDeAlta(String tipoEmpleadoActual){

         if (tipoEmpleadoActual == null || tipoEmpleadoActual.isEmpty()) {
             tipoEmpleadoActual = "Administrativo";  // Asignar un valor predeterminado
         }
         AlertDialog.Builder builderFormulario = new AlertDialog.Builder(requireContext());
         builderFormulario.setTitle("Añadir empleado" + " " + tipoEmpleadoActual.toLowerCase())
                 .setIcon(R.drawable.logo_app_robinauto);

         //Inflar el layout
         LayoutInflater inflater = requireActivity().getLayoutInflater();
         View formularioDialogView = inflater.inflate(R.layout.administrador_formulario_alta_dialog, null);
         builderFormulario.setView(formularioDialogView);
         //inflar los elementos del formulario
         inflarVistaElementos(formularioDialogView);

         //Configurar el spinner
         configurarSpinnerTipoUsuario(spinnerTipoUsuarioEmpleado, tipoEmpleadoActual);

         builderFormulario.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 obtenerDatosFormularioYGuardar();
             }
         });
         //Cancelar alta
         builderFormulario.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
                 Snackbar.make(getActivity().findViewById(android.R.id.content), "Has cancelado la alta del empleado", Snackbar.LENGTH_SHORT).show();
             }
         });

         builderFormulario.create().show();
     }

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

    private void inflarVistaElementos(View formularioDialogView){
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
        //asiganr los textWatcher
        asignarValidaciones();

    }

    private void asignarValidaciones(){
        editTextNombreEmpleado.addTextChangedListener(crearTextWatcher(layoutNombre, "El nombre es obligatorio"));
        editTextApellidosEmpleado.addTextChangedListener(crearTextWatcher(layoutApellidos, "Los apellidos son obligatorios"));
        editTextCorreoEmpleado.addTextChangedListener(crearTextWatcher(layoutCorreo, "Correo electrónico inválido, 'ejemplo@ejemplo.com'"));
        editTextTelefonoEmpleado.addTextChangedListener(crearTextWatcher(layoutTelefono, "Número de teléfono debe tener 9 dígitos"));
        editTextContrasenyaEmpleado.addTextChangedListener(crearTextWatcher(layoutContrasenya, "La contraseña debe tener al menos 6 caracteres"));
    }

    private TextWatcher crearTextWatcher(TextInputLayout layout, String errorMessage) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().trim().isEmpty()) {
                    layout.setError(errorMessage);
                } else {
                    layout.setError(null);  // Limpiar el error cuando es válido
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void obtenerDatosFormularioYGuardar() {
        // Obtener los datos de los campos
        String nombre = editTextNombreEmpleado.getText().toString().trim();
        String apellidos = editTextApellidosEmpleado.getText().toString().trim();
        String correo = editTextCorreoEmpleado.getText().toString().trim();
        String telefono = editTextTelefonoEmpleado.getText().toString().trim();
        String contrasenya = editTextContrasenyaEmpleado.getText().toString().trim();
        String tipoUsuario = tipoEmpleadoActual;

        // Validar todos los campos de una vez
        boolean camposValidos = true;

        // Validar nombre
        if (nombre.isEmpty()) {
            layoutNombre.setError("El nombre es obligatorio");
            camposValidos = false;
        } else {
            layoutNombre.setError(null);
        }

        // Validar apellidos
        if (apellidos.isEmpty()) {
            layoutApellidos.setError("Los apellidos son obligatorios");
            camposValidos = false;
        } else {
            layoutApellidos.setError(null);
        }

        // Validar correo electrónico
        if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {
            layoutCorreo.setError("Correo electrónico inválido, 'ejemplo@ejemplo.com'");
            camposValidos = false;
        } else {
            layoutCorreo.setError(null);
        }

        // Validar teléfono
        if (telefono.isEmpty() || telefono.length() != 9) {
            layoutTelefono.setError("Número de teléfono debe tener 9 dígitos");
            camposValidos = false;
        } else {
            layoutTelefono.setError(null);
        }

        // Validar contraseña
        if (contrasenya.isEmpty() || contrasenya.length() < 6) {
            layoutContrasenya.setError("La contraseña debe tener al menos 6 caracteres");
            camposValidos = false;
        } else {
            layoutContrasenya.setError(null);
        }

        // Si todos los campos son válidos, guardar el empleado
        if (camposValidos) {
            // Llamar al método para guardar los datos del empleado
            UsuarioUtils.guardarEmpleadoEnFirebase(requireContext(), nombre, apellidos, correo, telefono, contrasenya, tipoUsuario);
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Se ha guardado el empleado correctamente", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Por favor, corrige los errores antes de guardar", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean hayConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    }
