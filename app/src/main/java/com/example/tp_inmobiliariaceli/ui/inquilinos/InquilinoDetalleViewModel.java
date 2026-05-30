package com.example.tp_inmobiliariaceli.ui.inquilinos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp_inmobiliariaceli.modelo.Contrato;
import com.example.tp_inmobiliariaceli.modelo.Inquilino;
import com.example.tp_inmobiliariaceli.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinoDetalleViewModel extends AndroidViewModel {
    private final MutableLiveData<Inquilino> inquilino = new MutableLiveData<>();

    public InquilinoDetalleViewModel(@NonNull Application application) {
        super(application);
    }

    public void cargarInquilino(int idInmueble) {
        // En lugar de api/Inquilinos/inmueble/{id}, usamos api/Contratos/inmueble/{id}
        // Porque el Postman indica que el contrato devuelve toda la info (y dentro trae al inquilino).
        ApiClient.getServicio(getApplication())
                .obtenerContratoPorInmueble(idInmueble)
                .enqueue(new Callback<Contrato>() {
                    @Override
                    public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            inquilino.setValue(response.body().getInquilino());
                        } else {
                            inquilino.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Contrato> call, Throwable t) {
                        inquilino.setValue(null);
                    }
                });
    }

    public LiveData<Inquilino> getInquilino() {
        return inquilino;
    }
}
