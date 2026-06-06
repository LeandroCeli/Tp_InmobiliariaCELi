package com.example.tp_inmobiliariaceli.ui.pagos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tp_inmobiliariaceli.R;
import com.example.tp_inmobiliariaceli.modelo.Pago;

public class PagoDetalleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pago_detalle, container, false);

        TextView tvPagoCodigo = root.findViewById(R.id.tvPagoCodigo);
        TextView tvPagoNumero = root.findViewById(R.id.tvPagoNumero);
        TextView tvPagoFecha = root.findViewById(R.id.tvPagoFecha);
        TextView tvPagoImporte = root.findViewById(R.id.tvPagoImporte);
        TextView tvPagoContratoCodigo = root.findViewById(R.id.tvPagoContratoCodigo);

        if (getArguments() != null) {
            Pago pago = (Pago) getArguments().getSerializable("pago");
            if (pago != null) {
                tvPagoCodigo.setText(String.valueOf(pago.getIdPago()));
                tvPagoNumero.setText(pago.getDetalle());
                tvPagoFecha.setText(pago.getFechaPago());
                tvPagoImporte.setText("$ " + pago.getMonto());
                tvPagoContratoCodigo.setText("Contrato Nro " + pago.getIdContrato());
            }
        }

        return root;
    }
}
