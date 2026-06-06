package com.example.tp_inmobiliariaceli.ui.pagos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tp_inmobiliariaceli.R;
import com.example.tp_inmobiliariaceli.modelo.Pago;
import java.util.ArrayList;
import java.util.List;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.PagoViewHolder> {

    private List<Pago> pagos = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pago pago);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pago, parent, false);
        return new PagoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoViewHolder holder, int position) {
        Pago pago = pagos.get(position);
        holder.tvItemPagoCodigo.setText(String.valueOf(pago.getIdPago()));
        holder.tvItemPagoFecha.setText(pago.getFechaPago());
        holder.tvItemPagoMonto.setText("$ " + pago.getMonto());

        holder.btnVerDetallePago.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(pago);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pagos.size();
    }

    static class PagoViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemPagoCodigo;
        TextView tvItemPagoFecha;
        TextView tvItemPagoMonto;
        Button btnVerDetallePago;

        public PagoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemPagoCodigo = itemView.findViewById(R.id.tvItemPagoCodigo);
            tvItemPagoFecha = itemView.findViewById(R.id.tvItemPagoFecha);
            tvItemPagoMonto = itemView.findViewById(R.id.tvItemPagoMonto);
            btnVerDetallePago = itemView.findViewById(R.id.btnVerDetallePago);
        }
    }
}
