package com.example.practica_2ev_pmdm_robingonzalez.administrativo;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
    private EditText editTextPresupuesto;
    private Spinner spinnerCocheReparacion, spinnerClienteReparacion;
    private String correoCliente;





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
        cargarReparaciones();
        anadirNuevaReparacion();




        return vista;
    }


    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalReparacionCoches);
        fabNuevaReparacion = vista.findViewById(R.id.floatingBotonDarDeAltaReparacion);
        recyclerViewDarAltaReparacion = vista.findViewById(R.id.recyclerViewListaAltaReparaciones);
        textViewNoHayReparaciones = vista.findViewById(R.id.textViewNoHayReparaciones);
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
        reparacionAdapter = new ReparacionAdapter(listaReparacion, getContext());
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
                    listaReparacion.add(reparacion);

                    // Si necesitas seleccionar una reparación y mostrar su matrícula:
                    if (textviewCocheSeleccionado != null && reparacion.getMatriculaCoche() != null) {
                        textviewCocheSeleccionado.setText(reparacion.getMatriculaCoche());
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

        //Cargar datos para los spinners
        cargarSpinnerCoche();
        cargarSpinnerCliente();


        builderNuevaReparacion.setView(vistaDialogo).setTitle("Nueva reparación").setIcon(R.drawable.ic_consul_reparaciones)
                .setPositiveButton("Dar de alta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Obtener el presupuesto desde el EditText
                        String presupuestoTexto = editTextPresupuesto.getText().toString().trim();
                        if (presupuestoTexto.isEmpty()) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content),
                                    "Se debe insertar un presupuesto para realizar la nueva reparación",
                                    Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        double presupuesto;
                        try {
                            presupuesto = Double.parseDouble(presupuestoTexto);
                        } catch (NumberFormatException e) {
                            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                    "El presupuesto debe ser un número válido",
                                    Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        // Obtener la matrícula y el correo del coche seleccionado
                        String matriculaCoche = textViewMatricula.getText().toString();
                        String correoMecanicoJefe = textViewMecanicoJefe.getText().toString();
                        String correoCliente = textViewCorreoCliente.getText().toString();

                        // Crear la nueva reparación
                        Reparacion nuevaReparacion = new Reparacion(matriculaCoche, presupuesto, correoMecanicoJefe, correoCliente); // El correo del cliente debe obtenerse de algún otro lugar

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
        // Configurar el ArrayAdapter para el Spinner
        ArrayAdapter<String> adapterCliente = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, listaClientes);
        adapterCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClienteReparacion.setAdapter(adapterCliente);

        // Al seleccionar un cliente, guardar el correo asociado
        spinnerClienteReparacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String clienteSeleccionado = (String) parentView.getItemAtPosition(position);
                // Obtener el correo del cliente desde el mapa
                String correoCliente = clientesMap.get(clienteSeleccionado);
                textViewCorreoCliente.setText(correoCliente);  // Mostrar el correo en el TextView
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
                        String cocheDisponible = coche.getMarca() + " " + coche.getModelo() + " " + coche.getMatricula() + " - " + coche.getNombreMecanicoJefe();
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
        // Configurar el ArrayAdapter para el Spinner
        ArrayAdapter<String> adapterCoche = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, listaCoches);
        adapterCoche.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCocheReparacion.setAdapter(adapterCoche);

        spinnerCocheReparacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtener el coche seleccionado
                String cocheSeleccionado = (String) parentView.getItemAtPosition(position);

                // Obtener el coche completo desde el mapa
                Coche cocheSeleccionadoCompleto = cochesMap.get(cocheSeleccionado);

                if (cocheSeleccionadoCompleto != null) {
                    // Mostrar los datos del coche en los TextViews
                    textViewMarca.setText(cocheSeleccionadoCompleto.getMarca());
                    textViewModelo.setText(cocheSeleccionadoCompleto.getModelo());
                    textViewMatricula.setText(cocheSeleccionadoCompleto.getMatricula());
                    textViewMecanicoJefe.setText(cocheSeleccionadoCompleto.getNombreMecanicoJefe());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no se selecciona ningún coche
            }
        });
    }


}
