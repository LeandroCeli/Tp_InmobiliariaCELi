package com.example.tp_inmobiliariaceli.ui.perfil;

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

public class PerfilViewModel extends AndroidViewModel {
    private MutableLiveData<Propietario> mPropietario;
    private MutableLiveData<String> mMensaje;
    private Context context;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mPropietario = new MutableLiveData<>();
        mMensaje = new MutableLiveData<>();
    }

    public LiveData<Propietario> getPropietario() {
        return mPropietario;
    }

    public LiveData<String> getMensaje() {
        return mMensaje;
    }

    public void obtenerPerfil() {
        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio(context);
        Call<Propietario> call = api.obtenerPerfil();
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mPropietario.postValue(response.body());
                } else {
                    Log.d("API_ERROR", "Código de error: " + response.code());
                    mMensaje.postValue("Error al obtener el perfil. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.d("API_ERROR", "Error de conexión: " + t.getMessage());
                mMensaje.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void actualizarPerfil(String dni, String nombre, String apellido, String email, String telefono) {
        if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
            mMensaje.postValue("Ningún campo puede estar vacío.");
            return;
        }

        Propietario current = mPropietario.getValue();
        if (current == null) {
            mMensaje.postValue("Error al actualizar: Perfil no cargado.");
            return;
        }

        Propietario p = new Propietario();
        p.setIdPropietario(current.getIdPropietario());
        p.setDni(dni);
        p.setNombre(nombre);
        p.setApellido(apellido);
        p.setEmail(email);
        p.setTelefono(telefono);
        p.setClave(null); // Previene errores de hash/clave en el backend

        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio(context);
        Call<Propietario> call = api.actualizarPerfil(p);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mPropietario.postValue(response.body());
                    mMensaje.postValue("Perfil actualizado con éxito.");
                } else {
                    Log.d("API_ERROR", "Error al actualizar perfil: " + response.code());
                    mMensaje.postValue("Error al actualizar el perfil.");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.d("API_ERROR", "Falla de red al actualizar: " + t.getMessage());
                mMensaje.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}
