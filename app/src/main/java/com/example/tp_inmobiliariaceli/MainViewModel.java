package com.example.tp_inmobiliariaceli;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp_inmobiliariaceli.modelo.Propietario;
import com.example.tp_inmobiliariaceli.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<Propietario> propietario;
    private Context context;

    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        propietario = new MutableLiveData<>();
    }

    public LiveData<Propietario> getPropietario() {
        return propietario;
    }

    public void cargarPerfil() {
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio(context);
        Call<Propietario> call = servicio.obtenerPerfil();
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if(response.isSuccessful() && response.body() != null) {
                    propietario.setValue(response.body());
                } else {
                    Log.d("MainViewModel", "Error cargando perfil: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.d("MainViewModel", "Falla de red: " + t.getMessage());
            }
        });
    }
}
