package com.example.tp_inmobiliariaceli.ui.pagos;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.tp_inmobiliariaceli.modelo.Pago;
import com.example.tp_inmobiliariaceli.request.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pago>> pagos;
    private MutableLiveData<String> mensaje;

    public PagosViewModel(@NonNull Application application) {
        super(application);
        pagos = new MutableLiveData<>();
        mensaje = new MutableLiveData<>();
    }

    public LiveData<List<Pago>> getPagos() {
        return pagos;
    }

    public LiveData<String> getMensaje() {
        return mensaje;
    }

    public void cargarPagos(int idContrato) {
        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio(getApplication());
        Call<List<Pago>> call = api.obtenerPagosPorContrato(idContrato);

        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pagos.setValue(response.body());
                } else {
                    mensaje.setValue("No se pudieron cargar los pagos");
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                mensaje.setValue("Error de conexión: " + t.getMessage());
                Log.e("API_ERROR", "Error de conexión", t);
            }
        });
    }
}
