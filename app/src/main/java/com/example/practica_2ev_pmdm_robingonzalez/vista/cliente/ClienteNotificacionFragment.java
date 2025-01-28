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
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.NotificacionAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Notificacion;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ClienteNotificacionFragment extends Fragment {

    private ImageView imageViewVolver;
    private ClienteActivity clienteActivity;
    private RecyclerView recyclerViewNotificaciones;
    private NotificacionAdapter notificacionAdapter;
    private List<Notificacion> notificaciones = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout de contacto con el taller
        View vista = inflater.inflate(R.layout.cliente_notificaciones_fragment, container, false);


        inicializarComponenetes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeNotificacion();
        configurarRecyclerView();
        cargarNotificacionesDesdeFirebase();

        return vista;
    }

    private void inicializarComponenetes(View vista){
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalNotificacionCliente);
        recyclerViewNotificaciones = vista.findViewById(R.id.recyclerViewNotificacionesCliente);

    }

    private void obtenerHelper() {
        if (getActivity() instanceof ClienteActivity) {
            clienteActivity = ((ClienteActivity) getActivity());
        } else {
            Log.e("AdministrativoNotificacionesFragment", "Error al obtener helper");
        }
    }

    private void volverMenuPrincipalDesdeNotificacion(){
        imageViewVolver.setOnClickListener(v -> clienteActivity.volverMenuPrincipal());
    }

    private void configurarRecyclerView() {
        String correoReceptor = clienteActivity.getCorreo();
        recyclerViewNotificaciones.setLayoutManager(new LinearLayoutManager(getContext()));
        notificacionAdapter = new NotificacionAdapter(notificaciones, getContext());
        recyclerViewNotificaciones.setAdapter(notificacionAdapter);

    }

    private void cargarNotificacionesDesdeFirebase() {
        String correoReceptor = clienteActivity.getCorreo();
        DatabaseReference ref = FirebaseUtil.getFirebaseDatabase().getReference("Notificaciones");

        ref.orderByChild("correoReceptor").equalTo(correoReceptor)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notificaciones.clear(); // Limpiar lista para evitar duplicados
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Notificacion notificacion = ds.getValue(Notificacion.class);
                            if (notificacion != null) {
                                notificaciones.add(notificacion);
                            }
                        }
                        notificacionAdapter.notifyDataSetChanged(); // Notificar cambios al adaptador
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error al cargar notificaciones: " + error.getMessage());
                    }
                });
    }



}