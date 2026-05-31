package com.example.tp_inmobiliariaceli.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tp_inmobiliariaceli.R;
import com.example.tp_inmobiliariaceli.databinding.FragmentInquilinosBinding;
import com.example.tp_inmobiliariaceli.modelo.Inmueble;

import java.util.List;

public class InquilinosFragment extends Fragment {
    private InquilinosViewModel viewModel;
    private FragmentInquilinosBinding binding;
    private InquilinosAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(InquilinosViewModel.class);
        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        configurarRecyclerView();
        configurarObservadores();

        return root;
    }

    private void configurarRecyclerView() {
        binding.recyclerViewInmueblesAlquilados.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void configurarObservadores() {
        viewModel.getInmueblesAlquilados().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                if (inmuebles != null) {
                    adapter = new InquilinosAdapter(inmuebles, requireContext(), new InquilinosAdapter.OnItemClickListener() {
                        @Override
                        public void onVerClick(Inmueble inmueble) {
                            viewModel.onInmuebleClicked(inmueble.getIdInmueble());
                        }
                    });
                    binding.recyclerViewInmueblesAlquilados.setAdapter(adapter);
                }
            }
        });

        viewModel.getNavigateToDetalle().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer idInmueble) {
                if (idInmueble != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("idInmueble", idInmueble);
                    Navigation.findNavController(requireView()).navigate(R.id.action_inquilinos_to_detalle, bundle);
                    viewModel.resetNavegacion();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
