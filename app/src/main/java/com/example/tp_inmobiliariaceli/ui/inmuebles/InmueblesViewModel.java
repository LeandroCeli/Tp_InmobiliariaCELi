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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {
    private MutableLiveData<List<Inmueble>> mListaInmuebles;
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<Inmueble> mInmuebleParaDetalle;
    private Context context;

    public InmueblesViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mListaInmuebles = new MutableLiveData<>();
        mMensaje = new MutableLiveData<>();
        mInmuebleParaDetalle = new MutableLiveData<>();
    }

    public LiveData<List<Inmueble>> getListaInmuebles() {
        return mListaInmuebles;
    }

    public LiveData<String> getMensaje() {
        return mMensaje;
    }

    public LiveData<Inmueble> getInmuebleParaDetalle() {
        return mInmuebleParaDetalle;
    }

    public void seleccionarInmueble(Inmueble inmueble) {
        mInmuebleParaDetalle.setValue(inmueble);
    }

    public void resetNavegacionDetalle() {
        mInmuebleParaDetalle.setValue(null);
    }

    public void cargarInmuebles() {
        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio(context);
        Call<List<Inmueble>> call = api.obtenerInmuebles();
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mListaInmuebles.postValue(response.body());
                } else {
                    Log.d("API_ERROR", "Error al obtener inmuebles: " + response.code());
                    mMensaje.postValue("Error al cargar inmuebles. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Log.d("API_ERROR", "Falla de red en inmuebles: " + t.getMessage());
                mMensaje.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}
