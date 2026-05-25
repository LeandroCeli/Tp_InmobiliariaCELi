package com.example.tp_inmobiliariaceli.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.tp_inmobiliariaceli.databinding.FragmentInmuebleDetalleBinding;
import com.example.tp_inmobiliariaceli.modelo.Inmueble;
import com.example.tp_inmobiliariaceli.request.ApiClient;

public class InmuebleDetalleFragment extends Fragment {
    private FragmentInmuebleDetalleBinding binding;
    private InmuebleDetalleViewModel viewModel;
    private boolean switchCambioByUser = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInmuebleDetalleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(InmuebleDetalleViewModel.class);

        // Recuperar el inmueble pasado por Bundle (serializable)
        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                viewModel.setInmueble(inmueble);
            }
        }

        // Observar el inmueble y poblar la vista
        viewModel.getInmueble().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                if (inmueble == null) return;
                poblarVista(inmueble);
            }
        });

        // Observar mensajes de la API
        viewModel.getMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                if (mensaje != null && !mensaje.isEmpty()) {
                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener del Switch: solo dispara el update si el cambio lo hizo el usuario
        binding.swDetalleDisponible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchCambioByUser) {
                    Inmueble actual = viewModel.getInmueble().getValue();
                    viewModel.actualizarDisponibilidad(actual, isChecked);
                }
            }
        });
    }

    private void poblarVista(Inmueble inmueble) {
        binding.tvDetalleCodigo.setText(String.valueOf(inmueble.getIdInmueble()));
        binding.tvDetalleDireccion.setText(inmueble.getDireccion());
        binding.tvDetalleAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
        binding.tvDetallePrecio.setText("$ " + inmueble.getPrecio());
        binding.tvDetalleUso.setText(inmueble.getUso());
        binding.tvDetalleTipo.setText(inmueble.getTipo());

        // Actualizar el Switch sin disparar el listener (es un cambio programático)
        switchCambioByUser = false;
        binding.swDetalleDisponible.setChecked(inmueble.isDisponible());
        switchCambioByUser = true;

        // Cargar imagen con Glide
        String imageUrl = ApiClient.BASE_URL + inmueble.getImagen();
        Glide.with(requireContext())
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.ivDetalleFoto);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
