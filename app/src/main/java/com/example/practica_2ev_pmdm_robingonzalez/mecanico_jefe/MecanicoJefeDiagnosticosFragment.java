package com.example.practica_2ev_pmdm_robingonzalez.mecanico_jefe;



import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.adaptadores.ReparacionAdapter;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.ReparacionUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
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


public class MecanicoJefeDiagnosticosFragment extends Fragment implements  ReparacionAdapter.OnItemClickListener {

    private ImageView imageViewVolver;
    private MecanicoJefeActivity mecanicoJefeActivity;
    private RecyclerView recyclerViewDiagnostico;
    private ReparacionAdapter reparacionAdapter;
    private List<Reparacion> listaReparacion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
    }

    private void obtenerHelper() {
        if (getActivity() instanceof MecanicoJefeActivity) {
            mecanicoJefeActivity = ((MecanicoJefeActivity) getActivity());
        } else {
            Log.e("AdministrativoReparacionesFragment", "Error al obtener helper");
        }
    }


    private void volverMenuPrincipalDesdeDiagnosticos(){
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

                for (DataSnapshot reparacionSnapshot : snapshot.getChildren()) {
                    Reparacion reparacion = reparacionSnapshot.getValue(Reparacion.class);
                    if (reparacion != null && correoMecanicoJefe.equals(reparacion.getCorreoMecanicoJefe())) {
                        // Filtrar solo las reparaciones que corresponden al correo del mecánico jefe
                        listaReparacion.add(reparacion);
                        Log.d("MecanicoJefeDiagnosticosFragment", "Reparación cargada: " + reparacion.getMatriculaCoche());
                    }
                }

                // Notificar al adaptador que los datos han cambiado
                reparacionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MecanicoJefeDiagnosticosFragment", "Error al cargar reparaciones: " + error.getMessage());
            }
        };

        // Llamar a la utilidad para cargar las reparaciones sin ningún filtro
        ReparacionUtil.cargarReparaciones(listener);
    }

    @Override
    public void onItemClick(Reparacion reparacion){
        iniciarDiagnostico(reparacion);
    }

    private void configurarRecyclerView(){
        // Configurar el adaptador solo una vez
        listaReparacion = new ArrayList<>();
        reparacionAdapter = new ReparacionAdapter(listaReparacion, getContext(),
                MecanicoJefeDiagnosticosFragment.this);
        recyclerViewDiagnostico.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDiagnostico.setAdapter(reparacionAdapter);

    }

    private void iniciarDiagnostico(Reparacion reparacion) {
        MaterialAlertDialogBuilder builderDiagnostico = new MaterialAlertDialogBuilder(getContext());
        View vistaDialogo = LayoutInflater.from(getContext()).inflate(R.layout.mecanico_jefe_insertar_diagnostico_dialog, null);

        // Inicializar AutoCompleteTextView
        AutoCompleteTextView autoCompleteDiagnostico = vistaDialogo.findViewById(R.id.autoCompleteTextViewDiagnostico);

        // El DatePicker se encuentra en el XML
        DatePicker datePickerDiagnosticos = vistaDialogo.findViewById(R.id.datePickerDiagnosticos);

        // Crear un adaptador para el AutoCompleteTextView
        String[] diagnosticosArray = getResources().getStringArray(R.array.diagnosticos_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, diagnosticosArray);
        autoCompleteDiagnostico.setAdapter(adapter);

        // Configurar el click en el DatePicker
        datePickerDiagnosticos.setOnClickListener(v -> configurarDatePickerDialog(datePickerDiagnosticos));

        builderDiagnostico.setView(vistaDialogo)
                .setTitle("Diagnóstico y fecha fin de reparación")
                .setIcon(R.drawable.ic_diagnostico)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String diagnosticoSeleccionado = autoCompleteDiagnostico.getText().toString().trim();

                    // Convertir la fecha del DatePicker a string para mostrarla
                    int year = datePickerDiagnosticos.getYear();
                    int month = datePickerDiagnosticos.getMonth();
                    int day = datePickerDiagnosticos.getDayOfMonth();
                    String fechaFinDiagnostico = day + "/" + (month + 1) + "/" + year;

                    if (!diagnosticoSeleccionado.isEmpty() && Arrays.asList(diagnosticosArray).contains(diagnosticoSeleccionado) && !fechaFinDiagnostico.isEmpty()) {
                        // Guardar diagnóstico y fecha de finalización en Firebase
                        guardarDiagnosticoEnFirebase(reparacion, diagnosticoSeleccionado, fechaFinDiagnostico);
                    } else {
                        Snackbar.make(recyclerViewDiagnostico, "Seleccione un diagnóstico válido y una fecha de finalización.", Snackbar.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builderDiagnostico.show();
    }


    private void configurarDatePickerDialog(DatePicker datePicker) {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Establecer la fecha mínima que el usuario puede elegir (solo fechas futuras)
        calendar.add(Calendar.DATE, 1); // Evitar que se seleccione el mismo día o días anteriores
        long minDate = calendar.getTimeInMillis();

        // Configurar el DatePicker para la fecha actual
        datePicker.updateDate(year, month, day);

        // Establecer la fecha mínima que el usuario puede elegir
        datePicker.setMinDate(minDate);
    }

    private void guardarDiagnosticoEnFirebase(Reparacion reparacion, String diagnosticoSeleccionado, String fechaFinDiagnostico) {
        // Convertir la fecha de fin (string) a un Timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(fechaFinDiagnostico);
            long timestamp = date != null ? date.getTime() : 0;

            // Comprobar si la fecha seleccionada es anterior a la fecha actual
            Calendar currentCalendar = Calendar.getInstance();
            long currentTimeInMillis = currentCalendar.getTimeInMillis();

            if (timestamp < currentTimeInMillis) {
                // Mostrar un Snackbar de advertencia y cancelar la actualización
                Snackbar.make(recyclerViewDiagnostico,
                        "¡La fecha seleccionada no es válida! Selecciona una fecha futura", Snackbar.LENGTH_LONG).show();
                return; // Salir del método y no continuar con la actualización
            }

            // Llamar a la utilidad de ReparacionUtil para actualizar el tipo de reparación y fecha de finalización
            ReparacionUtil.actualizarTipoReparacion(reparacion.getCorreoMecanicoJefe(), reparacion.getMatriculaCoche(), diagnosticoSeleccionado);
            ReparacionUtil.actualizarFechaFinDiagnostico(timestamp, reparacion.getCorreoMecanicoJefe(), reparacion.getMatriculaCoche());

            // Notificar al usuario que el diagnóstico se ha guardado
            Snackbar.make(recyclerViewDiagnostico, "Diagnóstico guardado correctamente.", Snackbar.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
            Snackbar.make(recyclerViewDiagnostico, "Error al procesar la fecha.", Snackbar.LENGTH_LONG).show();
        }
    }

}
