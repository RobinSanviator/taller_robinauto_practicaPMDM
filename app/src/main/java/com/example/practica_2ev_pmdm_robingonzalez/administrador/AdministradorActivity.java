package com.example.practica_2ev_pmdm_robingonzalez.administrador;

import android.annotation.SuppressLint;
import android.os.Bundle;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.administrativo.AdministrativoActivity;
import com.example.practica_2ev_pmdm_robingonzalez.administrativo.AdministrativoMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.navegacion.ManejadorFragmento;
import com.example.practica_2ev_pmdm_robingonzalez.navegacion.ManejadorNavegacionInferior;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;


public class AdministradorActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBarNavegacionInferior; // Referencia al ChipNavigationBar
    private ManejadorFragmento manejadorFragmento; // Instancia del manejador de fragmentos
    private ManejadorNavegacionInferior manejadorNavegacionInferior;
    private int frameLayoutContenedorFragmento;


    private  boolean correoEnviado;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.administrador_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();
        obtenerManejadores();
        cargarOpcionesNavegacionInferior();
        cargarMenuPrincipalPorDefecto();

    }

    private void inicializarComponentes(){
        chipNavigationBarNavegacionInferior =findViewById(R.id.chipNavigationNavegacionAdministrador);
        frameLayoutContenedorFragmento = (R.id.frameLayoutContenedorFragmentoAdmin);
    }

    private void obtenerManejadores(){
        manejadorFragmento = new ManejadorFragmento(AdministradorActivity.this, frameLayoutContenedorFragmento);
        manejadorNavegacionInferior = new ManejadorNavegacionInferior(
                AdministradorActivity.this, chipNavigationBarNavegacionInferior, manejadorFragmento);
    }

    private void cargarOpcionesNavegacionInferior(){
        Map<Integer, Fragment> opcionesDeMenu = new HashMap<>();
        opcionesDeMenu.put(R.id.menuPrincipal, new AdministradorMenuPrincipalFragment());
        opcionesDeMenu.put(R.id.opcionPefil, new AdministradorPerfilFragment());
        opcionesDeMenu.put(R.id.opcionAjustes, new AdministradorAjustesFragment());

        //Utilizando la clase ManejadorNavegación llamar al método configurarNavegacionInferior
        manejadorNavegacionInferior.configurarNavegacionInferior(opcionesDeMenu);
    }

    public void cargarMenuPrincipalPorDefecto(){
        manejadorFragmento.cargarFragmento(new AdministradorMenuPrincipalFragment());
    }

    //Método que se llamará desde los fragmentos de Administrador para volver al fragmento menú prinipal
    public void volverMenuPrincipal(){
        manejadorFragmento.cargarFragmento(new AdministradorMenuPrincipalFragment());
        manejadorNavegacionInferior.seleccionarItemMenuPrincipal();
    }

    public ManejadorFragmento getManejadorFragmento() {
        return manejadorFragmento;
    }

    public ManejadorNavegacionInferior getManejadorNavegacionInferior() {
        return manejadorNavegacionInferior;
    }



   /* private final ActivityResultLauncher<Intent> verificarEnviarCorreo = registerForActivityResult(
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
    }*/



    }






