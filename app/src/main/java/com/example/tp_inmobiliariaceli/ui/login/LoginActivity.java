package com.example.tp_inmobiliariaceli.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp_inmobiliariaceli.MainActivity;
import com.example.tp_inmobiliariaceli.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    // Ajuste del número telefónico destino para la acción directa
    private static final String CADENA_TELEFONO_INMOBILIARIA = "tel:2664000000";
    private static final int CODIGO_PETICION_PERMISO_LLAMADA = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicialización formal del ViewModel asociado a la pantalla
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observador de mensajes informativos o de error
        viewModel.getMensaje().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        // Observador de la transacción de Login exitoso
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

        // Observador del estado de carga visual
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


        viewModel.getMAgitadoExitoso().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean haSidoAgitado) {
                if (haSidoAgitado != null && haSidoAgitado) {
                    ejecutarFlujoLlamadaSaliendo();
                }
            }
        });

        // Evento clásico de disparo del Login
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                viewModel.recuperarDatos(email, password);
            }
        });
    }

    // Manejo exclusivo de flujos de UI y permisos del sistema nativo dentro de la Activity
    private void ejecutarFlujoLlamadaSaliendo() {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CODIGO_PETICION_PERMISO_LLAMADA);
        } else {
            Intent intentLlamarInmobiliaria = new Intent(Intent.ACTION_CALL);
            intentLlamarInmobiliaria.setData(Uri.parse(CADENA_TELEFONO_INMOBILIARIA));
            startActivity(intentLlamarInmobiliaria);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODIGO_PETICION_PERMISO_LLAMADA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido. Agita de nuevo para llamar.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de llamada telefónica denegado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    // ESPECÍFICO CLASE 29 DE ABRIL: La Activity coordina el encendido y apagado vinculando la vista al ViewModel
    @Override
    protected void onResume() {
        super.onResume();
        viewModel.registrarSensor();
    }

    @Override
    protected void onPause() {
        viewModel.removerSensor();
        super.onPause();
    }
}