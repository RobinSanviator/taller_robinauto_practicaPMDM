package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.ReparacionAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.ReparacionUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Coche;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdministrativoReparacionesFragment extends Fragment {


    private ImageView imageViewVolver;
    private AdministrativoActivity activityAdministrativo;
    private FloatingActionButton fabNuevaReparacion;
    private RecyclerView recyclerViewDarAltaReparacion;
    private TextView textViewNoHayReparaciones;
    private ReparacionAdapter reparacionAdapter;
    private List<Reparacion> listaReparacion;
    private TextView textviewCocheSeleccionado, textViewMarca, textViewModelo,
            textViewMatricula, textViewMecanicoJefe, textViewCorreoCliente;
    private TextInputEditText editTextPresupuesto;
    private Spinner spinnerCocheReparacion, spinnerClienteReparacion, spinnerMecanicoReparacion;
    private CheckBox checkBoxPendiente, checkBoxEnProceso, checkBoxFinalizado;
    LinearLayout  linearLayoutCorreosMecanicos;
    private Map<String, String> mecanicosMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout de reparaciones
        View vista = inflater.inflate(R.layout.administrativo_reparaciones_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeReparaciones();
        configurarRecyclerView();
        configurarCheckBoxes();
        cargarReparaciones();
        anadirNuevaReparacion();
        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalReparacionCoches);
        fabNuevaReparacion = vista.findViewById(R.id.floatingBotonDarDeAltaReparacion);
        recyclerViewDarAltaReparacion = vista.findViewById(R.id.recyclerViewListaAltaReparaciones);
        textViewNoHayReparaciones = vista.findViewById(R.id.textViewNoHayReparaciones);
        checkBoxPendiente = vista.findViewById(R.id.checkboxPendienteReparacion);
        checkBoxEnProceso = vista.findViewById(R.id.checkboxEnProcesoReparacion);
        checkBoxFinalizado = vista.findViewById(R.id.checkboxFinalizadoReparacion);
    }

    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
        } else {
            Log.e("AdministrativoReparacionesFragment", "Error al obtener helper");
        }
    }

    private void volverMenuPrincipalDesdeReparaciones(){
        imageViewVolver.setOnClickListener(v -> activityAdministrativo.volverMenuPrincipal());
    }

    private void configurarRecyclerView() {
        listaReparacion = new ArrayList<>();
        reparacionAdapter = new ReparacionAdapter(listaReparacion, getContext(),null);
        recyclerViewDarAltaReparacion.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDarAltaReparacion.setAdapter(reparacionAdapter);
    }

    private void cargarReparaciones() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaReparacion.clear();
                for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                    Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                    if (reparacion != null) {
                        // Verificar si el estado coincide con los seleccionados
                        if ((checkBoxPendiente.isChecked() && "pendiente".equalsIgnoreCase(reparacion.getEstadoReparacion())) ||
                                (checkBoxEnProceso.isChecked() && "en proceso".equalsIgnoreCase(reparacion.getEstadoReparacion())) ||
                                (checkBoxFinalizado.isChecked() && "finalizado".equalsIgnoreCase(reparacion.getEstadoReparacion()))) {
                            listaReparacion.add(reparacion);
                            Log.d("Estado Reparacion", "estado " + reparacion.getEstadoReparacion());
                        }
                    }
                }
                reparacionAdapter.notifyDataSetChanged();
                verificarListaReparaciones();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdministrativoReparacionesFragment", "Error al cargar reparaciones: " + error.getMessage());
            }
        };
        ReparacionUtil.cargarReparaciones(listener);
    }

    private void verificarListaReparaciones() {
        if (listaReparacion.isEmpty()) {
            textViewNoHayReparaciones.setVisibility(View.VISIBLE);
            recyclerViewDarAltaReparacion.setVisibility(View.GONE);
        } else {
            textViewNoHayReparaciones.setVisibility(View.GONE);
            recyclerViewDarAltaReparacion.setVisibility(View.VISIBLE);
        }
    }

    private void configurarCheckBoxes() {
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> cargarReparaciones();

        checkBoxPendiente.setOnCheckedChangeListener(listener);
        checkBoxEnProceso.setOnCheckedChangeListener(listener);
        checkBoxFinalizado.setOnCheckedChangeListener(listener);
    }

    private void anadirNuevaReparacion() {
        fabNuevaReparacion.setOnClickListener(v -> mostrarFormularioAltaReparacion());
    }


    private void mostrarFormularioAltaReparacion() {
        AlertDialog.Builder builderNuevaReparacion = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View vistaDialogo = inflater.inflate(R.layout.administrativo_dar_alta_reparacion_dialog, null);

        // Inicializar los TextViews dentro del Dialog
        textviewCocheSeleccionado = vistaDialogo.findViewById(R.id.textViewCocheSeleccionado);
        textViewMarca = vistaDialogo.findViewById(R.id.textViewMostrarMarca);
        textViewModelo = vistaDialogo.findViewById(R.id.textViewmostrarModelo);
        textViewMatricula = vistaDialogo.findViewById(R.id.textViewMostrarMatricula);
        textViewMecanicoJefe = vistaDialogo.findViewById(R.id.textViewMostrarCorreoMecanicoJefe);
        spinnerCocheReparacion = vistaDialogo.findViewById(R.id.spinnerCocheNuevaReparacion);
        spinnerClienteReparacion = vistaDialogo.findViewById(R.id.spinerClienteNuevaReparacion);
        textViewCorreoCliente = vistaDialogo.findViewById(R.id.textViewMostrarCorreoCliente);
        editTextPresupuesto = vistaDialogo.findViewById(R.id.editTextPresupuesto);
        spinnerMecanicoReparacion = vistaDialogo.findViewById(R.id.spinnerMecanicoNuevaReparacion);
        linearLayoutCorreosMecanicos = vistaDialogo.findViewById(R.id.linearLayoutCorreosMecanicos);



        //Cargar datos para los spinners
        cargarSpinnerCoche();
        cargarSpinnerCliente();
        cargarSpinnerMecanicos();


        builderNuevaReparacion.setView(vistaDialogo).setTitle("Nueva reparación").setIcon(R.drawable.ic_consul_reparaciones)
                .setPositiveButton("Dar de alta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Obtener la matrícula y el correo del coche seleccionado
                        String matriculaCoche = textViewMatricula.getText().toString();
                        String correoMecanicoJefe = textViewMecanicoJefe.getText().toString();
                        String correoCliente = textViewCorreoCliente.getText().toString();
                        // Obtener los correos de los mecánicos seleccionados usando el nuevo método
                        List<String> correosMecanicos = obtenerCorreosMecanicosSeleccionados();

                        //Obtener el presupuesto validado
                        double presupuesto = obtenerPresupuesto(editTextPresupuesto);
                        // Si el presupuesto no es válido
                        if (presupuesto == -1) {
                            return; // Sale del método y no crea la reparación
                        }

                        // Crear la nueva reparación
                        Reparacion nuevaReparacion = new Reparacion(matriculaCoche, presupuesto, correoMecanicoJefe, correoCliente, correosMecanicos);

                        // Guardar la reparación en Firebase
                        ReparacionUtil.guardarReparacionEnFirebase(nuevaReparacion);

                        // Mostrar mensaje de éxito
                        Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Alta de nueva reparación completado con éxito", Snackbar.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Alta de nueva reparación cancelado", Snackbar.LENGTH_LONG).show();
                    }
                });
        builderNuevaReparacion.show();
    }

    // Método separado para obtener el presupuesto
    private double obtenerPresupuesto(EditText editTextPresupuesto) {
        String presupuestoTexto = editTextPresupuesto.getText().toString().trim();

        if (presupuestoTexto.isEmpty()) {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    "Se debe insertar un presupuesto para realizar la nueva reparación",
                    Snackbar.LENGTH_LONG).show();
            return -1; // Retornamos un valor que indica que hubo un error
        }

        try {
            return Double.parseDouble(presupuestoTexto);
        } catch (NumberFormatException e) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "El presupuesto debe ser un número válido",
                    Snackbar.LENGTH_SHORT).show();
            return -1; // Retornamos un valor que indica que hubo un error
        }
    }

    //Método para obtener los correos de los mecánicos
    private List<String> obtenerCorreosMecanicosSeleccionados() {
        List<String> correosMecanicos = new ArrayList<>();
        for (int j = 0; j < linearLayoutCorreosMecanicos.getChildCount(); j++) {
            View childView = linearLayoutCorreosMecanicos.getChildAt(j);
            if (childView instanceof LinearLayout) {
                TextView textViewCorreo = (TextView) ((LinearLayout) childView).getChildAt(0);
                correosMecanicos.add(textViewCorreo.getText().toString());
            }
        }
        return correosMecanicos;
    }

    private void cargarSpinnerCliente() {
        UsuarioUtil.cargarUsuariosPorTipo("Cliente", new UsuarioUtil.usuariosCargadosListener() {
            @Override
            public void onUsuariosCargados(List<Usuario> usuarios) {
                // Extraer los nombres completos de los clientes (nombre + apellidos)
                List<String> listaClientes = new ArrayList<>();
                Map<String, String> clientesMap = new HashMap<>(); // Mapa para asociar nombre completo con correo

                for (Usuario usuario : usuarios) {
                    String nombreCompleto = usuario.getNombre() + " " + usuario.getApellidos();
                    String correoCliente = usuario.getCorreo(); // Obtener el correo del cliente
                    listaClientes.add(nombreCompleto); // Añadir nombre completo al spinner
                    clientesMap.put(nombreCompleto, correoCliente); // Asociar correo con nombre completo
                }

                // Configurar el Spinner con los datos cargados
                configurarSpinnerCliente(listaClientes, clientesMap);
            }

            @Override
            public void onError(Exception e) {
                Log.e("SpinnerCliente", "Error al cargar clientes: " + e.getMessage());
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        "Error al cargar clientes", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void configurarSpinnerCliente(List<String> listaClientes, Map<String, String> clientesMap) {
        ArrayAdapter<String> adapterCliente = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, listaClientes);
        adapterCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClienteReparacion.setAdapter(adapterCliente);

        // Establecer el listener para el spinner de clientes
        spinnerClienteReparacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    String clienteSeleccionado = (String) parentView.getItemAtPosition(position);

                    // Obtener el correo del cliente desde el mapa
                    String correoCliente = clientesMap.get(clienteSeleccionado);
                    textViewCorreoCliente.setText(correoCliente); // Mostrar el correo en el TextView

                    // Mostrar Snackbar en selecciones válidas
                    Snackbar.make(parentView, "Cliente " + clienteSeleccionado + " seleccionado",
                            Snackbar.LENGTH_SHORT).show();
                }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no se selecciona ningún cliente
            }
        });
    }

    private void cargarSpinnerCoche() {
        // Cargar coches desde Firebase
        FirebaseDatabase database = FirebaseUtil.getFirebaseDatabase();
        DatabaseReference cocheRef = database.getReference("Coches"); // Obtener la referencia al nodo "Coches"

        cocheRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> listaCoches = new ArrayList<>();
                Map<String, Coche> cochesMap = new HashMap<>(); // Mapa de matrícula -> objeto Coche completo

                for (DataSnapshot cocheSnapshot : snapshot.getChildren()) {
                    Coche coche = cocheSnapshot.getValue(Coche.class); // Obtener coche
                    if (coche != null) {
                        // Crear el texto para mostrar en el Spinner
                        String cocheDisponible = coche.getMarca() + " " +
                                coche.getModelo() + " " +
                                coche.getMatricula() + " - " +
                                coche.getNombreMecanicoJefe();
                        listaCoches.add(cocheDisponible);
                        cochesMap.put(cocheDisponible, coche); // Guardar el coche completo en el mapa
                    }
                }

                // Configurar el Spinner de coches
                configurarSpinnerCoche(listaCoches, cochesMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CargarCoches", "Error al cargar coches: " + error.getMessage());
            }
        });
    }

    private void configurarSpinnerCoche(List<String> listaCoches, Map<String, Coche> cochesMap) {
        ArrayAdapter<String> adapterCoche = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, listaCoches);
        adapterCoche.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCocheReparacion.setAdapter(adapterCoche);

        // Establecer el listener para el spinner
        spinnerCocheReparacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    String cocheSeleccionado = (String) parentView.getItemAtPosition(position);

                    // Obtener el coche completo desde el mapa
                    Coche cocheSeleccionadoCompleto = cochesMap.get(cocheSeleccionado);

                    if (cocheSeleccionadoCompleto != null) {
                        // Mostrar el Snackbar con la información del coche seleccionado
                        Snackbar.make(parentView, "Coche "
                                        + cocheSeleccionadoCompleto.getMarca() + " "
                                        + cocheSeleccionadoCompleto.getModelo() + " seleccionado",
                                Snackbar.LENGTH_SHORT).show();

                        // Mostrar los datos del coche en los TextViews
                        textViewMarca.setText(cocheSeleccionadoCompleto.getMarca());
                        textViewModelo.setText(cocheSeleccionadoCompleto.getModelo());
                        textViewMatricula.setText(cocheSeleccionadoCompleto.getMatricula());
                        textViewMecanicoJefe.setText(cocheSeleccionadoCompleto.getCorreoMecanicoJefe());
                    }
                }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }


    private void cargarSpinnerMecanicos() {
        UsuarioUtil.cargarUsuariosPorTipo("Mecanico", new UsuarioUtil.usuariosCargadosListener() {
            @Override
            public void onUsuariosCargados(List<Usuario> usuarios) {
                // Extraer los nombres completos de los mecánicos (nombre + apellidos)
                List<String> listaMecanicos = new ArrayList<>();
                mecanicosMap = new HashMap<>(); // Mapa para asociar nombre completo con correo

                for (Usuario usuario : usuarios) {
                    String nombreCompleto = usuario.getNombre() + " " + usuario.getApellidos();
                    String correoMecanico = usuario.getCorreo(); // Obtener el correo del mecánico
                    listaMecanicos.add(nombreCompleto); // Añadir nombre completo al spinner
                    mecanicosMap.put(nombreCompleto, correoMecanico); // Asociar correo con nombre completo
                }

                // Configurar el Spinner con los datos cargados
                configurarSpinnerMecanico(listaMecanicos, mecanicosMap);
            }

            @Override
            public void onError(Exception e) {
                Log.e("SpinnerMecanico", "Error al cargar mecánicos: " + e.getMessage());
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        "Error al cargar mecánicos", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void configurarSpinnerMecanico(List<String> listaMecanicos, Map<String, String> mecanicosMap) {
        ArrayAdapter<String> adapterMecanico = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, listaMecanicos);
        adapterMecanico.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMecanicoReparacion.setAdapter(adapterMecanico);

        // Lista para almacenar los correos de los mecánicos seleccionados
        List<String> correosMecanicosSeleccionados = new ArrayList<>();

        // Configurar el Listener para el Spinner
        spinnerMecanicoReparacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtener el mecánico seleccionado
                String mecanicoSeleccionado = (String) parentView.getItemAtPosition(position);
                String correoMecanico = mecanicosMap.get(mecanicoSeleccionado);

                // Validar si el correo ya fue agregado
                if (correoMecanico != null && !correoMecanico.isEmpty() && !correosMecanicosSeleccionados.contains(correoMecanico)) {
                    // Agregar el correo a la lista
                    correosMecanicosSeleccionados.add(correoMecanico);

                    // Mostrar mensaje de selección
                    Snackbar.make(parentView, "Mecánico seleccionado: " + mecanicoSeleccionado, Snackbar.LENGTH_SHORT).show();

                    // Llamar al método para crear el LinearLayout y añadirlo al contenedor
                    agregarMecanicoSeleccionado(correoMecanico, mecanicoSeleccionado, correosMecanicosSeleccionados, parentView);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada
            }
        });
    }

    private void agregarMecanicoSeleccionado(String correoMecanico, String nombreMecanico,
                                             List<String> correosMecanicosSeleccionados, AdapterView<?> parentView) {
        // Crear un TextView para mostrar el correo
        TextView textViewCorreo = new TextView(getContext());
        textViewCorreo.setText(correoMecanico);
        textViewCorreo.setTextSize(16);
        textViewCorreo.setTextColor(ContextCompat.getColor(getContext(), R.color.color_texto));

        // Crear un botón para eliminar el correo
        MaterialButton buttonEliminar = new MaterialButton(getContext());
        buttonEliminar.setText("Eliminar");
        buttonEliminar.setTextSize(10);
        buttonEliminar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_error)); // Establecer fondo
        buttonEliminar.setTextColor(ContextCompat.getColor(getContext(), R.color.blanco)); // Establecer color de texto
        buttonEliminar.setOnClickListener(view -> {
            // Eliminar el correo de la lista y del contenedor
            correosMecanicosSeleccionados.remove(correoMecanico);
            linearLayoutCorreosMecanicos.removeView((View) view.getParent());

            // Mostrar mensaje de eliminación
            Snackbar.make(parentView,
                    "Mecánico eliminado: " + nombreMecanico, Snackbar.LENGTH_SHORT).show();
        });

        // Crear un contenedor para el correo y el botón de eliminar
        LinearLayout contenedorCorreo = new LinearLayout(getContext());
        contenedorCorreo.setOrientation(LinearLayout.HORIZONTAL);
        contenedorCorreo.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Añadir el correo y el botón de eliminar al contenedor
        contenedorCorreo.addView(textViewCorreo);
        contenedorCorreo.addView(buttonEliminar);

        // Agregar el contenedor al LinearLayout principal
        linearLayoutCorreosMecanicos.addView(contenedorCorreo);
    }

}
