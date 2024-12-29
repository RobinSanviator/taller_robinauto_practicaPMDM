package com.example.practica_2ev_pmdm_robingonzalez.mecanico_jefe;

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


public class MecanicoJefePerfilFragment extends Fragment {

    private CardView cardViewDatosPerfil;
    private TextView textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono;
    private TextView textViewNombreCabecera,  textViewCorreoCabecera;
    private ImageView imageViewMenuPrincipal;
    private String correo;
    private MecanicoJefeActivity activityMecanicoJefe;
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
        // Inflar diseño del layout del perfil
        View vista = inflater.inflate(R.layout.mecanico_jefe_perfil_fragment, container, false);

        inicializarComponentes(vista);
        obtenerManejadores();
        volverMenuPrincipal();
        introducirDatosPerfilCabecera();
        introducirDatosEnPerfil();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewMenuPrincipal = vista.findViewById(R.id.imageViewVolverMenuPrincipalPerfilMjefe);
        cardViewDatosPerfil = vista.findViewById(R.id.cardViewDatosPerfilMjefe);
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombrePerfilMjefe);
        textViewCorreoCabecera = vista.findViewById(R.id.textViewCorreoPerfilMjefe);
        textViewNombre = vista.findViewById(R.id.textViewDatoPerfilNombreMjefe);
        textViewApellidos = vista.findViewById(R.id.textViewDatoPerfilApellidoMjefe);
        textViewCorreo = vista.findViewById(R.id.textViewDatoPerfilCorreoMjefe);
        textViewTelefono = vista.findViewById(R.id.textViewDatoPerfilTelefonoMjefe);
    }


    private void obtenerManejadores() {
        if (getActivity() instanceof MecanicoJefeActivity) {
            activityMecanicoJefe = ((MecanicoJefeActivity) getActivity());
            helperPerfil = activityMecanicoJefe.getManejadorPerfil();
            helperFragmento = activityMecanicoJefe.getManejadorFragmento();
            helperNavegacionInferior = activityMecanicoJefe.getManejadorNavegacionInferior();
            baseDeDatos = activityMecanicoJefe.obtenerInstanciaBaseDeDatos();
        }

    }

    private void volverMenuPrincipal(){
        imageViewMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperFragmento != null){
                    helperFragmento.cargarFragmento(new MecanicoJefeMenuPrincipalFragment());
                    helperNavegacionInferior.seleccionarItemMenuPrincipal();
                } else {
                    Log.e("Error", "MecanicoJefeMenuPrincipalFragment null");
                }

            }
        });
    }

    private void introducirDatosPerfilCabecera(){
        correo = activityMecanicoJefe.getCorreo();
        String nombre = baseDeDatos.obtenerNombreYApellidos(correo);

        if(correo != null && nombre != null){
            textViewNombreCabecera.setText(nombre);
            textViewCorreoCabecera.setText(correo);

        } else {
            Log.e("Error", "Correo o nombre null");
        }

    }

    private void introducirDatosEnPerfil() {
        correo = activityMecanicoJefe.getCorreo();
        if (correo != null) {
            helperPerfil.cargarDatosPerfil(correo, baseDeDatos, textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono);
        } else {
            Log.e("Error", "Correo es null");
        }

    }


}