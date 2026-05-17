package com.example.tp_inmobiliariaceli.ui.perfil;

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

public class CambiarPasswordViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<Boolean> mSuccess;

    public CambiarPasswordViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.mMensaje = new MutableLiveData<>();
        this.mSuccess = new MutableLiveData<>();
    }

    public LiveData<String> getMensaje() {
        return mMensaje;
    }

    public LiveData<Boolean> getSuccess() {
        return mSuccess;
    }

    public void cambiarPassword(String currentPassword, String newPassword, String repeatPassword) {
        if (currentPassword.isEmpty() || newPassword.isEmpty() || repeatPassword.isEmpty()) {
            mMensaje.setValue("Por favor, complete todos los campos.");
            return;
        }

        if (!newPassword.equals(repeatPassword)) {
            mMensaje.setValue("La nueva contraseña y su repetición no coinciden.");
            return;
        }

        if (newPassword.equals(currentPassword)) {
            mMensaje.setValue("La nueva contraseña debe ser diferente a la actual.");
            return;
        }

        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio(context);
        Call<Void> call = api.cambiarPassword(currentPassword, newPassword);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    mMensaje.postValue("Contraseña cambiada con éxito.");
                    mSuccess.postValue(true);
                } else {
                    Log.d("API_ERROR", "Error al cambiar password: " + response.code());
                    mMensaje.postValue("Error al cambiar contraseña. Verifique sus datos.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("API_ERROR", "Falla de red: " + t.getMessage());
                mMensaje.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}
