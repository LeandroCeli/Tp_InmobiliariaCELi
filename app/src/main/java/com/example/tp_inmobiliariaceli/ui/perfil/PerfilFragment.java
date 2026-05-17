package com.example.tp_inmobiliariaceli.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp_inmobiliariaceli.databinding.FragmentPerfilBinding;
import com.example.tp_inmobiliariaceli.modelo.Propietario;

public class PerfilFragment extends Fragment {
    private PerfilViewModel viewModel;
    private FragmentPerfilBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarObservadores();
        
        // Solicitar los datos del perfil al servidor
        viewModel.obtenerPerfil();

        return root;
    }

    private void inicializarObservadores() {
        viewModel.getPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {
                    // Cargar datos en la vista
                    binding.etCodigo.setText(String.valueOf(propietario.getIdPropietario()));
                    binding.etDni.setText(propietario.getDni());
                    binding.etNombre.setText(propietario.getNombre());
                    binding.etApellido.setText(propietario.getApellido());
                    binding.etEmail.setText(propietario.getEmail());
                    binding.etTelefono.setText(propietario.getTelefono());

                    // Habilitar el botón Editar ahora que los datos están cargados
                    binding.btnEditarGuardar.setEnabled(true);
                }
            }
        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
