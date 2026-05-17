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
import androidx.navigation.Navigation;

import com.example.tp_inmobiliariaceli.databinding.FragmentCambiarPasswordBinding;

public class CambiarPasswordFragment extends Fragment {
    private CambiarPasswordViewModel viewModel;
    private FragmentCambiarPasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CambiarPasswordViewModel.class);
        binding = FragmentCambiarPasswordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarListeners(root);
        inicializarObservadores(root);

        return root;
    }

    private void inicializarListeners(View view) {
        binding.btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPass = binding.etCurrentPassword.getText().toString();
                String newPass = binding.etNewPassword.getText().toString();
                String repeatPass = binding.etRepeatPassword.getText().toString();

                viewModel.cambiarPassword(currentPass, newPass, repeatPass);
            }
        });
    }

    private void inicializarObservadores(View view) {
        viewModel.getMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success != null && success) {
                    // Volver a la pantalla anterior (Perfil) tras cambiar con éxito
                    Navigation.findNavController(view).navigateUp();
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
