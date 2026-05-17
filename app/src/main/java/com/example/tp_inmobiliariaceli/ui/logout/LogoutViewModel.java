package com.example.tp_inmobiliariaceli.ui.logout;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp_inmobiliariaceli.request.ApiClient;

public class LogoutViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Boolean> logoutSuccess;

    public LogoutViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.logoutSuccess = new MutableLiveData<>();
    }

    public LiveData<Boolean> getLogoutSuccess() {
        return logoutSuccess;
    }

    public void cerrarSesion() {
        // Limpiar el token de SharedPreferences
        ApiClient.clearToken(context);
        
        // Notificar que se completó el cierre de sesión con éxito
        logoutSuccess.setValue(true);
    }
}
