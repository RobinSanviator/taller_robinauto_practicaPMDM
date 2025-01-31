package com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico;

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
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.PiezaAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.TareaAdaptador;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.PiezaUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Pieza;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Tarea;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MecanicoPiezasFragment extends Fragment {

    private ImageView imageViewVolver;
    private MecanicoActivity mecanicoActivity;
    private RecyclerView recyclerViewPiezas;
    private PiezaAdapter piezaAdapter;
    private List<Pieza> listaPiezas;
    private String correoUsuarioActual;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtener el correo del mecánico desde la actividad
        if (getActivity() instanceof MecanicoActivity) {
            mecanicoActivity = (MecanicoActivity) getActivity();
            correoUsuarioActual = mecanicoActivity.getCorreo();  // Aquí obtienes el correo
        } else {
            Log.e("MecanicoPiezasFragment", "Error al obtener el correo desde la actividad");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout piezas mecánico
        View vista = inflater.inflate(R.layout.mecanico_piezas_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdePiezas();
        configurarRecyclerView();
        cargarPiezas();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalPiezasMecanico);
        recyclerViewPiezas = vista.findViewById(R.id.recyclerViewPiezasMecanico);

    }

    private void obtenerHelper() {
        if (getActivity() instanceof MecanicoActivity) {
            mecanicoActivity = (MecanicoActivity) getActivity();
        } else {
            Log.e("MecanicoTareasFragment", "Error al obtener helper");
        }
    }

    private void volverMenuPrincipalDesdePiezas() {
        imageViewVolver.setOnClickListener(v -> mecanicoActivity.volverMenuPrincipal());
    }


    private void configurarRecyclerView() {
        listaPiezas = new ArrayList<>();
        piezaAdapter = new PiezaAdapter(listaPiezas, getContext(),correoUsuarioActual);
        recyclerViewPiezas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPiezas.setAdapter(piezaAdapter);
    }


    private void cargarPiezas() {
        PiezaUtil.cargarPiezasDisponibles(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Lista para almacenar las piezas disponibles
                List<Pieza> piezasDisponibles = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pieza pieza = snapshot.getValue(Pieza.class);
                    if (pieza != null && pieza.getCantidad() > 0) {
                        piezasDisponibles.add(pieza); // Solo agregar si tiene cantidad mayor a 0
                    }
                }

                // Actualizar la lista de piezas en el adaptador
                listaPiezas.clear(); // Limpiar la lista actual
                listaPiezas.addAll(piezasDisponibles); // Agregar las piezas disponibles

                // Notificar al adaptador que los datos han cambiado
                piezaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MecanicoPiezasFragment", "Error al cargar piezas: " + databaseError.getMessage());
            }
        });
    }


}