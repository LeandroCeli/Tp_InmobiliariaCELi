package com.example.tp_inmobiliariaceli.ui.perfil;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.tp_inmobiliariaceli.R;
import com.example.tp_inmobiliariaceli.databinding.FragmentPerfilBinding;

public class PerfilFragment extends Fragment {
    private PerfilViewModel viewModel;
    private FragmentPerfilBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        // 1. Observar estado de edición (habilita/deshabilita campos)
        viewModel.getEditando().observe(getViewLifecycleOwner(), estaEditando -> {
            binding.etDni.setEnabled(estaEditando);
            binding.etNombre.setEnabled(estaEditando);
            binding.etApellido.setEnabled(estaEditando);
            binding.etEmail.setEnabled(estaEditando);
            binding.etTelefono.setEnabled(estaEditando);
            binding.btnEditarGuardar.setText(estaEditando ? "Guardar" : "Editar");
        });

        // 2. Observar datos del perfil
        viewModel.getPropietario().observe(getViewLifecycleOwner(), p -> {
            binding.etDni.setText(p.getDni());
            binding.etNombre.setText(p.getNombre());
            binding.etApellido.setText(p.getApellido());
            binding.etEmail.setText(p.getEmail());
            binding.etTelefono.setText(p.getTelefono());
        });

        // 3. Listener del botón
        binding.btnEditarGuardar.setOnClickListener(v -> {
            if (Boolean.TRUE.equals(viewModel.getEditando().getValue())) {
                viewModel.actualizarPerfil(
                        binding.etDni.getText().toString(),
                        binding.etNombre.getText().toString(),
                        binding.etApellido.getText().toString(),
                        binding.etEmail.getText().toString(),
                        binding.etTelefono.getText().toString()
                );
            } else {
                viewModel.alternarModoEdicion();
            }
        });

        binding.btnCambiarPassword.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.nav_cambiar_password));

        viewModel.obtenerPerfil();
        return binding.getRoot();
    }
}