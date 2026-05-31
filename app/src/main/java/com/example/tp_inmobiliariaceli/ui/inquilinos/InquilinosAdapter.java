package com.example.tp_inmobiliariaceli.ui.inquilinos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tp_inmobiliariaceli.R;
import com.example.tp_inmobiliariaceli.modelo.Inmueble;
import com.example.tp_inmobiliariaceli.request.ApiClient;

import java.util.List;

public class InquilinosAdapter extends RecyclerView.Adapter<InquilinosAdapter.ViewHolder> {
    private List<Inmueble> inmuebles;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onVerClick(Inmueble inmueble);
    }

    public InquilinosAdapter(List<Inmueble> inmuebles, Context context, OnItemClickListener listener) {
        this.inmuebles = inmuebles;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inmueble_alquilado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (inmuebles == null || inmuebles.get(position) == null) return;

        Inmueble inmueble = inmuebles.get(position);
        holder.tvDireccion.setText(inmueble.getDireccion() != null ? inmueble.getDireccion() : "Sin dirección");

        String urlImagen = ApiClient.BASE_URL + inmueble.getImagen();
        Glide.with(holder.itemView.getContext())
                .load(urlImagen)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivFoto);

        holder.btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onVerClick(inmueble);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return inmuebles != null ? inmuebles.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivFoto;
        public TextView tvDireccion;
        public Button btnVer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Sincronizados de forma idéntica con el XML del Paso 1
            ivFoto = itemView.findViewById(R.id.ivFotoInmueble);
            tvDireccion = itemView.findViewById(R.id.tvDireccionInmueble);
            btnVer = itemView.findViewById(R.id.btnVer);
        }
    }
}