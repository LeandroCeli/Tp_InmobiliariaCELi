package com.example.tp_inmobiliariaceli.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.tp_inmobiliariaceli.databinding.FragmentInmuebleCrearBinding;
import com.example.tp_inmobiliariaceli.modelo.Inmueble;

public class InmuebleCrearFragment extends Fragment {
    private FragmentInmuebleCrearBinding binding;
    private InmuebleCrearViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleCrearBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(InmuebleCrearViewModel.class);

        binding.btnGuardarInmueble.setOnClickListener(v -> {
            // Se recomienda envolver en un try-catch por si el usuario deja el precio o ambiente vacío
            try {
                Inmueble nuevo = new Inmueble();
                nuevo.setDireccion(binding.etCrearDireccion.getText().toString());
                nuevo.setPrecio(Double.parseDouble(binding.etCrearPrecio.getText().toString()));
                nuevo.setAmbientes(Integer.parseInt(binding.etCrearAmbientes.getText().toString()));
                nuevo.setUso(binding.spinnerCrearUso.getSelectedItem().toString());
                nuevo.setTipo(binding.spinnerCrearTipo.getSelectedItem().toString());
                nuevo.setDisponible(true); // Requerimiento inicial

                // --- NUEVOS CAMPOS AGREGADOS PARA EVITAR EL ERROR 400 ---
                // Estos campos son obligatorios para la API aunque no estén en el formulario visual aún
                nuevo.setSuperficie(100);
                nuevo.setLatitud(-33.2950);
                nuevo.setLongitud(-66.3356);

                viewModel.crearInmueble(nuevo);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Complete todos los campos numéricos correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), msj ->
                Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show());

        viewModel.getCreado().observe(getViewLifecycleOwner(), creado -> {
            if (creado) Navigation.findNavController(requireView()).navigateUp();
        });

        return binding.getRoot();
    }
}