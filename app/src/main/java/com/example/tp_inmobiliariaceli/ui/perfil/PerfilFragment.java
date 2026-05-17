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

import com.example.tp_inmobiliariaceli.R;
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
        inicializarListeners();
        
        // Solicitar los datos del perfil al servidor
        viewModel.obtenerPerfil();

        return root;
    }

    private void inicializarListeners() {
        binding.btnEditarGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.btnEditarGuardar.getText().toString();
                if (text.equalsIgnoreCase("Editar")) {
                    // Modo Edición: habilitamos campos
                    binding.etDni.setEnabled(true);
                    binding.etNombre.setEnabled(true);
                    binding.etApellido.setEnabled(true);
                    binding.etEmail.setEnabled(true);
                    binding.etTelefono.setEnabled(true);
                    
                    binding.btnEditarGuardar.setText("Guardar");
                } else {
                    // Modo Guardar: enviamos datos al ViewModel
                    String dni = binding.etDni.getText().toString();
                    String nombre = binding.etNombre.getText().toString();
                    String apellido = binding.etApellido.getText().toString();
                    String email = binding.etEmail.getText().toString();
                    String telefono = binding.etTelefono.getText().toString();
                    
                    // Deshabilitamos el botón temporalmente para evitar doble clic
                    binding.btnEditarGuardar.setEnabled(false);
                    
                    viewModel.actualizarPerfil(dni, nombre, apellido, email, telefono);
                }
            }
        });

        binding.btnCambiarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la pantalla de cambiar contraseña
                androidx.navigation.Navigation.findNavController(v).navigate(R.id.nav_cambiar_password);
            }
        });
    }

    private void inicializarObservadores() {
        viewModel.getPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {
                    // Cargar datos en la vista
                    binding.etDni.setText(propietario.getDni());
                    binding.etNombre.setText(propietario.getNombre());
                    binding.etApellido.setText(propietario.getApellido());
                    binding.etEmail.setText(propietario.getEmail());
                    binding.etTelefono.setText(propietario.getTelefono());

                    // Al cargar o guardar con éxito, volvemos a inhabilitar los campos
                    binding.etDni.setEnabled(false);
                    binding.etNombre.setEnabled(false);
                    binding.etApellido.setEnabled(false);
                    binding.etEmail.setEnabled(false);
                    binding.etTelefono.setEnabled(false);

                    // Restaurar botón a estado Editar y habilitarlo
                    binding.btnEditarGuardar.setText("Editar");
                    binding.btnEditarGuardar.setEnabled(true);
                }
            }
        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                // Si la validación local falló o la API falló, re-habilitamos el botón
                // para que el usuario pueda corregir el formulario y reintentar.
                binding.btnEditarGuardar.setEnabled(true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
