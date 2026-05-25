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
    private MutableLiveData<Boolean> mEditando; // Estado de edición
    private Context context;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mPropietario = new MutableLiveData<>();
        mMensaje = new MutableLiveData<>();
        mEditando = new MutableLiveData<>(false); // Comienza bloqueado (modo lectura)
    }

    public LiveData<Propietario> getPropietario() { return mPropietario; }
    public LiveData<String> getMensaje() { return mMensaje; }
    public LiveData<Boolean> getEditando() { return mEditando; }

    // Cambia el modo entre lectura y edición
    public void alternarModoEdicion() {
        mEditando.setValue(!Boolean.TRUE.equals(mEditando.getValue()));
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
                    mMensaje.postValue("Error al obtener perfil: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mMensaje.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void actualizarPerfil(String dni, String nombre, String apellido, String email, String telefono) {
        Propietario current = mPropietario.getValue();
        if (current == null) return;

        Propietario p = new Propietario();
        p.setIdPropietario(current.getIdPropietario());
        p.setDni(dni);
        p.setNombre(nombre);
        p.setApellido(apellido);
        p.setEmail(email);
        p.setTelefono(telefono);

        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio(context);
        Call<Propietario> call = api.actualizarPerfil(p);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    mPropietario.postValue(p);
                    mEditando.postValue(false); // Vuelve a modo lectura al guardar
                    mMensaje.postValue("Perfil actualizado con éxito.");
                } else {
                    mMensaje.postValue("Error al actualizar.");
                }
            }
            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                mMensaje.postValue("Falla de red.");
            }
        });
    }
}