package com.example.tp_inmobiliariaceli.ui.contratos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp_inmobiliariaceli.modelo.Contrato;
import com.example.tp_inmobiliariaceli.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratoDetalleViewModel extends AndroidViewModel {
    private final MutableLiveData<Contrato> mContrato = new MutableLiveData<>();
    private final MutableLiveData<String> mMensaje = new MutableLiveData<>();

    public ContratoDetalleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getContrato() {
        return mContrato;
    }

    public LiveData<String> getMensaje() {
        return mMensaje;
    }

    public void cargarContrato(int idInmueble) {

        ApiClient.getServicio(getApplication())
                .obtenerContratoPorInmueble(idInmueble)
                .enqueue(new Callback<Contrato>() {
                    @Override
                    public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            mContrato.setValue(response.body());
                        } else {
                            mMensaje.setValue("No se encontró un contrato activo");
                        }
                    }

                    @Override
                    public void onFailure(Call<Contrato> call, Throwable t) {
                        mMensaje.setValue("Error de conexión al obtener contrato");
                    }
                });
    }
}