package com.example.tp_inmobiliariaceli.ui.contratos;

import android.app.Application;
import android.widget.Toast;
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

    public ContratoDetalleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Contrato> getContrato() {
        return mContrato;
    }

    public void cargarContrato(int idInmueble) {
        // CORREGIDO: Se removió la línea de leerToken ya que tu interfaz de Retrofit no lo requiere como argumento
        ApiClient.getServicio(getApplication())
                .obtenerContratoPorInmueble(idInmueble) // Llamada corregida con un solo parámetro
                .enqueue(new Callback<Contrato>() {
                    @Override
                    public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Seteamos el contrato en el LiveData para que el Fragment lo dibuje
                            mContrato.setValue(response.body());
                        } else {
                            Toast.makeText(getApplication(), "No se encontró un contrato activo", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Contrato> call, Throwable t) {
                        Toast.makeText(getApplication(), "Error de conexión al obtener contrato", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}