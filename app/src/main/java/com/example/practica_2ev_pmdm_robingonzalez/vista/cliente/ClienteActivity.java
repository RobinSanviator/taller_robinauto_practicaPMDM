package com.example.practica_2ev_pmdm_robingonzalez.vista.cliente;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperAjustes;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperPerfil;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;

public class ClienteActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBarNavegacionInferior; // Referencia al ChipNavigationBar
    private HelperMenuPrincipal helperMenuPrincipal; // Instancia del manejador de fragmentos
    private HelperNavegacionInferior helperNavegacionInferior;
    private HelperPerfil helperPerfil;
    private HelperAjustes helperAjustes;
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;
    private int frameLayoutContenedorFragmento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cliente_activity);
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
        chipNavigationBarNavegacionInferior = findViewById(R.id.chipNavigationNavegacionCliente);
        frameLayoutContenedorFragmento = (R.id.frameLayoutContenedorFragmentoCliente);

    }

    private void inicializarBaseDeDatos(){
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(ClienteActivity.this);
        // Obtener la instancia de UsuarioConsultas
        baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
    }

    private void obtenerHelper(){
        helperMenuPrincipal = new HelperMenuPrincipal(ClienteActivity.this, frameLayoutContenedorFragmento);
        helperNavegacionInferior = new HelperNavegacionInferior(
                ClienteActivity.this, chipNavigationBarNavegacionInferior, helperMenuPrincipal);
        helperPerfil = new HelperPerfil(baseDeDatosGestionUsuarios);
        helperAjustes = new HelperAjustes(helperMenuPrincipal,helperNavegacionInferior);
    }


    public void cargarOpcionesNavegacionInferior(){
        Map<Integer, Fragment> opcionesDeMenu = new HashMap<>();
        opcionesDeMenu.put(R.id.menuPrincipal, new ClienteMenuPrincipalFragment());
        opcionesDeMenu.put(R.id.opcionPefil, new ClientePerfilFragment());
        opcionesDeMenu.put(R.id.opcionAjustes, new ClienteAjustesFragment());

        //Utilizando la clase ManejadorNavegación llamar al método configurarNavegacionInferior
        helperNavegacionInferior.configurarNavegacionInferior(opcionesDeMenu);
    }

    public void cargarMenuPrincipalPorDefecto(){
        helperMenuPrincipal.cargarFragmento(new ClienteMenuPrincipalFragment());
    }


    //Método que se llamará desde los fragmentos de Cliente para volver al fragmento menú prinipal
    public void volverMenuPrincipal(){
        helperMenuPrincipal.cargarFragmento(new ClienteMenuPrincipalFragment());
        helperNavegacionInferior.seleccionarItemMenuPrincipal();
    }

    public String getCorreo(){
        return getIntent().getStringExtra("correo");
    }


    public HelperMenuPrincipal getHelperMenuPrincipal() {
        return helperMenuPrincipal;
    }


    public HelperNavegacionInferior getHelperNavegacionInferior() {
        return helperNavegacionInferior;
    }

    public HelperPerfil getManejadorPerfil(){
        return helperPerfil;
    }

    public HelperAjustes getHelperAjustes() {
        return helperAjustes;
    }



}