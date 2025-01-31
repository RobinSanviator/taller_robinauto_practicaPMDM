package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.vista.administrador.AdministradorMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.vista.administrativo.AdministrativoMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.vista.cliente.ClienteMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.vista.inicio_sesion.InicioSesionActivity;
import com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico.MecanicoMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico_jefe.MecanicoJefeMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * HelperAjustes es una clase que gestiona la configuración de ajustes de usuario,
 * como la visualización del nombre del usuario desde Firebase, el modo oscuro,
 * y la carga de fragmentos según el tipo de usuario.
 */
public class HelperAjustes {

    private HelperMenuPrincipal helperMenuPrincipal;
    private HelperNavegacionInferior helperNavegacionInferior;

    /**
     * Constructor de HelperAjustes que inicializa los objetos de HelperMenuPrincipal
     * y HelperNavegacionInferior.
     *
     * @param helperMenuPrincipal Instancia de HelperMenuPrincipal
     * @param helperNavegacionInferior Instancia de HelperNavegacionInferior
     */
    public HelperAjustes(HelperMenuPrincipal helperMenuPrincipal, HelperNavegacionInferior helperNavegacionInferior){
        this.helperMenuPrincipal = helperMenuPrincipal;
        this.helperNavegacionInferior = helperNavegacionInferior;
    }

    /**
     * Carga el nombre completo del usuario desde Firebase usando su correo.
     *
     * @param correo El correo del usuario a buscar en Firebase
     * @param textViewNombreCabecera El TextView donde se mostrará el nombre completo
     */
    public void cargarNombreCabeceraDesdeFirebase(String correo, TextView textViewNombreCabecera){
        // Obtener la referencia a la base de datos de Firebase
        DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();

        // Buscar el usuario por correo en la base de datos de Firebase
        usuariosRef.orderByChild("correo").equalTo(correo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Obtener el usuario desde Firebase
                                Usuario usuario = snapshot.getValue(Usuario.class);

                                if (usuario != null) {
                                    // Concatenar nombre y apellidos
                                    String nombreCompleto = usuario.getNombre() + " " + usuario.getApellidos();
                                    // Asignar el nombre completo
                                    textViewNombreCabecera.setText(nombreCompleto);
                                }
                            }
                        } else {
                            Log.d("FirebaseQuery", "No se encontraron usuarios con ese correo.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseQuery", "Error al obtener datos: " + databaseError.getMessage());
                    }
                });
    }

    /**
     * Configura el cambio de modo oscuro y gestiona la visibilidad de un ProgressBar
     * mientras se realiza el cambio.
     *
     * @param correo El correo del usuario
     * @param switchCompatBotonModoOscuro El SwitchCompat para activar o desactivar el modo oscuro
     * @param progressBarModoOscuro El ProgressBar que se muestra mientras se realiza el cambio
     * @param context El contexto de la actividad
     */
    public void modoOscuro(String correo, SwitchCompat switchCompatBotonModoOscuro, ProgressBar progressBarModoOscuro, Context context) {
        switchCompatBotonModoOscuro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Mostrar ProgressBar mientras se realiza el cambio de tema
                mostrarProgressBar(progressBarModoOscuro);

                if (context instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) context;
                    // Verificar que la actividad no esté destruida ni finalizando
                    if (!activity.isFinishing() && !activity.isDestroyed()) {
                        // Realizar el cambio de modo
                        if (isChecked) {
                            activarModoOscuro();
                        } else {
                            desactivarModoOscuro();
                        }

                        // Guardar la preferencia del modo oscuro
                        guardarPreferenciaModoOscuro(isChecked, context);

                        // Hacer la ProgressBar invisible después de hacer el cambio de tema
                        ocultarProgressBar(progressBarModoOscuro);

                        // Cargar el fragmento y seleccionar el item del menú
                        cargarFragmentoSegunTipoUsuario(correo);
                        helperNavegacionInferior.seleccionarItemMenuPrincipal();
                    } else {
                        Log.e("HelperAjustes", "No se pudo cargar el fragmento");
                    }
                }
            }
        });
    }

    /**
     * Carga un fragmento según el tipo de usuario (Administrador, Administrativo, etc.)
     *
     * @param tipoUsuario El tipo de usuario que determina qué fragmento cargar
     */
    private void cargarFragmento(String tipoUsuario) {
        Fragment fragmento;
        switch (tipoUsuario) {
            case "Administrador":
                fragmento = new AdministradorMenuPrincipalFragment();
                break;
            case "Administrativo":
                fragmento = new AdministrativoMenuPrincipalFragment();
                break;
            case "Mecanico jefe":
                fragmento = new MecanicoJefeMenuPrincipalFragment();
                break;
            case "Mecanico":
                fragmento = new MecanicoMenuPrincipalFragment();
                break;
            case "Cliente":
                fragmento = new ClienteMenuPrincipalFragment();
                break;
            default:
                Log.e("HelperAjustes", "TipoUsuario desconocido: " + tipoUsuario);
                return;
        }

        if (helperMenuPrincipal != null && helperNavegacionInferior != null) {
            helperMenuPrincipal.cargarFragmento(fragmento);
        } else {
            Log.e("HelperAjustes", "HelperFragmento no está inicializado.");
        }
    }

    /**
     * Carga un fragmento dependiendo del tipo de usuario recuperado desde Firebase.
     *
     * @param correo El correo del usuario cuya información se usará para cargar el fragmento adecuado
     */
    public void cargarFragmentoSegunTipoUsuario(String correo) {
        DatabaseReference usuariosRef = FirebaseUtil.getDatabaseReference();
        usuariosRef.orderByChild("correo").equalTo(correo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Usuario usuario = snapshot.getValue(Usuario.class);

                                if (usuario != null && usuario.getTipoUsuario() != null) {
                                    // Llamar al método para cargar el fragmento con tipo de usuario obtenido
                                    cargarFragmento(usuario.getTipoUsuario());
                                } else {
                                    Log.e("FirebaseQuery", "El usuario no tiene un rol definido.");
                                }
                            }
                        } else {
                            Log.d("FirebaseQuery", "No se encontraron usuarios con ese correo.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseQuery", "Error al obtener datos: " + databaseError.getMessage());
                    }
                });
    }

    /**
     * Muestra el ProgressBar con una animación de entrada.
     *
     * @param progressBarModoOscuro El ProgressBar que se desea mostrar
     */
    private static void mostrarProgressBar(ProgressBar progressBarModoOscuro) {
        progressBarModoOscuro.setVisibility(ProgressBar.VISIBLE);
        progressBarModoOscuro.animate()
                .alpha(1f)
                .setDuration(2000)
                .start();
    }

    /**
     * Oculta el ProgressBar con una animación de desvanecimiento.
     *
     * @param progressBarModoOscuro El ProgressBar que se desea ocultar
     */
    private static void ocultarProgressBar(ProgressBar progressBarModoOscuro) {
        progressBarModoOscuro.animate()
                .alpha(0f)
                .setDuration(1000)
                .withEndAction(() -> { // Acción que se ejecuta después de terminar la animación
                    progressBarModoOscuro.setVisibility(ProgressBar.GONE); // Ocultar el ProgressBar completamente
                })
                .start();
    }

    /**
     * Activa el modo oscuro en la aplicación.
     */
    private void activarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    /**
     * Desactiva el modo oscuro en la aplicación.
     */
    private void desactivarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * Guarda la preferencia del modo oscuro en SharedPreferences.
     *
     * @param isChecked El estado del switch (si el modo oscuro está activado o no)
     * @param context El contexto de la actividad
     */
    private void guardarPreferenciaModoOscuro(boolean isChecked, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ajustes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("modoOscuro", isChecked);
        editor.apply();
    }

    /**
     * Carga la preferencia del modo oscuro desde SharedPreferences y aplica el modo correspondiente.
     * También configura el estado del Switch de acuerdo con la preferencia guardada.
     *
     * @param switchCompatBotonModoOscuro El switch que controla el modo oscuro.
     * @param context El contexto de la actividad desde la que se llama este método.
     */
    public void cargarPreferenciaModoOscuro(SwitchCompat switchCompatBotonModoOscuro, Context context) {
        // Obtener las preferencias del usuario guardadas
        SharedPreferences sharedPreferences = context.getSharedPreferences("ajustes", Context.MODE_PRIVATE);
        // Recuperar el valor guardado de la preferencia "modoOscuro", con un valor predeterminado de "false" (modo claro)
        boolean modoOscuroActivado = sharedPreferences.getBoolean("modoOscuro", false);

        // Aplicar el modo oscuro según la preferencia almacenada
        if (modoOscuroActivado) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Modo oscuro activado
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Modo claro activado
        }

        // Configurar el estado del switch de acuerdo con la preferencia guardada
        switchCompatBotonModoOscuro.setChecked(modoOscuroActivado);
    }

    /**
     * Muestra un diálogo de confirmación para cerrar sesión. Si el usuario confirma, se borra la sesión
     * y se redirige a la pantalla de inicio de sesión.
     *
     * @param context El contexto de la actividad desde la que se llama este método.
     */
    public void cerrarSesion(Context context) {
        // Crear el MaterialAlertDialog para mostrar un mensaje de advertencia
        MaterialAlertDialogBuilder builderCerrarSesion = new MaterialAlertDialogBuilder(context);
        builderCerrarSesion.setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setIcon(R.drawable.ic_cerrar_sesion)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Limpiar los datos de sesión guardados
                        limpiarDatosSesion(context);
                        // Cerrar sesión de Firebase
                        FirebaseAuth.getInstance().signOut();
                        // Redirigir al usuario a la pantalla de inicio de sesión
                        Intent intentInicioSesion = new Intent(context, InicioSesionActivity.class);

                        // Evitar que el usuario regrese a la actividad anterior al agregar flags
                        intentInicioSesion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        // Iniciar la actividad de inicio de sesión
                        context.startActivity(intentInicioSesion);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Si el usuario cancela, no hacer nada
                        dialog.dismiss();
                    }
                });

        // Mostrar el MaterialAlertDialog
        builderCerrarSesion.show();
    }

    /**
     * Muestra un diálogo de confirmación para salir de la aplicación. Si el usuario confirma, se cierra la app.
     *
     * @param context El contexto de la actividad desde la que se llama este método.
     */
    public void salir(Context context){
        // Crear el AlertDialog para mostrar un mensaje de advertencia
        MaterialAlertDialogBuilder builderSalir = new MaterialAlertDialogBuilder(context);
        builderSalir.setTitle("Salir")
                .setMessage("¿Estás seguro de que deseas salir de la app?")
                .setIcon(R.drawable.ic_salir)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Limpiar los datos de sesión
                        limpiarDatosSesion(context);
                        // Cerrar sesión de Firebase
                        FirebaseAuth.getInstance().signOut();
                        // Si el usuario confirma, se cierra la aplicación
                        if(context instanceof AppCompatActivity){
                            AppCompatActivity activity = (AppCompatActivity) context;
                            activity.finishAffinity(); // Termina todas las actividades y sale de la app
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Si el usuario cancela, no hacer nada
                        dialog.dismiss();
                    }
                });

        // Mostrar el AlertDialog
        builderSalir.show();
    }

    /**
     * Limpia los datos sensibles de la sesión guardados en SharedPreferences.
     *
     * @param contexto El contexto de la actividad desde la que se limpia la sesión.
     */
    private void limpiarDatosSesion(Context contexto) {
        // Obtener las preferencias de sesión
        SharedPreferences sharedPreferences = contexto.getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();  // Eliminar todas las preferencias guardadas
        editor.apply();  // Aplicar los cambios
    }

    /**
     * Muestra los términos y condiciones de la aplicación en un diálogo.
     *
     * @param contexto El contexto de la actividad desde la que se muestran los términos.
     */
    public void mostrarTerminosYCondiciones(Context contexto){
        MaterialAlertDialogBuilder builderTyC = new MaterialAlertDialogBuilder(contexto);
        LayoutInflater inflater = LayoutInflater.from(contexto);
        // Inflar la vista del diálogo de términos y condiciones
        View vistaDialogo = inflater.inflate(R.layout.alert_dialog_mostrar_terminos_condiciones, null);

        builderTyC.setTitle("Términos y condiciones de Taller Robinauto")
                .setIcon(R.drawable.ic_terminos_condiciones)
                .setView(vistaDialogo) // Establecer la vista inflada
                .setNegativeButton("Cerrar", (dialog, which) -> dialog.dismiss()) // Botón para cerrar
                .show(); // Mostrar el diálogo
    }

}
