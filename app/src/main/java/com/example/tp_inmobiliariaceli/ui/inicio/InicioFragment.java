package com.example.tp_inmobiliariaceli.ui.inicio;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tp_inmobiliariaceli.R;
import com.example.tp_inmobiliariaceli.databinding.FragmentInicioBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MAPA_DEBUG";

    private InicioViewModel viewModel;
    private FragmentInicioBinding binding;
    private GoogleMap mMap;

    private final LatLng INMOBILIARIA_COORD =
            new LatLng(-33.15025218, -66.30516644);

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        viewModel = new ViewModelProvider(this)
                .get(InicioViewModel.class);

        binding = FragmentInicioBinding.inflate(
                inflater,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated");

        viewModel.getLocation().observe(
                getViewLifecycleOwner(),
                new Observer<Location>() {
                    @Override
                    public void onChanged(Location location) {

                        if (location != null) {

                            Log.d(
                                    TAG,
                                    "Ubicación recibida: "
                                            + location.getLatitude()
                                            + ", "
                                            + location.getLongitude()
                            );

                        } else {

                            Log.d(
                                    TAG,
                                    "Ubicación NULL"
                            );
                        }
                    }
                });

        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getChildFragmentManager()
                                .findFragmentById(R.id.map);

        if (mapFragment != null) {

            Log.d(TAG, "Fragment encontrado");

            mapFragment.getMapAsync(this);

        } else {

            Log.e(TAG, "Fragment NULL");
        }
    }

    @Override
    public void onMapReady(
            @NonNull GoogleMap googleMap) {

        Log.d(TAG, "onMapReady ejecutado");

        mMap = googleMap;

        mMap.setOnMapLoadedCallback(() ->
                android.util.Log.d(
                        "MAPA_DEBUG",
                        "MAPA COMPLETAMENTE CARGADO"
                )
        );

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings()
                .setZoomControlsEnabled(true);

        mMap.getUiSettings()
                .setCompassEnabled(true);

        mMap.setOnMapLoadedCallback(
                () -> Log.d(
                        TAG,
                        "MAPA COMPLETAMENTE CARGADO"
                )
        );

        mMap.setOnCameraIdleListener(
                () -> Log.d(
                        TAG,
                        "Cámara posicionada"
                )
        );

        try {

            mMap.addMarker(
                    new MarkerOptions()
                            .position(INMOBILIARIA_COORD)
                            .title("Inmobiliaria La Punta")
            );

            Log.d(
                    TAG,
                    "Marcador agregado"
            );

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Error agregando marcador",
                    e
            );
        }

        try {

            mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            INMOBILIARIA_COORD,
                            16f
                    )
            );

            Log.d(
                    TAG,
                    "Camera movida"
            );

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Error moviendo cámara",
                    e
            );
        }

        configurarUbicacionUsuario();
    }

    private void configurarUbicacionUsuario() {

        Log.d(
                TAG,
                "configurarUbicacionUsuario"
        );

        if (androidx.core.content.ContextCompat
                .checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                == android.content.pm.PackageManager.PERMISSION_GRANTED) {

            try {

                Log.d(
                        TAG,
                        "Permiso GPS concedido"
                );

                if (mMap != null) {

                    mMap.setMyLocationEnabled(true);

                    Log.d(
                            TAG,
                            "MyLocation habilitado"
                    );
                }

                viewModel.obtenerUltimaUbicacion();

            } catch (Exception e) {

                Log.e(
                        TAG,
                        "Error GPS",
                        e
                );
            }

        } else {

            Log.e(
                    TAG,
                    "Permiso GPS NO concedido"
            );
        }
    }

    @Override
    public void onDestroyView() {

        Log.d(
                TAG,
                "onDestroyView"
        );

        super.onDestroyView();

        viewModel.detenerLecturaUbicacion();

        binding = null;
    }
}