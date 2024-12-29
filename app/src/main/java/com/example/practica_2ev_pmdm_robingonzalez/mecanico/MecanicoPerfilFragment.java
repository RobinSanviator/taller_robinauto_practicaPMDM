package com.example.practica_2ev_pmdm_robingonzalez.mecanico;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperFragmento;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperPerfil;


public class MecanicoPerfilFragment extends Fragment {

    private CardView cardViewDatosPerfil;
    private TextView textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono;
    private TextView textViewNombreCabecera,  textViewCorreoCabecera;
    private ImageView imageViewMenuPrincipal;
    private String correo;
    private MecanicoActivity activityMecanico;
    private HelperPerfil helperPerfil;
    private BBDDUsuariosSQLite baseDeDatos;
    private HelperFragmento helperFragmento;
    private HelperNavegacionInferior helperNavegacionInferior;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del layout de perfil
        View vista = inflater.inflate(R.layout.mecanico_perfil_fragment, container, false);

        inicializarComponentes(vista);
        obtenerManejadores();
        volverMenuPrincipal();
        introducirDatosPerfilCabecera();
        introducirDatosEnPerfil();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewMenuPrincipal = vista.findViewById(R.id.imageViewVolverMenuPrincipalPerfilMecanico);
        cardViewDatosPerfil = vista.findViewById(R.id.cardViewDatosPerfilMecanico);
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombrePerfilMecanico);
        textViewCorreoCabecera = vista.findViewById(R.id.textViewCorreoPerfilMecanico);
        textViewNombre = vista.findViewById(R.id.textViewDatoPerfilNombreMecanico);
        textViewApellidos = vista.findViewById(R.id.textViewDatoPerfilApellidoMecanico);
        textViewCorreo = vista.findViewById(R.id.textViewDatoPerfilCorreoMecanico);
        textViewTelefono = vista.findViewById(R.id.textViewDatoPerfilTelefonoMecanico);
    }


    private void obtenerManejadores() {
        if (getActivity() instanceof MecanicoActivity) {
            activityMecanico = ((MecanicoActivity) getActivity());
            helperPerfil = activityMecanico.getManejadorPerfil();
            helperFragmento = activityMecanico.getManejadorFragmento();
            helperNavegacionInferior = activityMecanico.getManejadorNavegacionInferior();
            baseDeDatos = activityMecanico.obtenerInstanciaBaseDeDatos();
        }

    }

    private void volverMenuPrincipal(){
        imageViewMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperFragmento != null){
                    helperFragmento.cargarFragmento(new MecanicoMenuPrincipalFragment());
                    helperNavegacionInferior.seleccionarItemMenuPrincipal();
                } else {
                    Log.e("Error", "MecanicoMenuPrincipalFragment null");
                }

            }
        });
    }

    private void introducirDatosPerfilCabecera(){
        correo = activityMecanico.getCorreo();
        String nombre = baseDeDatos.obtenerNombreYApellidos(correo);

        if(correo != null && nombre != null){
            textViewNombreCabecera.setText(nombre);
            textViewCorreoCabecera.setText(correo);

        } else {
            Log.e("Error", "Correo o nombre null");
        }

    }

    private void introducirDatosEnPerfil() {
        correo = activityMecanico.getCorreo();
        if (correo != null) {
            helperPerfil.cargarDatosPerfil(correo,baseDeDatos, textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono);
        } else {
            Log.e("Error", "Correo es null");
        }

    }

}