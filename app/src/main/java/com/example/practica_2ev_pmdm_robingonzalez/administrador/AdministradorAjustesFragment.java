package com.example.practica_2ev_pmdm_robingonzalez.administrador;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;


public class AdministradorAjustesFragment extends Fragment {

    private MaterialButton buttonSeleccionarTema;
    private RadioGroup radioGroupTema;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_administrador_ajustes, container, false);

        buttonSeleccionarTema = view.findViewById(R.id.buttonSeleccionarTema);
        elegirTema();

        return view;
    }

    public void elegirTema(){
        buttonSeleccionarTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDialogoEleccionTema();
            }
        });
    }

    public void abrirDialogoEleccionTema() {
        // Inflar el diseño XML para mostrarlo al pulsar el botón de Registrarse
        LayoutInflater inflater = getLayoutInflater();
        View vistaDialogo = inflater.inflate(R.layout.alert_dialog_seleccionar_tema, null);

        // Configurar el AlertDialog
        AlertDialog.Builder builderTema = new AlertDialog.Builder(requireContext());
        builderTema.setTitle("Elegir tema");
        builderTema.setView(vistaDialogo);

        radioGroupTema = vistaDialogo.findViewById(R.id.radioGroupTema);

        // Configurar botón "Aceptar"
        builderTema.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Aplicar el tema seleccionado
                temaSeleccionado(radioGroupTema, vistaDialogo);
                dialog.dismiss(); // Cerrar el diálogo
            }
        });

        builderTema.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        builderTema.create().show();
    }

    public void temaSeleccionado(RadioGroup radioGroup, View vistaDialogo) {

        int idSeleccionado = radioGroup.getCheckedRadioButtonId();

        if(idSeleccionado == R.id.radioButtonTemaOscuro){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else if(idSeleccionado == R.id.radioButtonTemaClaro){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }else if(idSeleccionado == R.id.radioButtonTemaPorDefecto){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        }

    }
}