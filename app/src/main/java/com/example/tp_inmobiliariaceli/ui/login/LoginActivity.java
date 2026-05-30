package com.example.tp_inmobiliariaceli.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp_inmobiliariaceli.MainActivity;
import com.example.tp_inmobiliariaceli.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observamos el mensaje de error
        viewModel.getMensaje().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        // Observamos si el login fue exitoso
        viewModel.getLoginSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // Observamos el estado de carga
        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.btnLogin.setEnabled(false);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnLogin.setEnabled(true);
                }
            }
        });

        // Click en el botón de login
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tomo los datos de la vista
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                viewModel.recuperarDatos(email, password);
            }
        });
    }
}