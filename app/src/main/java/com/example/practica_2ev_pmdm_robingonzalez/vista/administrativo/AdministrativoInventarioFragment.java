package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.google.android.material.button.MaterialButton;


/**
 * Fragmento que gestiona la interfaz de usuario del inventario para el usuario Administrativo.
 * Permite consultar piezas y realizar pedidos, además de volver al menú principal.
 */
public class AdministrativoInventarioFragment extends Fragment {

    // Componentes de la interfaz de usuario
    private ImageView imageViewVolver; // Botón para volver al menú principal
    private AdministrativoActivity activityAdministrativo; // Referencia a la actividad principal
    private MaterialButton buttonConsultarPiezas, buttonHacerPedido; // Botones para consultar piezas y hacer pedidos

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lógica de inicialización (si es necesaria)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del layout del fragmento de inventario
        View vista = inflater.inflate(R.layout.administrativo_inventario_fragment, container, false);

        // Inicializar componentes y configurar la lógica
        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeInventario();
        cargarFragmentoPorDefectoPiezas();
        configurarBotones();
        return vista;
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     *
     * @param vista La vista inflada del fragmento.
     */
    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalInventario);
        buttonConsultarPiezas = vista.findViewById(R.id.buttonConsultarPiezas);
        buttonHacerPedido = vista.findViewById(R.id.buttonHacerPedido);
    }

    /**
     * Obtiene la instancia de la actividad principal (AdministrativoActivity).
     */
    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
        } else {
            Log.e("AdministrativoInventarioFragment", "Error al obtener helper");
        }
    }

    /**
     * Configura el botón para volver al menú principal.
     */
    private void volverMenuPrincipalDesdeInventario() {
        imageViewVolver.setOnClickListener(v -> activityAdministrativo.volverMenuPrincipal());
    }

    /**
     * Carga el fragmento de consulta de piezas por defecto al iniciar el fragmento.
     */
    private void cargarFragmentoPorDefectoPiezas() {
        // Cargar fragmento inicial y resaltar el botón correspondiente
        cargarFragmento(new AdministrativoInventarioPiezaFragment(), buttonConsultarPiezas);
    }

    /**
     * Configura los botones para cargar los fragmentos correspondientes.
     */
    private void configurarBotones() {
        // Configurar el botón de consultar piezas
        buttonConsultarPiezas.setOnClickListener(v ->
                cargarFragmento(new AdministrativoInventarioPiezaFragment(), buttonConsultarPiezas)
        );

        // Configurar el botón de hacer pedido
        buttonHacerPedido.setOnClickListener(v ->
                cargarFragmento(new AdministrativoInventarioPedidoFragment(), buttonHacerPedido)
        );
    }

    /**
     * Carga un fragmento en el contenedor de fragmentos y resalta el botón seleccionado.
     *
     * @param fragmento El fragmento que se va a cargar.
     * @param botonSeleccionado El botón que se va a resaltar.
     */
    private void cargarFragmento(Fragment fragmento, MaterialButton botonSeleccionado) {
        // Resetear el color de fondo de todos los botones
        buttonConsultarPiezas.setBackgroundColor(getResources().getColor(R.color.color_desactivado_fondo));
        buttonHacerPedido.setBackgroundColor(getResources().getColor(R.color.color_desactivado_fondo));

        // Resaltar el botón seleccionado
        botonSeleccionado.setBackgroundColor(getResources().getColor(R.color.verde));

        // Cargar el fragmento en el contenedor
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutContenedorFragmentoStockYpedidos, fragmento)
                .addToBackStack(null)
                .commit();
    }
}