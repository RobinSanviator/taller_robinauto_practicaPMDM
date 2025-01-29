package com.example.practica_2ev_pmdm_robingonzalez.vista.cliente;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.ReparacionAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.ReparacionUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ClienteReparacionesFragment extends Fragment {

    private ImageView imageViewVolver;
    private ClienteActivity clienteActivity;
    private RecyclerView recyclerViewReparacionCliente;
    private ReparacionAdapter reparacionAdapter;
    private List<Reparacion> listaReparacion;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.cliente_reparaciones_fragment, container, false);
        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeReparacionesCliente();
        configurarRecyclerView();
        cargarReparaciones();
        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalReparacionesCliente);
        recyclerViewReparacionCliente = vista.findViewById(R.id.recyclerViewReparacionesClientes);
    }
    private void obtenerHelper() {
        if (getActivity() instanceof ClienteActivity) {
           clienteActivity = (ClienteActivity) getActivity();
        } else {
            Log.e("ClienteReparacionesFragment", "Error al obtener helper");
        }
    }

    private void volverMenuPrincipalDesdeReparacionesCliente(){
        imageViewVolver.setOnClickListener(v -> clienteActivity.volverMenuPrincipal());
    }

    private void configurarRecyclerView() {
        listaReparacion = new ArrayList<>();
        reparacionAdapter = new ReparacionAdapter(listaReparacion, getContext(),null);
        recyclerViewReparacionCliente.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReparacionCliente.setAdapter(reparacionAdapter);
    }
    private void cargarReparaciones() {
        // Obtener el correo del cliente desde la actividad
        String correoCliente = clienteActivity.getCorreo();

        if (correoCliente != null && !correoCliente.isEmpty()) {
            // Crear el ValueEventListener para cargar las reparaciones filtradas
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listaReparacion.clear(); // Limpiar la lista antes de agregar nuevos datos

                    // Iterar sobre los resultados y agregarlos a la lista
                    for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                        Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                        if (reparacion != null) {
                            listaReparacion.add(reparacion);
                        }
                    }

                    // Notificar al adaptador que los datos han cambiado
                    reparacionAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("ClienteReparacionesFragment", "Error al cargar reparaciones: " + error.getMessage());
                }
            };

            // Llamar al método de la clase ReparacionUtil para cargar reparacione
            ReparacionUtil.cargarReparacionesPorCorreoCliente(correoCliente, listener);

        }
    }
}