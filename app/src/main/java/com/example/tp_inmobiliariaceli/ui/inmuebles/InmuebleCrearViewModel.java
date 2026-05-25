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
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleCrearViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<String> mMensaje;
    private MutableLiveData<Boolean> mCreado;

    public InmuebleCrearViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.mMensaje = new MutableLiveData<>();
        this.mCreado = new MutableLiveData<>(false);
    }

    public LiveData<String> getMensaje() { return mMensaje; }
    public LiveData<Boolean> getCreado() { return mCreado; }

    public void crearInmueble(Inmueble inmueble) {
        // 1. Convertimos el objeto Inmueble a un texto JSON
        String jsonInmueble = new Gson().toJson(inmueble);

        // 2. Empaquetamos el JSON en un RequestBody
        RequestBody cuerpoInmueble = RequestBody.create(MediaType.parse("application/json"), jsonInmueble);

        // 3. Imagen: Por ahora enviamos 'null' asumiendo que no adjuntas foto obligatoriamente.
        // (Si la API exige la foto sí o sí, aquí habrá que armar el archivo físico después).
        //MultipartBody.Part imagenPart = null;


        // 3. Imagen: Creamos una imagen "falsa" y vacía para cumplir con el requisito del servidor
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), new byte[0]);
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "vacia.jpg", requestFile);



        // 4. Llamamos a la API con el nuevo formato Multipart
        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio(context);
        Call<Inmueble> call = api.crearInmueble(imagenPart, cuerpoInmueble);

        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    mMensaje.setValue("Inmueble creado con éxito");
                    mCreado.setValue(true);
                } else {
                    // Intentamos leer la explicación exacta del servidor para el error 400
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e("API_ERROR", "Error: " + response.code() + " - Mensaje: " + response.message() + " - Detalle: " + errorBody);
                    mMensaje.setValue("Error al crear inmueble: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                mMensaje.setValue("Falla de red: " + t.getMessage());
            }
        });
    }
}