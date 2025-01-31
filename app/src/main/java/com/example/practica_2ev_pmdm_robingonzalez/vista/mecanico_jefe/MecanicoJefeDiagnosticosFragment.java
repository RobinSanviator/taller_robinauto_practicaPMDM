package com.example.practica_2ev_pmdm_robingonzalez.vista.mecanico_jefe;



import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.ReparacionAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.ReparacionUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MecanicoJefeDiagnosticosFragment extends Fragment implements ReparacionAdapter.OnItemClickListener {

    private ImageView imageViewVolver;
    private MecanicoJefeActivity mecanicoJefeActivity;
    private RecyclerView recyclerViewDiagnostico;
    private ReparacionAdapter reparacionAdapter;
    private List<Reparacion> listaReparacion;
    private CheckBox checkBoxDiagnosticados, checkBoxNoDiagnosticados;
    private TextView textViewInfoSeleccionaParaDiagnosticar, textViewNoHayCochesParaDiagnosticar, textViewMostrarLista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar diseño del layout de los diagnósticos
        View vista = inflater.inflate(R.layout.mecanico_jefe_diagnosticos_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeDiagnosticos();
        configurarRecyclerView();
        cargarReparaciones();

        return vista;
    }

    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalDiagnosticos);
        recyclerViewDiagnostico = vista.findViewById(R.id.recyclerViewDiagnosticos);
        checkBoxDiagnosticados = vista.findViewById(R.id.checkBoxDiagnosticados);
        checkBoxNoDiagnosticados = vista.findViewById(R.id.checkBoxNoDiagnosticados);
        textViewInfoSeleccionaParaDiagnosticar = vista.findViewById(R.id.textViewInfoSeleccionaParaDiagnosticar);
        textViewNoHayCochesParaDiagnosticar = vista.findViewById(R.id.textViewNoHayCochesParaDiagnosticar);
        textViewMostrarLista = vista.findViewById(R.id.textViewMostrarListaReparaciones);

        // Configurar el listener para los checkboxes
        checkBoxDiagnosticados.setOnCheckedChangeListener((buttonView, isChecked) -> cargarReparaciones());
        checkBoxNoDiagnosticados.setOnCheckedChangeListener((buttonView, isChecked) -> cargarReparaciones());
    }

    private void obtenerHelper() {
        if (getActivity() instanceof MecanicoJefeActivity) {
            mecanicoJefeActivity = (MecanicoJefeActivity) getActivity();
        } else {
            Log.e("MecanicoJefeDiagnosticosFragment", "Error al obtener helper");
        }
    }

    private void volverMenuPrincipalDesdeDiagnosticos() {
        imageViewVolver.setOnClickListener(v -> mecanicoJefeActivity.volverMenuPrincipal());
    }

    private void cargarReparaciones() {
        // Obtener el correo del mecánico jefe desde el helper de la actividad
        String correoMecanicoJefe = mecanicoJefeActivity.getCorreo();

        // Crear el ValueEventListener para cargar las reparaciones
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaReparacion.clear(); // Limpiar la lista antes de agregar nuevos datos

                // Verificar qué CheckBox está seleccionado
                boolean mostrarPendientes = checkBoxNoDiagnosticados.isChecked();  // "No diagnosticados"
                boolean mostrarDiagnosticados = checkBoxDiagnosticados.isChecked();  // "Diagnosticados"

                // Iterar sobre las reparaciones
                for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                    Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                    if (reparacion != null && correoMecanicoJefe.equals(reparacion.getCorreoMecanicoJefe())) {
                        // Verifica las reparaciones basadas en los checkboxes seleccionados
                        boolean esPendiente = "Pendiente".equalsIgnoreCase(reparacion.getTipoReparacion());
                        boolean esDiagnosticada = !"Pendiente".equalsIgnoreCase(reparacion.getTipoReparacion());

                        // Si se deben mostrar las reparaciones pendientes o diagnosticadas
                        if ((mostrarPendientes && esPendiente) || (mostrarDiagnosticados && esDiagnosticada)) {
                            listaReparacion.add(reparacion);
                        }
                    }
                }

                // Notificar al adaptador que los datos han cambiado
                reparacionAdapter.notifyDataSetChanged();

                // Verificar si hay reparaciones para mostrar el TextView
                verificarReparacionPorDiagnosticar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MecanicoJefeDiagnosticosFragment", "Error al cargar reparaciones: " + error.getMessage());
            }
        };

        // Llamar al metodo para cargar las reparaciones
        ReparacionUtil.cargarReparaciones(listener);
    }

    private void verificarReparacionPorDiagnosticar() {
        // Lógica para verificar el estado de los checkbox y las reparaciones
        if (checkBoxNoDiagnosticados.isChecked() && checkBoxDiagnosticados.isChecked()) {
            mostrarMensaje("Para editar diagnóstico, selecciona una reparación", R.color.color_editar, View.VISIBLE, View.GONE, View.VISIBLE);
        } else if (checkBoxNoDiagnosticados.isChecked() && listaReparacion.isEmpty()) {
            mostrarMensaje("", 0, View.GONE, View.VISIBLE, View.GONE);
        } else if (checkBoxDiagnosticados.isChecked() && !listaReparacion.isEmpty()) {
            mostrarMensaje("Para editar diagnóstico, selecciona una reparación", R.color.color_editar, View.VISIBLE, View.GONE, View.GONE);
        } else {
            mostrarMensaje("Selecciona los checkbox para consultar las reparaciones", R.color.color_degradado2_cardview2, View.VISIBLE, View.GONE, View.GONE);
        }
    }

    // Método para aplicar el mensaje, fondo y visibilidad de los TextViews
    private void mostrarMensaje(String mensaje, int fondo, int visibilidadInfo,
                                int visibilidadNoHayCoches, int visibilidadMostrarRep) {
        textViewInfoSeleccionaParaDiagnosticar.setText(mensaje);
        if (fondo != 0) {  // Solo aplicar fondo si se pasa un valor distinto de 0
            textViewInfoSeleccionaParaDiagnosticar.setBackgroundResource(fondo);
        }
        textViewInfoSeleccionaParaDiagnosticar.setVisibility(visibilidadInfo);
        textViewNoHayCochesParaDiagnosticar.setVisibility(visibilidadNoHayCoches);
        textViewMostrarLista.setVisibility(visibilidadMostrarRep);
    }

    @Override
    public void onItemClick(Reparacion reparacion) {
        iniciarDiagnostico(reparacion);
    }

    private void configurarRecyclerView() {

        listaReparacion = new ArrayList<>();
        reparacionAdapter = new ReparacionAdapter(listaReparacion, getContext(), MecanicoJefeDiagnosticosFragment.this);
        recyclerViewDiagnostico.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDiagnostico.setAdapter(reparacionAdapter);
    }


    private void iniciarDiagnostico(Reparacion reparacion) {
        MaterialAlertDialogBuilder builderDiagnostico = new MaterialAlertDialogBuilder(getContext());
        View vistaDialogo = LayoutInflater.from(getContext()).inflate(R.layout.mecanico_jefe_insertar_diagnostico_dialog, null);

        // Inicializar elementos en la vista
        AutoCompleteTextView autoCompleteDiagnostico = vistaDialogo.findViewById(R.id.autoCompleteTextViewDiagnostico);
        TextInputEditText textInputEditTextPresupuesto = vistaDialogo.findViewById(R.id.editTextPresupuesto);


        // Configurar los valores existentes
        if (reparacion.getTipoReparacion() != null) {
            autoCompleteDiagnostico.setText(reparacion.getTipoReparacion());
        }

        // Verificar si el presupuesto no es null y tiene un valor válido
        if (reparacion.getPresupuesto() != null && reparacion.getPresupuesto() != -1) {
            textInputEditTextPresupuesto.setText(String.valueOf(reparacion.getPresupuesto()));
        } else {
            textInputEditTextPresupuesto.setText(""); // Si no hay presupuesto, dejar vacío el campo
        }


        // Crear un adaptador para el AutoCompleteTextView
        String[] diagnosticosArray = getResources().getStringArray(R.array.diagnosticos_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, diagnosticosArray);
        autoCompleteDiagnostico.setAdapter(adapter);

        builderDiagnostico.setView(vistaDialogo)
                .setTitle("Diagnóstico, presupuesto y fecha fin")
                .setIcon(R.drawable.ic_diagnostico)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String diagnosticoSeleccionado = autoCompleteDiagnostico.getText().toString().trim();



                    // Obtener el presupuesto
                    Double presupuesto = obtenerPresupuesto(textInputEditTextPresupuesto);
                    if (presupuesto == -1) {
                        return; // Salir si hay error con el presupuesto
                    }

                    if (!diagnosticoSeleccionado.isEmpty() && Arrays.asList(diagnosticosArray).contains(diagnosticoSeleccionado)) {
                        // Guardar diagnóstico, presupuesto y fecha de finalización en Firebase
                        guardarDiagnosticoEnFirebase(reparacion, diagnosticoSeleccionado, presupuesto);
                    } else {
                        Snackbar.make(vistaDialogo, "Seleccione un diagnóstico válido", Snackbar.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builderDiagnostico.show();
    }

    // Método para obtener el presupuesto
    private double obtenerPresupuesto(EditText editTextPresupuesto) {
        String presupuestoTexto = editTextPresupuesto.getText().toString().trim();

        if (presupuestoTexto.isEmpty()) {
            Snackbar.make(getActivity().findViewById(android.R.id.content),
                    "Se debe insertar un presupuesto para realizar la nueva reparación",
                    Snackbar.LENGTH_LONG).show();
            return -1; // Retornamos un valor que indica que hubo un error
        }

        try {
            // Verifica si el presupuesto es válido y no nulo
            Double presupuesto = Double.valueOf(presupuestoTexto);
            return presupuesto != null ? presupuesto : -1; // Si el presupuesto es null, retornamos -1
        } catch (NumberFormatException e) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "El presupuesto debe ser un número válido",
                    Snackbar.LENGTH_SHORT).show();
            return -1; // Retornamos un valor que indica que hubo un error
        }
    }



    private void guardarDiagnosticoEnFirebase(Reparacion reparacion, String diagnosticoSeleccionado, double presupuesto) {

            // Actualizar los campos en Firebase
            ReparacionUtil.actualizarTipoReparacion(reparacion.getCorreoMecanicoJefe(), reparacion.getMatriculaCoche(), diagnosticoSeleccionado);
            ReparacionUtil.actualizarPresupuesto(reparacion.getCorreoMecanicoJefe(), reparacion.getMatriculaCoche(), presupuesto);

            // Notificar al usuario que el diagnóstico se ha guardado
            Snackbar.make(recyclerViewDiagnostico, "Diagnóstico guardado correctamente.", Snackbar.LENGTH_SHORT).show();
    }

}
