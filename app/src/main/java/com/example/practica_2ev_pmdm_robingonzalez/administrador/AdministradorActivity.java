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
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsultas;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperAjustes;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperPerfil;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;


public class AdministradorActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBarNavegacionInferior; // Referencia al ChipNavigationBar
    private HelperMenuPrincipal helperMenuPrincipal; // Instancia del manejador de fragmentos
    private HelperNavegacionInferior helperNavegacionInferior;
    private HelperPerfil helperPerfil;
    private HelperAjustes helperAjustes;
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
    private UsuarioConsultas usuarioConsultas;
    private int frameLayoutContenedorFragmento;


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
        inicializarBaseDeDatos();
        obtenerHelper();
        cargarOpcionesNavegacionInferior();
        cargarMenuPrincipalPorDefecto();

    }

    private void inicializarComponentes(){
        chipNavigationBarNavegacionInferior =findViewById(R.id.chipNavigationNavegacionAdministrador);
        frameLayoutContenedorFragmento = (R.id.frameLayoutContenedorFragmentoAdmin);
    }

    private void inicializarBaseDeDatos(){
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(AdministradorActivity.this);
        // Obtener la instancia de UsuarioConsultas
        usuarioConsultas = baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
    }

    private void obtenerHelper(){
        helperMenuPrincipal = new HelperMenuPrincipal(AdministradorActivity.this, frameLayoutContenedorFragmento);
        helperNavegacionInferior = new HelperNavegacionInferior(
                AdministradorActivity.this, chipNavigationBarNavegacionInferior, helperMenuPrincipal);

        helperPerfil = new HelperPerfil(baseDeDatosGestionUsuarios);
        helperAjustes = new HelperAjustes(helperMenuPrincipal,helperNavegacionInferior);

    }


    private void cargarOpcionesNavegacionInferior(){
        Map<Integer, Fragment> opcionesDeMenu = new HashMap<>();
        opcionesDeMenu.put(R.id.menuPrincipal, new AdministradorMenuPrincipalFragment());
        opcionesDeMenu.put(R.id.opcionPefil, new AdministradorPerfilFragment());
        opcionesDeMenu.put(R.id.opcionAjustes, new AdministradorAjustesFragment());

        //Utilizando la clase ManejadorNavegación llamar al método configurarNavegacionInferior
        helperNavegacionInferior.configurarNavegacionInferior(opcionesDeMenu);
    }

    public void cargarMenuPrincipalPorDefecto(){
        helperMenuPrincipal.cargarFragmento(new AdministradorMenuPrincipalFragment());
    }

    //Método que se llamará desde los fragmentos de Administrador para volver al fragmento menú prinipal
    public void volverMenuPrincipal(){
        helperMenuPrincipal.cargarFragmento(new AdministradorMenuPrincipalFragment());
        helperNavegacionInferior.seleccionarItemMenuPrincipal();
    }

    public String getCorreo(){
        return getIntent().getStringExtra("correo");
    }

    public HelperMenuPrincipal getHelperFragmento() {
        return helperMenuPrincipal;
    }

    public HelperNavegacionInferior getHelperNavegacionInferior() {
        return helperNavegacionInferior;
    }

    public HelperPerfil getHelperPerfil() {
        return helperPerfil;
    }

    public HelperAjustes getHelperAjustes() {
        return helperAjustes;
    }


    }






