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
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.PiezaUtil;
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
        DatabaseReference piezasRef = FirebaseUtil.getFirebaseDatabase().getReference("Piezas");

        String[] piezasPredefinidas = {
                "Pastillas de freno", "Líquido de frenos",
                "Filtro de aceite", "Aceite de motor",
                "Batería", "Pesas de equilibrado",
                "Fusibles", "Relés",
                "Amortiguadores", "Filtro de aire",
                "Silenciador", "Aceite de transmisión",
                "Gas refrigerante"
        };

        int umbralMinimo = 5;

        for (String piezaNombre : piezasPredefinidas) {
            piezasRef.orderByChild("nombre").equalTo(piezaNombre)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                String piezaId = piezasRef.push().getKey();
                                if (piezaId != null) {
                                    // Asignar el ID de la imagen según el nombre de la pieza
                                    int imagenPieza = obtenerIconoPorNombre(piezaNombre);
                                    Pieza pieza = new Pieza(piezaNombre, 0, umbralMinimo, imagenPieza);
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

    // Método que retorna el ID del icono correspondiente según el nombre de la pieza
    private int obtenerIconoPorNombre(String nombrePieza) {
        switch (nombrePieza) {
            case "Pastillas de freno":
                return R.drawable.ic_pastilla_frenos;
            case "Líquido de frenos":
                return R.drawable.ic_liquido_frenos;
            case "Filtro de aceite":
                return R.drawable.ic_filtro_aceite;
            case "Aceite de motor":
                return R.drawable.ic_aceite_motor;
            case "Batería":
                return R.drawable.ic_bateria;
            case "Pesas de equilibrado":
                return R.drawable.ic_pesas_equilibrado;
            case "Fusibles":
                return R.drawable.ic_fusibles;
            case "Relés":
                return R.drawable.ic_reles;
            case "Amortiguadores":
                return R.drawable.ic_amortiguadores;
            case "Filtro de aire":
                return R.drawable.ic_filtro_aire;
            case "Silenciador":
                return R.drawable.ic_silenciador;
            case "Aceite de transmisión":
                return R.drawable.ic_aceite_transmision;
            case "Gas refrigerante":
                return R.drawable.ic_gas_refrigerante;
            default:
                return R.drawable.pieza;
        }
    }

    private void cargarPiezas() {
        PiezaUtil.cargarPiezas(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPieza.clear();
                for (DataSnapshot piezaSnapshot : snapshot.getChildren()) {
                    Pieza pieza = piezaSnapshot.getValue(Pieza.class);
                    if (pieza != null) {
                        listaPieza.add(pieza);
                    }
                }
                Log.d("Fragment", "Número de piezas cargadas: " + listaPieza.size());
                piezaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Fragment", "Error al cargar piezas: " + error.getMessage());
            }
        });
    }

}