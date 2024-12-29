package com.example.practica_2ev_pmdm_robingonzalez.mecanico_jefe;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.BBDDUsuariosSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperFragmento;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperPerfil;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;

public class MecanicoJefeActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBarNavegacionInferior; // Referencia al ChipNavigationBar
    private HelperFragmento helperFragmento; // Instancia del manejador de fragmentos
    private HelperNavegacionInferior helperNavegacionInferior;
    private HelperPerfil helperPerfil;
    private BBDDUsuariosSQLite baseDeDatosGestionUsuarios;
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
        helperFragmento = new HelperFragmento(MecanicoJefeActivity.this, frameLayoutContenedorFragmento);
        helperNavegacionInferior = new HelperNavegacionInferior(
                MecanicoJefeActivity.this, chipNavigationBarNavegacionInferior, helperFragmento);
        helperPerfil = new HelperPerfil();
    }

    public BBDDUsuariosSQLite obtenerInstanciaBaseDeDatos() {
        baseDeDatosGestionUsuarios = new BBDDUsuariosSQLite(MecanicoJefeActivity.this, "gestion_usuario_taller", null, 3);
        return  baseDeDatosGestionUsuarios;
    }

    private void cargarOpcionesNavegacionInferior(){
        Map<Integer, Fragment> opcionesDeMenu = new HashMap<>();
        opcionesDeMenu.put(R.id.menuPrincipal, new MecanicoJefeMenuPrincipalFragment());
        opcionesDeMenu.put(R.id.opcionPefil, new MecanicoJefePerfilFragment());
        opcionesDeMenu.put(R.id.opcionAjustes, new MecanicoJefeAjustesFragment());

        //Utilizando la clase ManejadorNavegación llamar al método configurarNavegacionInferior
        helperNavegacionInferior.configurarNavegacionInferior(opcionesDeMenu);
    }

    public void cargarMenuPrincipalPorDefecto(){
        helperFragmento.cargarFragmento(new MecanicoJefeMenuPrincipalFragment());
    }

    //Método que se llamará desde los fragmentos de Mecánico jefe para volver al fragmento menú prinipal
    public void volverMenuPrincipal(){
        helperFragmento.cargarFragmento(new MecanicoJefeMenuPrincipalFragment());
        helperNavegacionInferior.seleccionarItemMenuPrincipal();
    }

    public String getCorreo(){
        return getIntent().getStringExtra("correo");
    }

    public HelperFragmento getManejadorFragmento() {
        return helperFragmento;
    }

    public HelperNavegacionInferior getManejadorNavegacionInferior() {
        return helperNavegacionInferior;
    }

    public HelperPerfil getManejadorPerfil(){
        return helperPerfil;
    }

}



