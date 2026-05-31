package com.example.tp_inmobiliariaceli.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp_inmobiliariaceli.databinding.FragmentInquilinoDetalleBinding;
import com.example.tp_inmobiliariaceli.modelo.Inquilino;

public class InquilinoDetalleFragment extends Fragment {
    private InquilinoDetalleViewModel viewModel;
    private FragmentInquilinoDetalleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(InquilinoDetalleViewModel.class);
        binding = FragmentInquilinoDetalleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        configurarObservadores();

        if (getArguments() != null) {
            int idInmueble = getArguments().getInt("idInmueble", -1);
            if (idInmueble != -1) {
                viewModel.cargarInquilino(idInmueble);
            }
        }

        return root;
    }

    private void configurarObservadores() {
        viewModel.getInquilino().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
            @Override
            public void onChanged(Inquilino inquilino) {
                if (inquilino != null) {
                    binding.tvDetalleCodigo.setText(String.valueOf(inquilino.getIdInquilino()));
                    binding.tvDetalleNombre.setText(inquilino.getNombre());
                    binding.tvDetalleApellido.setText(inquilino.getApellido());
                    binding.tvDetalleDni.setText(inquilino.getDni());
                    binding.tvDetalleEmail.setText(inquilino.getEmail());
                    binding.tvDetalleTelefono.setText(inquilino.getTelefono());

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
