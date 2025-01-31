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

/**
 * Fragmento de la pantalla de modificación de usuarios desde administrador.
 * Permite cargar y visualizar la lista de usuarios para su modificación.
 */
public class AdministradorModificarUsuariosFragment extends Fragment {

    private ImageView imageViewVolver;  // Imagen para volver al menú principal
    private AdministradorActivity administradorActivity;  // Actividad principal del administrador
    private List<Usuario> usuariosList;  // Lista de usuarios a mostrar
    private UsuarioModificarUsuariosAdapter usuariosModificarAdapter;  // Adaptador para el RecyclerView
    private RecyclerView recyclerViewUsuarios;  // RecyclerView para mostrar los usuarios

    /**
     * Se llama cuando el fragmento es creado. Este método se deja vacío si no es necesario guardar ningún estado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Se llama para inflar la vista del fragmento.
     * Aquí se inicializan los componentes visuales y se cargan los usuarios.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del layout de modificar usuarios
        View vista = inflater.inflate(R.layout.administrador_modificar_usuarios_fragment, container, false);

        inicializarComponentes(vista);  // Inicializa las vistas
        obtenerHelper();  // Obtiene la actividad principal
        volverMenuPrincipalDesdeUsuarios();  // Configura el botón para volver al menú principal
        configurarRecyclerView();  // Configura el RecyclerView para mostrar la lista de usuarios
        cargarUsuarios();  // Carga los usuarios desde la base de datos o Firebase

        return vista;  // Retorna la vista inflada
    }

    /**
     * Inicializa los componentes visuales del fragmento.
     * Se obtiene la referencia a las vistas (ImageView, RecyclerView, etc.).
     */
    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalDesdeCliente);  // Imagen para volver al menú principal
        recyclerViewUsuarios = vista.findViewById(R.id.recyclerViewListaUsuariosModificar);  // RecyclerView de usuarios
    }

    /**
     * Obtiene la instancia de la actividad principal del administrador.
     * Asegura que la actividad sea una instancia de `AdministradorActivity`.
     */
    private void obtenerHelper() {
        if (getActivity() instanceof AdministradorActivity) {
            administradorActivity = (AdministradorActivity) getActivity();  // Obtiene la actividad principal del administrador
        }
    }

    /**
     * Configura la acción para volver al menú principal al hacer clic en la imagen correspondiente.
     */
    private void volverMenuPrincipalDesdeUsuarios() {
        imageViewVolver.setOnClickListener(v -> {
            if (administradorActivity != null) {
                administradorActivity.volverMenuPrincipal();  // Vuelve al menú principal
            } else {
                Log.e("AdministradorModificarUsuariosFragment", "No se pudo volver al menú principal");
            }
        });
    }

    /**
     * Configura el RecyclerView para mostrar la lista de usuarios.
     * Se establece un LinearLayoutManager y se crea el adaptador para el RecyclerView.
     */
    private void configurarRecyclerView() {
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));  // Configura el LayoutManager
        usuariosList = new ArrayList<>();  // Crea la lista de usuarios
        usuariosModificarAdapter = new UsuarioModificarUsuariosAdapter(usuariosList, getContext());  // Crea el adaptador
        recyclerViewUsuarios.setAdapter(usuariosModificarAdapter);  // Asocia el adaptador al RecyclerView
    }

    /**
     * Carga los usuarios desde Firebase o desde la base de datos local.
     * Si hay conexión a Internet, los usuarios se cargan desde Firebase.
     */
    private void cargarUsuarios() {
        if (hayConexionInternet()) {
            // Si hay conexión a Internet, se cargan los usuarios desde Firebase
            UsuarioUtil.cargarUsuariosBBBDD(new UsuarioUtil.usuariosCargadosListener() {
                @Override
                public void onUsuariosCargados(List<Usuario> usuarios) {
                    // Actualiza el RecyclerView con los usuarios obtenidos
                    usuariosList.clear();  // Limpiar la lista actual
                    usuariosList.addAll(usuarios);  // Añadir los usuarios cargados
                    usuariosModificarAdapter.notifyDataSetChanged();  // Notificar que los datos han cambiado
                }

                @Override
                public void onError(Exception e) {
                    // Manejar el error si la carga de usuarios falla
                    Log.e("AdministradorModificarUsuariosFragment", "Error al cargar los usuarios", e);
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Error al cargar los usuarios", Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            // Si no hay conexión a Internet, mostrar mensaje
            Snackbar.make(getActivity().findViewById(android.R.id.content), "No tienes conexión a Internet. Conéctate para ver los usuarios", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Verifica si el dispositivo tiene conexión a Internet.
     * @return true si hay conexión, false si no la hay.
     */
    private boolean hayConexionInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();  // Retorna si la red está conectada
        }
        return false;  // Si no hay conexión
    }
}
