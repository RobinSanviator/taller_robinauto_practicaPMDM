package com.example.practica_2ev_pmdm_robingonzalez.administrador;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;


import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.google.android.material.snackbar.Snackbar;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class AdministradorActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBarNavegacionInferior;
    private Fragment fragment;
    private FrameLayout frameLayoutContenedorFragmento;

    private  boolean correoEnviado;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.administrador_activity);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayoutNavegacionLateralAdministrador), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Inicialización de los elementos para la navegación inferior
        chipNavigationBarNavegacionInferior =findViewById(R.id.chipNavigationNavegacionAdministrador);
        crearNavegacionInferior();
        //Seleccionar el icono del menú principal como opción predeterminada
        chipNavigationBarNavegacionInferior.setItemSelected(R.id.menuPrincipal, true);
        //Cargar de manera inicial el menú principal del administrador
        cargarFragmentoNavegacionInferiorAdministrador(new AdministradorMenuPrincipalFragment());

    }


    private final ActivityResultLauncher<Intent> verificarEnviarCorreo = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Snackbar.make(frameLayoutContenedorFragmento, "Correo enviado", Snackbar.LENGTH_LONG).show();

                    } else {
                        Snackbar.make(frameLayoutContenedorFragmento, "Se ha cancelado el envio", Snackbar.LENGTH_LONG).show();
                    }
                }
            });


    public void enviarCorreoContacto() {
        try {
            Intent intentCorreo = new Intent(Intent.ACTION_SENDTO);
            intentCorreo.setData(Uri.parse("mailto:tallerrobinauto@gmail.com"));
            verificarEnviarCorreo.launch(Intent.createChooser(intentCorreo, "Elige una aplicación de correo"));

        } catch (ActivityNotFoundException e) {
            Snackbar.make(frameLayoutContenedorFragmento, "No hay aplicaciones de correo disponibles", Snackbar.LENGTH_LONG).show();
        }
    }


    public void crearNavegacionInferior(){
        chipNavigationBarNavegacionInferior.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int idSeleccionado) {
                if (idSeleccionado == R.id.menuPrincipal) {
                    cargarFragmentoNavegacionInferiorAdministrador(new AdministradorMenuPrincipalFragment());
                } else if (idSeleccionado == R.id.opcionPefil) {
                    cargarFragmentoNavegacionInferiorAdministrador(new AdministradorPerfilFragment());
                } else if (idSeleccionado == R.id.opcionAjustes) {
                    cargarFragmentoNavegacionInferiorAdministrador(new AdministradorAjustesFragment());
                }
            }
        });

    }


    public void cargarFragmentoNavegacionInferiorAdministrador(Fragment fragmento) {

        if (fragment == null) {
            FragmentTransaction fragmentTransaction = AdministradorActivity.this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayoutContenedorFragmentoAdmin, fragmento);
            fragmentTransaction.addToBackStack(null); //Agregar el fragmento al back stack para que se pueda navegar hacia atrás
            fragmentTransaction.commit();
        }
    }

    public void deseleccionarItemMenuPrincipal(){
        chipNavigationBarNavegacionInferior =  AdministradorActivity.this.findViewById(R.id.chipNavigationNavegacionAdministrador);
        chipNavigationBarNavegacionInferior.setItemSelected(R.id.menuPrincipal, false);

    }

    public void seleccionarItemMenuPrincipal(){
        chipNavigationBarNavegacionInferior =  AdministradorActivity.this.findViewById(R.id.chipNavigationNavegacionAdministrador);
        chipNavigationBarNavegacionInferior.setItemSelected(R.id.menuPrincipal, true);

    }

    }





