package com.example.practica_2ev_pmdm_robingonzalez.administrativo;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorActivity;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorAjustesFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorPerfilFragment;
import com.example.practica_2ev_pmdm_robingonzalez.navegacion.ManejadorFragmento;
import com.example.practica_2ev_pmdm_robingonzalez.navegacion.ManejadorNavegacionInferior;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;

public class AdministrativoActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBarNavegacionInferior; // Referencia al ChipNavigationBar
    private ManejadorFragmento manejadorFragmento; // Instancia del manejador de fragmentos
    private ManejadorNavegacionInferior manejadorNavegacionInferior;
    private int frameLayoutContenedorFragmento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.administrativo_activity);
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
        chipNavigationBarNavegacionInferior = findViewById(R.id.chipNavigationNavegacionAdministrativo);
        frameLayoutContenedorFragmento = (R.id.frameLayoutContenedorFragmentoAdministrativo);

    }

    private void obtenerManejadores(){
        manejadorFragmento = new ManejadorFragmento(AdministrativoActivity.this, frameLayoutContenedorFragmento);
        manejadorNavegacionInferior = new ManejadorNavegacionInferior(
                AdministrativoActivity.this, chipNavigationBarNavegacionInferior, manejadorFragmento);
    }

    public void cargarOpcionesNavegacionInferior(){
        Map<Integer, Fragment> opcionesDeMenu = new HashMap<>();
        opcionesDeMenu.put(R.id.menuPrincipal, new AdministrativoMenuPrincipalFragment());
        opcionesDeMenu.put(R.id.opcionPefil, new AdministrativoPerfilFragment());
        opcionesDeMenu.put(R.id.opcionAjustes, new AdministrativoAjustesFragment());

        //Utilizando la clase ManejadorNavegación llamar al método configurarNavegacionInferior
        manejadorNavegacionInferior.configurarNavegacionInferior(opcionesDeMenu);
    }

    public void cargarMenuPrincipalPorDefecto(){
        manejadorFragmento.cargarFragmento(new AdministrativoMenuPrincipalFragment());
    }


    //Método que se llamará desde los fragmentos de Administrativo para volver al fragmento menú prinipal
    public void volverMenuPrincipal(){
        manejadorFragmento.cargarFragmento(new AdministrativoMenuPrincipalFragment());
        manejadorNavegacionInferior.seleccionarItemMenuPrincipal();
    }


    public ManejadorFragmento getManejadorFragmento() {
        return manejadorFragmento;
    }

    public ManejadorNavegacionInferior getManejadorNavegacionInferior() {
        return manejadorNavegacionInferior;
    }
}