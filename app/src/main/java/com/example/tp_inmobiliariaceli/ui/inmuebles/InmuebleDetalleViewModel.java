package com.example.tp_inmobiliariaceli.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp_inmobiliariaceli.modelo.Inmueble;
import com.example.tp_inmobiliariaceli.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleDetalleViewModel extends AndroidViewModel {
    private MutableLiveData<Inmueble> mInmueble;
    private MutableLiveData<String> mMensaje;
    private Context context;

    public InmuebleDetalleViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mInmueble = new MutableLiveData<>();
        mMensaje = new MutableLiveData<>();
    }

    public LiveData<Inmueble> getInmueble() {
        return mInmueble;
    }

    public LiveData<String> getMensaje() {
        return mMensaje;
    }

    public void setInmueble(Inmueble inmueble) {
        mInmueble.setValue(inmueble);
    }

    public void actualizarDisponibilidad(Inmueble inmueble, boolean disponible) {
        if (inmueble == null) return;

        // Modificamos solo el campo de disponibilidad
        inmueble.setDisponible(disponible);

        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio(context);
        Call<Inmueble> call = api.actualizarInmueble(inmueble);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mInmueble.postValue(response.body());
                    mMensaje.postValue("Disponibilidad actualizada con éxito.");
                } else {
                    Log.d("API_ERROR", "Error al actualizar disponibilidad: " + response.code());
                    mMensaje.postValue("Error al actualizar estado en el servidor.");
                    
                    // En caso de fallo, volvemos a notificar el objeto original para revertir el Switch en la vista
                    mInmueble.postValue(inmueble);
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.d("API_ERROR", "Falla de red en actualización: " + t.getMessage());
                mMensaje.postValue("Error de conexión: " + t.getMessage());
                
                // Revertimos en la vista
                mInmueble.postValue(inmueble);
            }
        });
    }
}
