package com.example.practica_2ev_pmdm_robingonzalez.administrativo;

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

import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsultas;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperPerfil;


public class AdministrativoPerfilFragment extends Fragment {

    private CardView cardViewDatosPerfil;
    private TextView textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono;
    private TextView textViewNombreCabecera,  textViewCorreoCabecera;
    private ImageView imageViewMenuPrincipal;
    private String correo;
    private AdministrativoActivity activityAdministrativo;
    private HelperPerfil helperPerfil;
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
    private UsuarioConsultas usuarioConsultas;
    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperNavegacionInferior helperNavegacionInferior;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar disño del layout del perfil
        View vista = inflater.inflate(R.layout.administrativo_perfil_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipal();
        introducirDatosPerfilCabecera();
        introducirDatosEnPerfil();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewMenuPrincipal = vista.findViewById(R.id.imageViewVolverMenuPrincipalPerfilAdministrativo);
        cardViewDatosPerfil = vista.findViewById(R.id.cardViewDatosPerfilAdministrativo);
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombrePerfilAdministrativo);
        textViewCorreoCabecera = vista.findViewById(R.id.textViewCorreoPerfilAdministrativo);
        textViewNombre = vista.findViewById(R.id.textViewDatoPerfilNombreAdministrativo);
        textViewApellidos = vista.findViewById(R.id.textViewDatoPerfilApellidoAdministrativo);
        textViewCorreo = vista.findViewById(R.id.textViewDatoPerfilCorreoAdministrativo);
        textViewTelefono = vista.findViewById(R.id.textViewDatoPerfilTelefonoAdministrativo);
    }


    private void obtenerHelper() {
        if (getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
            helperPerfil = activityAdministrativo.getManejadorPerfil();
            helperMenuPrincipal = activityAdministrativo.getHelperMenuPrincipal();
            helperNavegacionInferior = activityAdministrativo.getHelperNavegacionInferior();
            baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(getContext());
            usuarioConsultas = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
        }

    }

    private void volverMenuPrincipal(){
        imageViewMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperMenuPrincipal != null){
                    helperMenuPrincipal.cargarFragmento(new AdministrativoMenuPrincipalFragment());
                    helperNavegacionInferior.seleccionarItemMenuPrincipal();
                } else {
                    Log.e("Error", "AdministrativoMenuPrincipalFragment null");
                }

            }
        });
    }

    private void introducirDatosPerfilCabecera(){
        correo = activityAdministrativo.getCorreo();
        String nombre = usuarioConsultas.obtenerNombreYApellidos(correo);

        if(correo != null && nombre != null){
            textViewNombreCabecera.setText(nombre);
            textViewCorreoCabecera.setText(correo);

        } else {
            helperPerfil.cargarDatosPerfilCabeceraDesdeFirebase(correo, textViewNombreCabecera, textViewCorreoCabecera);
        }

    }

    private void introducirDatosEnPerfil() {
        correo = activityAdministrativo.getCorreo();
        if (correo != null) {
            helperPerfil.cargarDatosPerfil(correo,textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono);
        } else {
            Log.e("AdministrativoPerfilFragment", "Correo es null");
        }

    }


}