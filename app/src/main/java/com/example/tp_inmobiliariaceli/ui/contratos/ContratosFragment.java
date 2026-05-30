package com.example.tp_inmobiliariaceli.ui.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tp_inmobiliariaceli.R;
import com.example.tp_inmobiliariaceli.databinding.FragmentContratosBinding;
import com.example.tp_inmobiliariaceli.modelo.Inmueble;
import com.example.tp_inmobiliariaceli.ui.inquilinos.InquilinosAdapter;

public class ContratosFragment extends Fragment {
    private ContratosViewModel viewModel;
    private FragmentContratosBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ContratosViewModel.class);
        binding = FragmentContratosBinding.inflate(inflater, container, false);

        binding.rvContratosInmuebles.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getInmueblesAlquilados().observe(getViewLifecycleOwner(), inmuebles -> {
            if (inmuebles != null) {
                // CORREGIDO: Se usa la interfaz OnItemClickListener explícita para evitar errores de compilación
                InquilinosAdapter adapter = new InquilinosAdapter(inmuebles, requireContext(), new InquilinosAdapter.OnItemClickListener() {
                    @Override
                    public void onVerClick(Inmueble inmueble) {
                        viewModel.onInmuebleSelected(inmueble.getIdInmueble());
                    }
                });
                binding.rvContratosInmuebles.setAdapter(adapter);
            }
        });

        viewModel.getNavigateToDetalle().observe(getViewLifecycleOwner(), idInmueble -> {
            if (idInmueble != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("idInmueble", idInmueble);
                Navigation.findNavController(requireView()).navigate(R.id.action_contratos_to_detalle, bundle);
                viewModel.resetNavegacion();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}