package com.example.tp_inmobiliariaceli.ui.pagos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tp_inmobiliariaceli.R;
import android.widget.TextView;

public class PagosFragment extends Fragment {

    private PagosViewModel viewModel;
    private PagoAdapter adapter;
    private RecyclerView rvPagos;
    private TextView tvNoPagos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pagos, container, false);

        viewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        rvPagos = root.findViewById(R.id.rvPagos);
        tvNoPagos = root.findViewById(R.id.tvNoPagos);

        rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PagoAdapter();
        rvPagos.setAdapter(adapter);

        adapter.setOnItemClickListener(pago -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("pago", pago);
            Navigation.findNavController(root).navigate(R.id.action_pagos_to_pago_detalle, bundle);
        });

        viewModel.getPagos().observe(getViewLifecycleOwner(), pagos -> {
            if (pagos != null && !pagos.isEmpty()) {
                adapter.setPagos(pagos);
                tvNoPagos.setVisibility(View.GONE);
                rvPagos.setVisibility(View.VISIBLE);
            } else {
                tvNoPagos.setVisibility(View.VISIBLE);
                rvPagos.setVisibility(View.GONE);
            }
        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        if (getArguments() != null) {
            int idContrato = getArguments().getInt("idContrato", -1);
            if (idContrato != -1) {
                viewModel.cargarPagos(idContrato);
            }
        }

        return root;
    }
}
