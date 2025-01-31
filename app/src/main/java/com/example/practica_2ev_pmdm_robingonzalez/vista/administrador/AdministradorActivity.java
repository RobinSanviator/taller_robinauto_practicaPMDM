package com.example.practica_2ev_pmdm_robingonzalez.vista.administrador;

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
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperAjustes;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperMenuPrincipal;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperNavegacionInferior;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.HelperPerfil;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;


/**
 * Activity principal para el administrador del taller.
 * Esta actividad gestiona la navegación entre diferentes fragmentos de la interfaz de usuario y maneja la interacción
 * con el menú, perfil y ajustes del administrador.
 */
public class AdministradorActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBarNavegacionInferior; // Referencia al ChipNavigationBar para navegación inferior
    private HelperMenuPrincipal helperMenuPrincipal;  // Helper para manejar el menú principal
    private HelperNavegacionInferior helperNavegacionInferior;  // Helper para la navegación inferior
    private HelperPerfil helperPerfil;  // Helper para gestionar el perfil del administrador
    private HelperAjustes helperAjustes;  // Helper para gestionar los ajustes
    private TallerRobinautoSQLite baseDeDatosGestionUsuarios;  // Instancia de la base de datos local
    private int frameLayoutContenedorFragmento;  // Contenedor para los fragmentos en la actividad

    /**
     * Método que se ejecuta cuando se crea la actividad.
     * Inicializa los componentes de la interfaz, la base de datos, los helpers, y configura la navegación.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Habilita la funcionalidad de borde a borde
        setContentView(R.layout.administrador_activity);  // Establece el layout de la actividad

        // Ajusta el padding para que los elementos se adapten a las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();  // Inicializa los componentes de la interfaz
        inicializarBaseDeDatos();  // Inicializa la base de datos
        obtenerHelper();  // Obtiene las instancias de los helpers
        cargarOpcionesNavegacionInferior();  // Carga las opciones de navegación inferior
        cargarMenuPrincipalPorDefecto();  // Carga el menú principal por defecto
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     * Se obtienen las referencias a los elementos visuales.
     */
    private void inicializarComponentes(){
        chipNavigationBarNavegacionInferior = findViewById(R.id.chipNavigationNavegacionAdministrador);  // Referencia a la barra de navegación inferior
        frameLayoutContenedorFragmento = R.id.frameLayoutContenedorFragmentoAdmin;  // Contenedor para los fragmentos
    }

    /**
     * Inicializa la base de datos local.
     * Se obtiene la instancia de la base de datos para realizar consultas relacionadas con usuarios.
     */
    private void inicializarBaseDeDatos(){
        baseDeDatosGestionUsuarios = TallerRobinautoSQLite.getInstance(AdministradorActivity.this);  // Obtiene la instancia de la base de datos
        baseDeDatosGestionUsuarios.obtenerUsuarioConsultas();  // Inicializa las consultas de usuarios
    }

    /**
     * Obtiene las instancias de los helpers para gestionar las diferentes funcionalidades de la actividad.
     */
    private void obtenerHelper(){
        helperMenuPrincipal = new HelperMenuPrincipal(AdministradorActivity.this, frameLayoutContenedorFragmento);  // Helper para el menú principal
        helperNavegacionInferior = new HelperNavegacionInferior(AdministradorActivity.this, chipNavigationBarNavegacionInferior, helperMenuPrincipal);  // Helper para la navegación inferior

        helperPerfil = new HelperPerfil(baseDeDatosGestionUsuarios);  // Helper para gestionar el perfil
        helperAjustes = new HelperAjustes(helperMenuPrincipal, helperNavegacionInferior);  // Helper para gestionar los ajustes
    }

    /**
     * Carga las opciones de la barra de navegación inferior.
     * Se configuran las opciones del menú para navegar entre los diferentes fragmentos.
     */
    private void cargarOpcionesNavegacionInferior(){
        Map<Integer, Fragment> opcionesDeMenu = new HashMap<>();  // Mapa de las opciones de menú
        opcionesDeMenu.put(R.id.menuPrincipal, new AdministradorMenuPrincipalFragment());  // Fragmento del menú principal
        opcionesDeMenu.put(R.id.opcionPefil, new AdministradorPerfilFragment());  // Fragmento de perfil
        opcionesDeMenu.put(R.id.opcionAjustes, new AdministradorAjustesFragment());  // Fragmento de ajustes

        // Utiliza el helper para configurar la navegación inferior con las opciones
        helperNavegacionInferior.configurarNavegacionInferior(opcionesDeMenu);
    }

    /**
     * Carga el fragmento del menú principal por defecto.
     * Se usa el helper para cargar el fragmento inicial de la actividad.
     */
    public void cargarMenuPrincipalPorDefecto(){
        helperMenuPrincipal.cargarFragmento(new AdministradorMenuPrincipalFragment());  // Carga el fragmento del menú principal
    }

    /**
     * Método que permite volver al fragmento del menú principal desde cualquier otro fragmento.
     */
    public void volverMenuPrincipal(){
        helperMenuPrincipal.cargarFragmento(new AdministradorMenuPrincipalFragment());  // Carga el fragmento del menú principal
        helperNavegacionInferior.seleccionarItemMenuPrincipal();  // Selecciona el ítem correspondiente en la barra de navegación inferior
    }

    /**
     * Obtiene el correo del usuario registrado desde el intent de inicio de sesión.
     *
     * @return El correo del usuario.
     */
    public String getCorreo(){
        return getIntent().getStringExtra("correo");  // Obtiene el correo del intent
    }

    /**
     * Getter para obtener el helper del menú principal.
     *
     * @return El helper del menú principal.
     */
    public HelperMenuPrincipal getHelperMenuPrincipal() {
        return helperMenuPrincipal;
    }

    /**
     * Getter para obtener el helper de la navegación inferior.
     *
     * @return El helper de la navegación inferior.
     */
    public HelperNavegacionInferior getHelperNavegacionInferior() {
        return helperNavegacionInferior;
    }

    /**
     * Getter para obtener el helper de perfil.
     *
     * @return El helper del perfil.
     */
    public HelperPerfil getHelperPerfil() {
        return helperPerfil;
    }

    /**
     * Getter para obtener el helper de ajustes.
     *
     * @return El helper de ajustes.
     */
    public HelperAjustes getHelperAjustes() {
        return helperAjustes;
    }
}



