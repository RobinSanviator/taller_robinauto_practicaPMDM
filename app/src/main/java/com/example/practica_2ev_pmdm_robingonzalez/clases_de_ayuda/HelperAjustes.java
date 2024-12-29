package com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.administrador.AdministradorMenuPrincipalFragment;
import com.example.practica_2ev_pmdm_robingonzalez.inicio_sesion.InicioSesionActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class HelperAjustes {

    private HelperFragmento helperFragmento;
    private HelperNavegacionInferior helperNavegacionInferior;

    public HelperAjustes(HelperFragmento helperFragmento, HelperNavegacionInferior helperNavegacionInferior){
        this.helperFragmento = helperFragmento;
        this.helperNavegacionInferior = helperNavegacionInferior;
    }



    // Método para configurar el modo oscuro y la visibilidad del ProgressBar
    public void modoOscuro(SwitchCompat switchCompatBotonModoOscuro, ProgressBar progressBarModoOscuro, Context context) {
        switchCompatBotonModoOscuro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchCompatBotonModoOscuro != null) {
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
                            cargarFragmentoEitem();
                        } else {
                            Log.e("HelperAjustes", "No se pudo cargar el fragmento");
                        }
                    }
                }
            }
        });
    }

    private void cargarFragmentoEitem(){
        Fragment menuPrincipal = new AdministradorMenuPrincipalFragment();
        if (helperFragmento != null && helperNavegacionInferior != null) {
            helperFragmento.cargarFragmento(new AdministradorMenuPrincipalFragment());
            helperNavegacionInferior.seleccionarItemMenuPrincipal();
        } else {
            Log.e("HelperAjustes", "HelperFragmento no está inicializado.");
        }
    }

    // Mostrar el ProgressBar con una animación de entrada
    private static void mostrarProgressBar(ProgressBar progressBarModoOscuro) {
        progressBarModoOscuro.setVisibility(ProgressBar.VISIBLE);
        progressBarModoOscuro.animate().alpha(1f).setDuration(2000);  // Animación para hacerlo visible suavemente
    }

    // Ocultar el ProgressBar con una animación de desvanecimiento
    private static void ocultarProgressBar(ProgressBar progressBarModoOscuro) {
        progressBarModoOscuro.animate().alpha(0f).setDuration(1000);  // Desvanecer ProgressBar
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarModoOscuro.setVisibility(ProgressBar.GONE); // Finalmente lo ocultamos completamente
            }
        }, 1000); // Esperamos a que termine la animación para ocultarlo
    }

    // Activar el modo oscuro
    private  void activarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    // Desactivar el modo oscuro
    private  void desactivarModoOscuro() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    // Guardar la preferencia del modo oscuro en SharedPreferences
    private void guardarPreferenciaModoOscuro(boolean isChecked, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ajustes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("modoOscuro", isChecked);
        editor.apply();
    }

    // Cargar la preferencia del modo oscuro desde SharedPreferences
    public  void cargarPreferenciaModoOscuro(SwitchCompat switchCompatBotonModoOscuro, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ajustes", Context.MODE_PRIVATE);
        boolean modoOscuroActivado = sharedPreferences.getBoolean("modoOscuro", false);

        // Aplicar el modo oscuro según la preferencia guardada
        if (modoOscuroActivado) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Configurar el estado del switch según la preferencia guardada
        switchCompatBotonModoOscuro.setChecked(modoOscuroActivado);
    }

    public void cerrarSesion(Context context) {
        // Crear el MaterialAlertDialog para mostrar un mensaje de advertencia
        MaterialAlertDialogBuilder builderCerrarSesion = new MaterialAlertDialogBuilder(context);
        builderCerrarSesion.setTitle("Cerrar sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setIcon(R.drawable.ic_cerrar_sesion)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Si el usuario confirma, se cierra la sesión y se redirige a la actividad de inicio de sesión
                        Intent intentInicioSesion = new Intent(context, InicioSesionActivity.class);
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

    public  void salir(Context context){
        // Crear el AlertDialog para mostrar un mensaje de advertencia
       MaterialAlertDialogBuilder builderSalir = new MaterialAlertDialogBuilder(context);
        builderSalir.setTitle("Salir")
                .setMessage("¿Estás seguro de que deseas salir de la app?")
                .setIcon(R.drawable.ic_salir)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Si el usuario confirma, se cierra sale de la app
                        if(context instanceof AppCompatActivity){
                          AppCompatActivity activityActividad = (AppCompatActivity) context;
                          activityActividad.finishAffinity();
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

}
