package com.example.practica_2ev_pmdm_robingonzalez.administrativo;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.CocheUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.UsuarioUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Coche;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AdministrativoRegistroCochesFragment extends Fragment {


    private ImageView imageViewVolver;
    private AdministrativoActivity activityAdministrativo;
    private TextInputEditText editTextMarcaCoche, editTextModeloCoche, editTextMatriculaCoche;
    private Spinner spinnerAsignarCocheMj;
    private MaterialButton buttonGuardarRegistro;
    private ArrayAdapter<String> spinnerAdapter;
    private List<String> nombresMecanicosJefe;
    private List<String> correosMecanicosJefe;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del layout de registro de coches
        View vista = inflater.inflate(R.layout.administrativo_registro_coches_fragment, container, false);

        inicializarComponentes(vista);
        obtenerHelper();
        volverMenuPrincipalDesdeRegistroCoches();
        configurarSpinner();
        cargarNombreMecanicoJefe();
        guardarRegistroCocheEnFirebase();

        return vista;
    }




    private void inicializarComponentes(View vista) {
        imageViewVolver = vista.findViewById(R.id.imageViewVolverMenuPrincipalRegistroCoches);
        editTextMarcaCoche = vista.findViewById(R.id.editTextMarcaCoche);
        editTextModeloCoche = vista.findViewById(R.id.editTextModeloCoche);
        editTextMatriculaCoche = vista.findViewById(R.id.editTextMatriculaCoche);
        spinnerAsignarCocheMj = vista.findViewById(R.id.spinnerAsignarCocheMecanicoJefe);
        buttonGuardarRegistro = vista.findViewById(R.id.buttonGuardarRegistroCoche);
        nombresMecanicosJefe = new ArrayList<>();
        correosMecanicosJefe = new ArrayList<>();

    }

    private void obtenerHelper(){
        if(getActivity() instanceof AdministrativoActivity) {
            activityAdministrativo = ((AdministrativoActivity) getActivity());
        } else {
            Log.e("AdministradorGestionEmpleadosFragment", "Error al obtener helper");
        }
    }


    private void volverMenuPrincipalDesdeRegistroCoches(){
        imageViewVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityAdministrativo.volverMenuPrincipal();
            }
        });
    }


    private void configurarSpinner() {
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresMecanicosJefe);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAsignarCocheMj.setAdapter(spinnerAdapter);
    }


    private void cargarNombreMecanicoJefe(){
        UsuarioUtil.cargarUsuariosPorTipo("Mecanico jefe", new UsuarioUtil.usuariosCargadosListener() {
            @Override
            public void onUsuariosCargados(List<Usuario> usuarios) {
                nombresMecanicosJefe.clear(); // Limpiar la lista de nombres
                correosMecanicosJefe.clear(); // Limpiar la lista de correos s
                for (Usuario usuario : usuarios) {
                    if (usuario.getNombre() != null && usuario.getApellidos() != null && usuario.getCorreo() != null) {
                        String nombreCompleto = usuario.getNombre() + "  " + usuario.getApellidos();
                        nombresMecanicosJefe.add(nombreCompleto);
                        correosMecanicosJefe.add(usuario.getCorreo());
                    }
                }
                spinnerAdapter.notifyDataSetChanged(); // Actualizar el Spinner con los nuevos datos
            }

            @Override
            public void onError(Exception e) {
                Log.e("AdministrativoRegistroCochesFragment", "Error al cargar mecánicos jefe: ");
            }
        });
    }


    private void guardarRegistroCocheEnFirebase() {
        buttonGuardarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String marca = editTextMarcaCoche.getText().toString().trim();
                String modelo = editTextModeloCoche.getText().toString().trim();
                String matricula = editTextMatriculaCoche.getText().toString().trim();

                // Obtener el índice del mecánico jefe seleccionado en el Spinner
                int mecanicoSeleccionado = spinnerAsignarCocheMj.getSelectedItemPosition();

                if (mecanicoSeleccionado != -1 && !marca.isEmpty() && !modelo.isEmpty() && !matricula.isEmpty()) {
                    // Obtener el correo del mecánico jefe seleccionado
                    String correoMecanicoJefe = correosMecanicosJefe.get(mecanicoSeleccionado);

                    // Crear el objeto coche con los datos
                    Coche coche = new Coche(marca, modelo, matricula, correoMecanicoJefe);

                    // Guardar en Firebase (asume que tienes un método en FirebaseHelper para guardar el coche)
                    CocheUtil.guardarCocheEnFirebase(coche);

                    Snackbar.make(v, "Registro completado con éxito del coche  " + marca + "  " + modelo, Snackbar.LENGTH_LONG).show();

                } else {
                    Snackbar.make(v, "Rellena todos los campos para realizar el registro correctamente", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

}
