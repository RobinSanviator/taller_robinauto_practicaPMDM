package com.example.practica_2ev_pmdm_robingonzalez.mecanico_jefe;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

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

public class MecanicoJefeActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBarNavegacionInferior; // Referencia al ChipNavigationBar
    private ManejadorFragmento manejadorFragmento; // Instancia del manejador de fragmentos
    private ManejadorNavegacionInferior manejadorNavegacionInferior;
    private int frameLayoutContenedorFragmento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mecanico_jefe_activity);
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
        chipNavigationBarNavegacionInferior =findViewById(R.id.chipNavigationNavegacionMjefe);
        frameLayoutContenedorFragmento = (R.id.frameLayoutContenedorFragmentoMecanicoJefe);
    }

    private void obtenerManejadores(){
        manejadorFragmento = new ManejadorFragmento(MecanicoJefeActivity.this, frameLayoutContenedorFragmento);
        manejadorNavegacionInferior = new ManejadorNavegacionInferior(
                MecanicoJefeActivity.this, chipNavigationBarNavegacionInferior, manejadorFragmento);
    }

    private void cargarOpcionesNavegacionInferior(){
        Map<Integer, Fragment> opcionesDeMenu = new HashMap<>();
        opcionesDeMenu.put(R.id.menuPrincipal, new MecanicoJefeMenuPrincipalFragment());
        opcionesDeMenu.put(R.id.opcionPefil, new MecanicoJefePerfilFragment());
        opcionesDeMenu.put(R.id.opcionAjustes, new MecanicoJefeAjustesFragment());

        //Utilizando la clase ManejadorNavegación llamar al método configurarNavegacionInferior
        manejadorNavegacionInferior.configurarNavegacionInferior(opcionesDeMenu);
    }

    public void cargarMenuPrincipalPorDefecto(){
        manejadorFragmento.cargarFragmento(new MecanicoJefeMenuPrincipalFragment());
    }

    //Método que se llamará desde los fragmentos de Mecánico jefe para volver al fragmento menú prinipal
    public void volverMenuPrincipal(){
        manejadorFragmento.cargarFragmento(new MecanicoJefeMenuPrincipalFragment());
        manejadorNavegacionInferior.seleccionarItemMenuPrincipal();
    }

    public ManejadorFragmento getManejadorFragmento() {
        return manejadorFragmento;
    }

    public ManejadorNavegacionInferior getManejadorNavegacionInferior() {
        return manejadorNavegacionInferior;
    }


}



