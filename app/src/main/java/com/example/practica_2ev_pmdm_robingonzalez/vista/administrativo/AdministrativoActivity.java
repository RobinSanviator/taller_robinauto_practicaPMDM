package com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.TallerRobinautoSQLite;
import com.example.practica_2ev_pmdm_robingonzalez.base_de_datos.UsuarioConsulta;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperAjustes;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperPerfil;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Actividad principal para el usuario de tipo Administrativo.
 * Gestiona la navegación entre fragmentos (Menú Principal, Perfil y Ajustes) utilizando un ChipNavigationBar
 * y varios helpers para manejar la lógica de la interfaz de usuario.
 */
public class AdministrativoActivity extends AppCompatActivity {

    // Componentes de la interfaz de usuario
    private ChipNavigationBar chipNavigationBarNavegacionInferior; // Barra de navegación inferior
    private HelperMenuPrincipal helperMenuPrincipal; // Helper para cargar fragmentos
    private HelperNavegacionInferior helperNavegacionInferior; // Helper para gestionar la navegación inferior
    private HelperPerfil helperPerfil; // Helper para gestionar el perfil del usuario
    private HelperAjustes helperAjustes; // Helper para gestionar los ajustes
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios; // Instancia de la base de datos local
    private int frameLayoutContenedorFragmento; // Contenedor de fragmentos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilitar el modo EdgeToEdge para la actividad
        setContentView(R.layout.administrativo_activity); // Establecer el layout de la actividad

        // Configurar el padding para evitar superposiciones con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar componentes y lógica de la actividad
        inicializarComponentes();
        inicializarBaseDeDatos();
        obtenerHelper();
        cargarOpcionesNavegacionInferior();
        cargarMenuPrincipalPorDefecto();
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     */
    private void inicializarComponentes() {
        chipNavigationBarNavegacionInferior = findViewById(R.id.chipNavigationNavegacionAdministrativo);
        frameLayoutContenedorFragmento = (R.id.frameLayoutContenedorFragmentoAdministrativo);
    }

    /**
     * Inicializa la base de datos local para gestionar usuarios.
     */
    private void inicializarBaseDeDatos() {
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(AdministrativoActivity.this);
        // Obtener la instancia de UsuarioConsultas
        baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();
    }

    /**
     * Obtiene las instancias de los helpers necesarios para gestionar la lógica de la actividad.
     */
    private void obtenerHelper() {
        helperMenuPrincipal = new HelperMenuPrincipal(AdministrativoActivity.this, frameLayoutContenedorFragmento);
        helperNavegacionInferior = new HelperNavegacionInferior(
                AdministrativoActivity.this, chipNavigationBarNavegacionInferior, helperMenuPrincipal);
        helperPerfil = new HelperPerfil(baseDeDatosGestionUsuarios);
        helperAjustes = new HelperAjustes(helperMenuPrincipal, helperNavegacionInferior);
    }

    /**
     * Configura las opciones de la barra de navegación inferior.
     * Asocia cada ítem del menú con un fragmento específico.
     */
    public void cargarOpcionesNavegacionInferior() {
        Map<Integer, Fragment> opcionesDeMenu = new HashMap<>();
        opcionesDeMenu.put(R.id.menuPrincipal, new AdministrativoMenuPrincipalFragment());
        opcionesDeMenu.put(R.id.opcionPefil, new AdministrativoPerfilFragment());
        opcionesDeMenu.put(R.id.opcionAjustes, new AdministrativoAjustesFragment());

        // Configurar la navegación inferior utilizando el helper
        helperNavegacionInferior.configurarNavegacionInferior(opcionesDeMenu);
    }

    /**
     * Carga el fragmento del Menú Principal por defecto al iniciar la actividad.
     */
    public void cargarMenuPrincipalPorDefecto() {
        helperMenuPrincipal.cargarFragmento(new AdministrativoMenuPrincipalFragment());
    }

    /**
     * Método para volver al fragmento del Menú Principal desde otros fragmentos.
     */
    public void volverMenuPrincipal() {
        helperMenuPrincipal.cargarFragmento(new AdministrativoMenuPrincipalFragment());
        helperNavegacionInferior.seleccionarItemMenuPrincipal();
    }

    /**
     * Obtiene el correo del usuario actual desde el Intent.
     *
     * @return El correo del usuario.
     */
    public String getCorreo() {
        return getIntent().getStringExtra("correo");
    }

    // Getters para los helpers

    /**
     * Obtiene el helper para gestionar el menú principal.
     *
     * @return Instancia de HelperMenuPrincipal.
     */
    public HelperMenuPrincipal getHelperMenuPrincipal() {
        return helperMenuPrincipal;
    }

    /**
     * Obtiene el helper para gestionar la navegación inferior.
     *
     * @return Instancia de HelperNavegacionInferior.
     */
    public HelperNavegacionInferior getHelperNavegacionInferior() {
        return helperNavegacionInferior;
    }

    /**
     * Obtiene el helper para gestionar el perfil del usuario.
     *
     * @return Instancia de HelperPerfil.
     */
    public HelperPerfil getManejadorPerfil() {
        return helperPerfil;
    }

    /**
     * Obtiene el helper para gestionar los ajustes.
     *
     * @return Instancia de HelperAjustes.
     */
    public HelperAjustes getHelperAjustes() {
        return helperAjustes;
    }
}