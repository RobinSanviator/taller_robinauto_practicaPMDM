package com.example.practica_2ev_pmdm_robingonzalez.administrativo;

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
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.ReparacionAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.ReparacionUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;


public class AdministrativoReparacionesFragment extends Fragment {

    private ImageView imageViewVolver;
    private AdministrativoActivity activityAdministrativo;
    private FloatingActionButton fabNuevaReparacion;
    private RecyclerView recyclerViewDarAltaReparacion;
    private TextView textViewNoHayReparaciones;
    private ReparacionAdapter reparacionAdapter;
    private List<Reparacion> listaReparacion;


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
        // Crear el listener para cargar las reparaciones
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaReparaciones.clear();
                for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                    Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                    listaReparaciones.add(reparacion);
                }
                reparacionAdapter.notifyDataSetChanged();
                verificarListaReparaciones();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdministrativoReparacionesFragment", "Error al cargar reparaciones: " + error.getMessage());
            }
        };

        // Llamar al método estático de ReparacionUtil para cargar las reparaciones
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

    private void configurarFabNuevaReparacion() {

    }


}