package com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico_jefe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.MecanicoAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MecanicoJefeTareasFragment extends Fragment {

    private ImageView imageViewVolver;
    private MecanicoJefeActivity mecanicoJefeActivity;
    private Spinner spinnerSeleccionaReparacionTarea;
    private LinearLayout linearLayoutAsignarTareas;
    private RecyclerView recyclerViewListaMecanicos;
    private MecanicoAdapter mecanicoAdapter;
    private List<Usuario> listaMecanicos;
    private TextView textViewNoHayReparacionEnProceso;
    private CardView cardViewAsignarTareasMecanico;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout tareas mecánico jefe
        View vista = inflater.inflate(R.layout.mecanico_jefe_tareas_fragment, container, false);
        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeTareas();
        configurarRecyclerView();
        cargarSpinnerReparacionesEnProceso();
        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalTareasMjefe);
        spinnerSeleccionaReparacionTarea = vista.findViewById(R.id.spinnerSeleccionaReparacionParaTarea);
        linearLayoutAsignarTareas = vista.findViewById(R.id.linearLayoutAsignarTareas);
        recyclerViewListaMecanicos = vista.findViewById(R.id.recyclerViewListaMecánicos);
        textViewNoHayReparacionEnProceso = vista.findViewById(R.id.textViewNoHayReparacionesEnProceso);
        cardViewAsignarTareasMecanico = vista.findViewById(R.id.cardViewAsignarTareaMecanico);
    }
    private void obtenerHelper() {
        if (getActivity() instanceof MecanicoJefeActivity) {
            mecanicoJefeActivity = (MecanicoJefeActivity) getActivity();
        } else {
            Log.e("MecanicoJefeTareasFragment", "Error al obtener helper");
        }
    }

    private void volverMenuPrincipalDesdeTareas() {
        imageViewVolver.setOnClickListener(v -> mecanicoJefeActivity.volverMenuPrincipal());
    }

    private void configurarRecyclerView() {
        listaMecanicos = new ArrayList<>();
        mecanicoAdapter = new MecanicoAdapter(listaMecanicos, getContext(), null);
        recyclerViewListaMecanicos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewListaMecanicos.setAdapter(mecanicoAdapter);
    }

    private void cargarSpinnerReparacionesEnProceso() {
        // Obtener el correo del mecánico jefe
        String correoMecanicoJefe = mecanicoJefeActivity.getCorreo();

        // Obtener referencia a Firebase Database
        FirebaseDatabase firebaseDatabase = FirebaseUtil.getFirebaseDatabase();
        DatabaseReference reparacionesRef = firebaseDatabase.getReference("Reparaciones");

        // Realizar la consulta para obtener las reparaciones en proceso
        reparacionesRef.orderByChild("estadoReparacion")
                .equalTo("En proceso") // Filtrar reparaciones con estado "En proceso"
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Lista para almacenar las reparaciones que cumplen con el filtro
                        List<String> reparacionesEnProceso = new ArrayList<>();
                        Map<String, String> reparacionesMap = new HashMap<>(); // Map para guardar ID de reparación

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Reparacion reparacion = snapshot.getValue(Reparacion.class);

                            if (reparacion != null && reparacion.getCorreoMecanicoJefe().equals(correoMecanicoJefe)) {
                                // Guardar el ID único de la reparación y el nombre/estado para mostrar
                                String reparacionInfo = reparacion.getTipoReparacion() + " - "
                                        + reparacion.getCorreoCliente();
                                reparacionesEnProceso.add(reparacionInfo);
                                reparacionesMap.put(reparacionInfo, snapshot.getKey()); // Guardar el ID de la reparación
                            }
                        }

                        // Llamar al método para cargar datos en el spinner
                        cargarDatosEnSpinner(reparacionesEnProceso, reparacionesMap);

                        // Si no hay reparaciones en proceso, ocultar el CardView y mostrar el TextView
                        if (reparacionesEnProceso.isEmpty()) {
                            cardViewAsignarTareasMecanico.setVisibility(View.GONE); // Ocultar CardView
                            textViewNoHayReparacionEnProceso.setVisibility(View.VISIBLE); // Mostrar TextView
                        } else {
                            cardViewAsignarTareasMecanico.setVisibility(View.VISIBLE); // Mostrar CardView
                            textViewNoHayReparacionEnProceso.setVisibility(View.GONE); // Ocultar TextView
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("MecanicoJefeTareas", "Error al cargar las reparaciones: " + databaseError.getMessage());
                    }
                });
    }


    private void cargarDatosEnSpinner(List<String> reparacionesEnProceso, Map<String, String> reparacionesMap) {
        if (reparacionesEnProceso != null && !reparacionesEnProceso.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, reparacionesEnProceso);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerSeleccionaReparacionTarea.setAdapter(adapter);

            spinnerSeleccionaReparacionTarea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String seleccionada = parent.getItemAtPosition(position).toString();
                    String idReparacion = reparacionesMap.get(seleccionada);

                    // Actualizar en SharedPreferences (opcional)
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("idReparacion", idReparacion);
                    editor.apply();
                    Log.d("TAREAS MJEFE", "idReparacion Enviado: " +idReparacion);



                    Log.d("ID_REPARACION", "ID de reparación seleccionado: " + idReparacion);
                    mecanicoAdapter.setIdReparacion(idReparacion);
                    mecanicoAdapter.notifyDataSetChanged();
                    // Mostrar el LinearLayout y cargar mecánicos
                    linearLayoutAsignarTareas.setVisibility(View.VISIBLE);
                    cargarMecanicosDeReparacion(idReparacion);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void cargarMecanicosDeReparacion(String idReparacion) {
        DatabaseReference reparacionesRef = FirebaseUtil.getFirebaseDatabase().getReference("Reparaciones");
        reparacionesRef.child(idReparacion).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verificar que la reparación existe
                if (dataSnapshot.exists()) {
                    Reparacion reparacion = dataSnapshot.getValue(Reparacion.class);

                    if (reparacion != null) {
                        // Obtener los correos de los mecánicos asignados
                        List<String> correosMecanicos = reparacion.getCorreosMecanicosAsignados();

                        // Consultar los datos de cada mecánico
                        cargarDatosDeMecanicos(correosMecanicos);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MecanicoJefeTareas", "Error al cargar mecánicos: " + databaseError.getMessage());
            }
        });
    }

    private void cargarDatosDeMecanicos(List<String> correosMecanicos) {
        DatabaseReference usuariosRef = FirebaseUtil.getFirebaseDatabase().getReference("Usuarios");
        listaMecanicos.clear(); // Limpiar la lista antes de agregar nuevos datos

        for (String correo : correosMecanicos) {
            usuariosRef.orderByChild("correo").equalTo(correo)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Usuario mecanico = snapshot.getValue(Usuario.class);
                                if (mecanico != null) {
                                    listaMecanicos.add(mecanico);
                                }
                            }

                            // Actualizar el RecyclerView después de cargar los datos
                            mecanicoAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("MecanicoJefeTareas", "Error al cargar datos de mecánicos: " + databaseError.getMessage());
                        }
                    });
        }
    }


}