package com.example.tp_inmobiliariaceli.ui.inmuebles;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp_inmobiliariaceli.modelo.Inmueble;
import com.example.tp_inmobiliariaceli.request.ApiClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    // Actualizado para recibir el Uri
    public void crearInmueble(Inmueble inmueble, Uri imageUri) {
        // 1. Convertimos el objeto Inmueble a un texto JSON
        String jsonInmueble = new Gson().toJson(inmueble);
        RequestBody cuerpoInmueble = RequestBody.create(MediaType.parse("application/json"), jsonInmueble);

        // 2. Convertimos el Uri a bytes reales
        MultipartBody.Part imagenPart;
        try {
            byte[] imagenBytes = uriToBytes(imageUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagenBytes);
            imagenPart = MultipartBody.Part.createFormData("imagen", "foto_inmueble.jpg", requestFile);
        } catch (Exception e) {
            // Si algo falla, enviamos la imagen vacía para que no explote
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), new byte[0]);
            imagenPart = MultipartBody.Part.createFormData("imagen", "vacia.jpg", requestFile);
        }

        // 3. Llamamos a la API con formato Multipart
        ApiClient.MiServicioInmobiliaria api = ApiClient.getServicio(context);
        Call<Inmueble> call = api.crearInmueble(imagenPart, cuerpoInmueble);

        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    mMensaje.setValue("Inmueble creado con éxito");
                    mCreado.setValue(true);
                } else {
                    Log.e("API_ERROR", "Error: " + response.code());
                    mMensaje.setValue("Error al crear inmueble: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                mMensaje.setValue("Falla de red: " + t.getMessage());
            }
        });
    }

    // Método auxiliar para convertir Uri a array de bytes
    private byte[] uriToBytes(Uri uri) throws IOException {
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        iStream.close();
        return byteBuffer.toByteArray();
    }
}