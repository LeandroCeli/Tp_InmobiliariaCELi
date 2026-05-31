package com.example.tp_inmobiliariaceli.ui.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp_inmobiliariaceli.databinding.FragmentContratoDetalleBinding;

public class ContratoDetalleFragment extends Fragment {
    private ContratoDetalleViewModel viewModel;
    private FragmentContratoDetalleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ContratoDetalleViewModel.class);
        binding = FragmentContratoDetalleBinding.inflate(inflater, container, false);

        // Nos suscribimos al LiveData del ViewModel para enterarnos cuando llegue el contrato
        viewModel.getContrato().observe(getViewLifecycleOwner(), contrato -> {
            if (contrato != null) {
                // Seteamos los datos en la interfaz usando los IDs exactos del XML corregido
                binding.tvContratoCodigo.setText(String.valueOf(contrato.getIdContrato()));
                binding.tvContratoFechaInicio.setText(contrato.getFechaInicio());
                binding.tvContratoFechaFin.setText(contrato.getFechaFin());
                binding.tvContratoMonto.setText("$ " + contrato.getMonto());

                if (contrato.getInquilino() != null) {
                    binding.tvContratoInquilino.setText(contrato.getInquilino().getNombre() + " " + contrato.getInquilino().getApellido());
                }

                if (contrato.getInmueble() != null) {
                    binding.tvContratoInmueble.setText(contrato.getInmueble().getDireccion());
                }
            }
        });

        // Recuperamos el ID del inmueble enviado desde el fragmento anterior
        if (getArguments() != null) {
            int idInmueble = getArguments().getInt("idInmueble", -1);
            if (idInmueble != -1) {
                viewModel.cargarContrato(idInmueble);
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}