package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class AdministrativoNotificacionesFragment extends Fragment {

    private ImageView imageViewVolver;
    private AdministrativoActivity activityAdministrativo;
    private Spinner spinnerClientes;
    private TextInputEditText editTextEnviarMensaje;
    private MaterialButton buttonEnviarMensaje, buttonConsultarMensajesEnviados;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar diseño del layout para las notificaciones
        View vista = inflater.inflate(R.layout.administrativo_notificaciones_fragment, container, false);

        inicializarComponenetes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeNotificacion();

        return vista;
    }

    private void inicializarComponenetes(View vista){
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalNotificaciones);
        spinnerClientes = vista.findViewById(R.id.spinnerCargarClientesNotificacion);
    }

    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
        } else {
            Log.e("AdministrativoNotificacionesFragment", "Error al obtener helper");
        }
    }

    private void volverMenuPrincipalDesdeNotificacion(){
        imageViewVolver.setOnClickListener(v -> activityAdministrativo.volverMenuPrincipal());
    }
}