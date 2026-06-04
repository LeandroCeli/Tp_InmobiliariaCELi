package com.example.tp_inmobiliariaceli.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.tp_inmobiliariaceli.R;
import com.example.tp_inmobiliariaceli.databinding.FragmentInmueblesBinding;
import com.example.tp_inmobiliariaceli.modelo.Inmueble;

import java.util.List;

public class InmueblesFragment extends Fragment {
    private InmueblesViewModel viewModel;
    private FragmentInmueblesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar RecyclerView en cuadrícula (2 columnas) para un look premium
        binding.rvInmuebles.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Observar listado de inmuebles
        viewModel.getListaInmuebles().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                InmuebleAdapter adapter = new InmuebleAdapter(getContext(), inmuebles, new InmuebleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Inmueble inmueble) {
                        viewModel.seleccionarInmueble(inmueble);
                    }
                });
                binding.rvInmuebles.setAdapter(adapter);
            }
        });

        // Observar trigger de navegación al Detalle
        viewModel.getInmuebleParaDetalle().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                if (inmueble != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("inmueble", inmueble);
                    Navigation.findNavController(requireView()).navigate(R.id.action_inmuebles_to_detalle, bundle);
                    
                    // Resetear el observador inmediatamente para evitar bucles infinitos al regresar
                    viewModel.resetNavegacionDetalle();
                }
            }
        });

        // Observar mensajes de error/API
        viewModel.getMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                if (mensaje != null && !mensaje.isEmpty()) {
                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.fabAgregarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_inmuebles_to_agregar);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar al regresar para tener los datos actualizados
        viewModel.cargarInmuebles();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
