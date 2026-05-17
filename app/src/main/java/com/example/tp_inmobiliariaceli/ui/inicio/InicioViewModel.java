package com.example.tp_inmobiliariaceli.ui.inicio;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

public class InicioViewModel extends AndroidViewModel {
    private Context context;
    private FusedLocationProviderClient fusedLocationClient;
    private MutableLiveData<Location> mLocation;
    private LocationCallback locationCallback;

    public InicioViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
        this.mLocation = new MutableLiveData<>();
    }

    public LiveData<Location> getLocation() {
        return mLocation;
    }

    public void obtenerUltimaUbicacion() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getApplication().getMainExecutor(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLocation.postValue(location);
                            } else {
                                iniciarLecturaUbicacion();
                            }
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void iniciarLecturaUbicacion() {
        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mLocation.postValue(location);
                    }
                }
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
            );
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void detenerLecturaUbicacion() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
