package com.example.practica_2ev_pmdm_robingonzalez.vista.administrador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.UsuarioModificarUsuariosAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AdministradorModificarUsuariosFragment extends Fragment {

    private ImageView imageViewVolver;
    private AdministradorActivity administradorActivity;
    private List<Usuario> usuariosList;
    private UsuarioModificarUsuariosAdapter usuariosModificarAdapter;
    private RecyclerView recyclerViewUsuarios;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflar diseño del layout del menú principal
        View vista = inflater.inflate(R.layout.administrador_modificar_usuarios_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeUsuarios();
        configurarRecyclerView();
        cargarUsuarios();

        return vista;
    }

    private void inicializarComponentes(View vista){
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalDesdeCliente);
        recyclerViewUsuarios = vista.findViewById(R.id.recyclerViewListaUsuariosModificar);
    }

    private void obtenerHelper(){
        if(getActivity() instanceof AdministradorActivity){
            administradorActivity = ((AdministradorActivity) getActivity());
        }
    }
    private void volverMenuPrincipalDesdeUsuarios(){
        imageViewVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (administradorActivity != null) {
                    administradorActivity.volverMenuPrincipal();
                }else {

                }
            }
        });
    }

    private void configurarRecyclerView(){
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        usuariosList = new ArrayList<>();
        usuariosModificarAdapter = new UsuarioModificarUsuariosAdapter(usuariosList, getContext());
        recyclerViewUsuarios.setAdapter(usuariosModificarAdapter);
    }


    private void cargarUsuarios() {
        if (hayConexionInternet()) {
            // Cargar los usuarios desde Firebase sin necesidad del tipo de usuario
            UsuarioUtil.cargarUsuariosBBBDD(new UsuarioUtil.usuariosCargadosListener() {
                @Override
                public void onUsuariosCargados(List<Usuario> usuarios) {
                    // Actualiza el RecyclerView con los usuarios obtenidos
                    usuariosList.clear(); // Limpiar la lista actual
                    usuariosList.addAll(usuarios); // Añadir los usuarios cargados
                    usuariosModificarAdapter.notifyDataSetChanged(); // Notificar que los datos han cambiado
                }

                @Override
                public void onError(Exception e) {
                    // Manejar el error si la carga de usuarios falla
                    Log.e("AdministradorModificarUsuariosFragment", "Error al cargar los usuarios", e);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Error al cargar los usuarios", Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "No tienes conexión a Internet. Conéctate para ver los usuarios", Snackbar.LENGTH_LONG).show();
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