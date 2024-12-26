package com.example.practica_2ev_pmdm_robingonzalez.administrador;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practica_2ev_pmdm_robingonzalez.R;

public class AdministradorModificarUsuariosFragment extends Fragment {

    private ImageView imageViewVolver;
    private AdministradorActivity administradorActivity;
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
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalDesdeUsuarios);


        obtenerMetdosAdministrador();
        volverMenuPrincipalDesdeUsuarios();
        return vista;
    }

    public void obtenerMetdosAdministrador(){
        if(getActivity() instanceof AdministradorActivity){
            administradorActivity = ((AdministradorActivity) getActivity());
        }
    }
    public void volverMenuPrincipalDesdeUsuarios(){
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

}