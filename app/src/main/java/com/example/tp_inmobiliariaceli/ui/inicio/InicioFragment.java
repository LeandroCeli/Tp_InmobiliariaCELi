package com.example.tp_inmobiliariaceli.ui.inicio;

import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class InicioFragment extends Fragment implements OnMapReadyCallback {
    private InicioViewModel viewModel;
    private FragmentInicioBinding binding;
    private GoogleMap mMap;

    private final LatLng INMOBILIARIA_COORD = new LatLng(-33.184323, -66.311749);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(InicioViewModel.class);
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel.getLocation().observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if (location != null) {
                    // Actualizaciones de ubicación recibidas desde el ViewModel
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        mMap.addMarker(new MarkerOptions()
                .position(INMOBILIARIA_COORD)
                .title("Inmobiliaria La Punta")
                .snippet("Tu hogar, nuestro compromiso"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(INMOBILIARIA_COORD)
                .zoom(16f)
                .bearing(0)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        configurarUbicacionUsuario();
    }

    private void configurarUbicacionUsuario() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            
            try {
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                }
                viewModel.obtenerUltimaUbicacion();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.detenerLecturaUbicacion();
        binding = null;
    }
}
