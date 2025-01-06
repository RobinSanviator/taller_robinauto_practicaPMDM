package com.example.practica_2ev_pmdm_robingonzalez.administrador;

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


public class AdministradorPerfilFragment extends Fragment {

    private CardView cardViewDatosPerfil;
    private TextView textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono;
    private TextView textViewNombreCabecera,  textViewCorreoCabecera;
    private ImageView imageViewMenuPrincipal;
    private String correo;
    private AdministradorActivity activityAdministrador;
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
        // Inflar diseño del layout perfil
        View vista = inflater.inflate(R.layout.administrador_perfil_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipal();
        introducirDatosPerfilCabecera();
        introducirDatosEnPerfil();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewMenuPrincipal = vista.findViewById(R.id.imageViewVolverMenuPrincipalPerfilAdmin);
        cardViewDatosPerfil = vista.findViewById(R.id.cardViewDatosPerfilAdministrador);
        textViewNombreCabecera = vista.findViewById(R.id.textViewNombrePerfilAdmin);
        textViewCorreoCabecera = vista.findViewById(R.id.textViewCorreoPerfilAdmin);
        textViewNombre = vista.findViewById(R.id.textViewDatoPerfilNombreAdmin);
        textViewApellidos = vista.findViewById(R.id.textViewDatoPerfilApellidoAdmin);
        textViewCorreo = vista.findViewById(R.id.textViewDatoPerfilCorreoAdmin);
        textViewTelefono = vista.findViewById(R.id.textViewDatoPerfilTelefonoAdmin);
    }


    private void obtenerHelper() {
        if (getActivity() instanceof AdministradorActivity) {
            activityAdministrador = ((AdministradorActivity) getActivity());
            helperPerfil = activityAdministrador.getHelperPerfil();
            helperMenuPrincipal = activityAdministrador.getHelperFragmento();
            helperNavegacionInferior = activityAdministrador.getHelperNavegacionInferior();
            baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(getActivity().getApplicationContext());
            usuarioConsultas = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
        }

    }

    private void volverMenuPrincipal(){
        imageViewMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helperMenuPrincipal != null){
                    helperMenuPrincipal.cargarFragmento(new AdministradorMenuPrincipalFragment());
                    helperNavegacionInferior.seleccionarItemMenuPrincipal();
                } else {
                    Log.e("AdministradorPerfilFragment", "Error al volver al menú principal");
                }

            }
        });
    }

    private void introducirDatosPerfilCabecera(){
        correo = activityAdministrador.getCorreo();
        String nombre = usuarioConsultas.obtenerNombreYApellidos(correo);

        if (correo != null && nombre != null) {
            textViewNombreCabecera.setText(nombre);
            textViewCorreoCabecera.setText(correo);
        } else {
            helperPerfil.cargarDatosPerfilCabeceraDesdeFirebase(correo, textViewNombreCabecera, textViewCorreoCabecera);
        }

    }

    private void introducirDatosEnPerfil() {
        correo = activityAdministrador.getCorreo();
        if (correo != null) {
            helperPerfil.cargarDatosPerfil(correo, textViewNombre, textViewApellidos, textViewCorreo, textViewTelefono);
        } else {
            Log.e("AdministradorPerfilFragment", "Error al introducir los datos en el perfil");
        }

    }
}