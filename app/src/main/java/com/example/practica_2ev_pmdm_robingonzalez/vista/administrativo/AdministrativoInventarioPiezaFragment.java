package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;

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
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.PiezaAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.ReparacionAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Pieza;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdministrativoInventarioPiezaFragment extends Fragment {

    private RecyclerView recyclerViewPieza;
    private List<Pieza> listaPieza;
    private PiezaAdapter piezaAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño layout para consultar el stock de piezas
        View vista = inflater.inflate(R.layout.fragment_administrativo_inventario_pieza, container, false);

        inicializarComponentes(vista);
        configurarRecyclerView();
        inicializarPiezasPredefinidas();
        cargarPiezas();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        recyclerViewPieza = vista.findViewById(R.id.recyclerViewPiezas);
    }

    private void configurarRecyclerView() {
        listaPieza = new ArrayList<>();
        piezaAdapter = new PiezaAdapter(listaPieza, getContext());
        recyclerViewPieza.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPieza.setAdapter(piezaAdapter);
    }

    private void inicializarPiezasPredefinidas() {
        DatabaseReference piezasRef = FirebaseDatabase.getInstance().getReference("Piezas");

        // Lista de piezas predefinidas
        String[] piezasPredefinidas = {
                "Pastillas de freno", "Líquido de frenos",
                "Filtro de aceite", "Aceite de motor",
                "Batería", "Pesas de equilibrado",
                "Fusibles", "Relés",
                "Amortiguadores", "Filtro de aire",
                "Silenciador", "Aceite de transmisión",
                "Gas refrigerante"
        };

        // Umbral mínimo predeterminado
        int umbralMinimo = 5;

        for (String piezaNombre : piezasPredefinidas) {
            piezasRef.orderByChild("nombre").equalTo(piezaNombre).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        String piezaId = piezasRef.push().getKey();
                        if (piezaId != null) {
                            Pieza pieza = new Pieza(piezaNombre, 0, umbralMinimo);
                            piezasRef.child(piezaId).setValue(pieza).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("Firebase Piezas", "Pieza añadida: " + piezaNombre);
                                } else {
                                    Log.e("Firebase Piezas", "Error al añadir pieza: " + piezaNombre);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase Piezas", "Error al comprobar existencia: " + error.getMessage());
                }
            });
        }
    }

    private void cargarPiezas() {
        DatabaseReference piezasRef = FirebaseDatabase.getInstance().getReference("Piezas");

        piezasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPieza.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Pieza pieza = ds.getValue(Pieza.class);
                        if (pieza != null) {
                            listaPieza.add(pieza);
                        }
                    }
                }
                piezaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Piezas", "Error al cargar piezas: " + error.getMessage());
            }
        });
    }
}