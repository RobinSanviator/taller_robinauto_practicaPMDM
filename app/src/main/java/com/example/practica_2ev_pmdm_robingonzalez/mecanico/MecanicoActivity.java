package com.example.practica_2ev_pmdm_robingonzalez.mecanico;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperFragmento;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperPerfil;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;

public class MecanicoActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBarNavegacionInferior; // Referencia al ChipNavigationBar
    private HelperFragmento helperFragmento; // Instancia del manejador de fragmentos
    private HelperNavegacionInferior helperNavegacionInferior;
    private HelperPerfil helperPerfil;
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
    private int frameLayoutContenedorFragmento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.mecanico_activity);
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
        chipNavigationBarNavegacionInferior =findViewById(R.id.chipNavigationNavegacionMecanico);
        frameLayoutContenedorFragmento = (R.id.frameLayoutContenedorFragmentoMecanico);
    }

    private void obtenerManejadores(){
        helperFragmento = new HelperFragmento(MecanicoActivity.this, frameLayoutContenedorFragmento);
        helperNavegacionInferior = new HelperNavegacionInferior(
                MecanicoActivity.this, chipNavigationBarNavegacionInferior, helperFragmento);
        helperPerfil = new HelperPerfil();
    }

    public TallerRobinautoSQLite obtenerInstanciaBaseDeDatos() {
        baseDeDatosGestionUsuarios = new TallerRobinautoSQLite(MecanicoActivity.this, "gestion_usuario_taller", null, 4);
        return  baseDeDatosGestionUsuarios;
    }

    private void cargarOpcionesNavegacionInferior(){
        Map<Integer, Fragment> opcionesDeMenu = new HashMap<>();
        opcionesDeMenu.put(R.id.menuPrincipal, new MecanicoMenuPrincipalFragment());
        opcionesDeMenu.put(R.id.opcionPefil, new MecanicoPerfilFragment());
        opcionesDeMenu.put(R.id.opcionAjustes, new MecanicoAjustesFragment());

        //Utilizando la clase ManejadorNavegación llamar al método configurarNavegacionInferior
        helperNavegacionInferior.configurarNavegacionInferior(opcionesDeMenu);
    }

    public void cargarMenuPrincipalPorDefecto(){
        helperFragmento.cargarFragmento(new MecanicoMenuPrincipalFragment());
    }

    //Método que se llamará desde los fragmentos de Administrador para volver al fragmento menú prinipal
    public void volverMenuPrincipal(){
        helperFragmento.cargarFragmento(new MecanicoMenuPrincipalFragment());
        helperNavegacionInferior.seleccionarItemMenuPrincipal();
    }

    public String getCorreo(){
        return getIntent().getStringExtra("correo");
    }

    public HelperFragmento getHelperFragmento() {
        return helperFragmento;
    }

    public HelperNavegacionInferior getHelperNavegacionInferior() {
        return helperNavegacionInferior;
    }

    public HelperPerfil getHelperPerfil(){
        return helperPerfil;
    }

}