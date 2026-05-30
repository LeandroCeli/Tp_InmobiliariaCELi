package com.example.tp_inmobiliariaceli.ui.contratos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp_inmobiliariaceli.modelo.Inmueble;
import com.example.tp_inmobiliariaceli.request.ApiClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Inmueble>> inmueblesAlquilados = new MutableLiveData<>();
    private final MutableLiveData<Integer> navigateToDetalle = new MutableLiveData<>();

    public ContratosViewModel(@NonNull Application application) {
        super(application);
        cargarInmueblesConContrato();
    }

    public LiveData<List<Inmueble>> getInmueblesAlquilados() {
        return inmueblesAlquilados;
    }

    public LiveData<Integer> getNavigateToDetalle() {
        return navigateToDetalle;
    }

    public void cargarInmueblesConContrato() {
        ApiClient.getServicio(getApplication())
                .obtenerInmueblesAlquilados()
                .enqueue(new Callback<List<Inmueble>>() {
                    @Override
                    public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                        if (response.isSuccessful()) {
                            inmueblesAlquilados.setValue(response.body());
                        } else {
                            inmueblesAlquilados.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                        inmueblesAlquilados.setValue(null);
                    }
                });
    }

    public void onInmuebleSelected(int idInmueble) {
        navigateToDetalle.setValue(idInmueble);
    }

    public void resetNavegacion() {
        navigateToDetalle.setValue(null);
    }
}