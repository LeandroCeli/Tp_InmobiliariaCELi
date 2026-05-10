package com.example.tp_inmobiliariaceli.ui.login;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp_inmobiliariaceli.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    
    private MutableLiveData<String> mensaje;
    private MutableLiveData<Boolean> loginSuccess;
    private MutableLiveData<Boolean> isLoading;
    private Context context;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mensaje = new MutableLiveData<>();
        loginSuccess = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        isLoading.setValue(false);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public void recuperarDatos(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            mensaje.setValue("Por favor, complete todos los campos");
        } else {
            isLoading.setValue(true);
            ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio(context);
            Call<String> call = servicio.login(email, password);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    isLoading.setValue(false);
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body();
                        ApiClient.recuperarToken(context, token);
                        // Log.d("token_test", token); // ya funciona, lo comento
                        
                        // Notificamos a la vista que el login fue exitoso
                        loginSuccess.setValue(true);
                    } else {
                        Log.d("Error_Login", response.message());
                        //System.out.println("codigo de error: " + response.code());
                        mensaje.setValue("Usuario o contraseña incorrectos");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    isLoading.setValue(false);
                    Log.d("mensaje", t.getMessage());
                    mensaje.setValue("Error de conexión: " + t.getMessage());
                }
            });
        }
    }
}
