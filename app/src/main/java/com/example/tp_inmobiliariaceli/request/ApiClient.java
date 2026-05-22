package com.example.tp_inmobiliariaceli.request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.tp_inmobiliariaceli.modelo.Propietario;
import com.example.tp_inmobiliariaceli.modelo.Inmueble;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public class ApiClient {
    public final static String BASE_URL ="https://capacitacion.alwaysdata.net/";
    
    public static MiServicioInmobiliaria getServicio(Context context){
        Gson gson = new GsonBuilder().setLenient().create();
        
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String token = leerToken(context);
                        // el token ya tiene 'Bearer ' concatenado
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", token)
                                .build();
                        Response response = chain.proceed(newRequest);
                        
                        // Centralización de Seguridad (Hito 1): 401 o 403 redirige a Login
                        if (response.code() == 401 || response.code() == 403) {
                            clearToken(context);
                            Intent intent = new Intent(context, com.example.tp_inmobiliariaceli.ui.login.LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        }
                        
                        return response;
                    }
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
                
        return retrofit.create(MiServicioInmobiliaria.class);
    }
    
    public interface MiServicioInmobiliaria{
        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);
        
        @GET("api/Propietarios")
        Call<Propietario> obtenerPerfil();

        @PUT("api/Propietarios/actualizar")
        Call<Propietario> actualizarPerfil(@Body Propietario propietario);

        @FormUrlEncoded
        @PUT("api/Propietarios/changePassword")
        Call<Void> cambiarPassword(@Field("currentPassword") String currentPassword, @Field("newPassword") String newPassword);

        // Hito 3 & 4: Endpoints para Inmuebles
        @GET("api/Inmuebles")
        Call<List<Inmueble>> obtenerInmuebles();

        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> actualizarInmueble(@Body Inmueble inmueble);

        @POST("api/Inmuebles")
        Call<Inmueble> crearInmueble(@Body Inmueble inmueble);
    }
    
    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", "Bearer " + token);
        editor.apply();
    }
    
    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", "");
    }

    public static void clearToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        sp.edit().remove("token").apply();
    }
}
