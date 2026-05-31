package com.example.tp_inmobiliariaceli.ui.login;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tp_inmobiliariaceli.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    // LiveData originales de tu entrega funcional
    private MutableLiveData<String> mensaje;
    private MutableLiveData<Boolean> loginSuccess;
    private MutableLiveData<Boolean> isLoading;
    private Context context;

    // ESPECÍFICO CLASE 29 DE ABRIL: LiveData para comunicar el evento del sensor a la vista
    private MutableLiveData<Boolean> mAgitadoExitoso;

    // Componentes del Sensor centralizados en el ViewModel
    private SensorManager mSensorManager;
    private Sensor mAcelerometro;
    private AgitarSensorListener mSensorListener;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        // Uso obligatorio de getApplication() según las buenas prácticas de arquitectura de la cátedra
        this.context = application.getApplicationContext();

        this.mensaje = new MutableLiveData<>();
        this.loginSuccess = new MutableLiveData<>();
        this.isLoading = new MutableLiveData<>();
        this.isLoading.setValue(false);

        this.mAgitadoExitoso = new MutableLiveData<>();

        // Obtención de la instancia del servicio mediante getApplication()
        this.mSensorManager = (SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            this.mAcelerometro = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // Opción preferida por el profesor: Instanciación de la clase interna dedicada
            this.mSensorListener = new AgitarSensorListener();
        }
    }

    // Getters públicos para que la Activity observe de forma pasiva
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getMensaje() { return mensaje; }
    public LiveData<Boolean> getLoginSuccess() { return loginSuccess; }
    public LiveData<Boolean> getMAgitadoExitoso() { return mAgitadoExitoso; }

    // Tu lógica original de negocio intacta conectada a tu backend
    public void recuperarDatos(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            mensaje.setValue("Por favor, complete todos los campos");
        } else {
            isLoading.setValue(true);
            ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio(context);
            Call<String> call = servicio.login(email, password);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    isLoading.setValue(false);
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body();
                        ApiClient.guardarToken(context, token);
                        loginSuccess.setValue(true);
                    } else {
                        Log.d("Error_Login", response.message());
                        mensaje.setValue("Usuario o contraseña incorrectos");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    isLoading.setValue(false);
                    Log.d("mensaje", t.getMessage());
                    mensaje.setValue("Error de conexión: " + t.getMessage());
                }
            });
        }
    }

    // Métodos expuestos para sincronizar el registro del sensor con el ciclo de vida de la vista
    public void registrarSensor() {
        if (mSensorManager != null && mAcelerometro != null) {
            mSensorManager.registerListener(mSensorListener, mAcelerometro, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void removerSensor() {
        if (mSensorManager != null && mSensorListener != null) {
            mSensorManager.unregisterListener(mSensorListener);
        }
    }

    // --- OPCIÓN RECOMENDADA POR EL DOCENTE: CLASE INTERNA PARA MANEJAR onSensorChanged ---
    private class AgitarSensorListener implements SensorEventListener {
        private static final float LIMITE_FUERZA_GRAVEDAD = 2.7F; // Sensibilidad del movimiento
        private static final int TIEMPO_ENTRE_SACUDIDAS_MS = 500;
        private static final int TIEMPO_RESETEO_CONTADOR_MS = 3000;

        private long mMarcaTiempoUltimoMovimiento;
        private int mContadorSacudidas;

        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float fuerzaGravedadX = x / SensorManager.GRAVITY_EARTH;
            float fuerzaGravedadY = y / SensorManager.GRAVITY_EARTH;
            float fuerzaGravedadZ = z / SensorManager.GRAVITY_EARTH;

            // Raíz cuadrada vectorial para evaluar la aceleración total del dispositivo
            float fuerzaGTotal = (float) Math.sqrt(fuerzaGravedadX * fuerzaGravedadX + fuerzaGravedadY * fuerzaGravedadY + fuerzaGravedadZ * fuerzaGravedadZ);

            if (fuerzaGTotal > LIMITE_FUERZA_GRAVEDAD) {
                final long tiempoActual = System.currentTimeMillis();

                if (mMarcaTiempoUltimoMovimiento + TIEMPO_ENTRE_SACUDIDAS_MS > tiempoActual) {
                    return;
                }

                if (mMarcaTiempoUltimoMovimiento + TIEMPO_RESETEO_CONTADOR_MS < tiempoActual) {
                    mContadorSacudidas = 0;
                }

                mMarcaTiempoUltimoMovimiento = tiempoActual;
                mContadorSacudidas++;

                // Patrón detectado con éxito tras un movimiento de vaivén coordinado (2 sacudidas bruscas)
                if (mContadorSacudidas >= 2) {
                    mContadorSacudidas = 0;
                    // Comunicación asíncrona con la vista mediante el canal MutableLiveData
                    mAgitadoExitoso.setValue(true);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Requerido obligatoriamente por el contrato de la interfaz de Android
        }
    }
}