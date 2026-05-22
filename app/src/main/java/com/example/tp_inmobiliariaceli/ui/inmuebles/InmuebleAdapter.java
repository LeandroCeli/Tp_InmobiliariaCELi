package com.example.tp_inmobiliariaceli.ui.inmuebles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tp_inmobiliariaceli.R;
import com.example.tp_inmobiliariaceli.modelo.Inmueble;
import com.example.tp_inmobiliariaceli.request.ApiClient;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.ViewHolder> {
    private List<Inmueble> listaInmuebles;
    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Inmueble inmueble);
    }

    public InmuebleAdapter(Context context, List<Inmueble> listaInmuebles, OnItemClickListener listener) {
        this.context = context;
        this.listaInmuebles = listaInmuebles;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_inmueble, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inmueble inmueble = listaInmuebles.get(position);
        holder.tvDireccion.setText(inmueble.getDireccion());
        holder.tvPrecio.setText("$ " + inmueble.getPrecio());

        // Concatenación de base + ruta relativa para Glide
        String imageUrl = ApiClient.BASE_URL + inmueble.getImagen();
        
        Glide.with(context)
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.ivFoto);

        // Disparar callback de click para manejo en Fragment/ViewModel
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(inmueble);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaInmuebles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvDireccion;
        TextView tvPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.ivInmuebleFoto);
            tvDireccion = itemView.findViewById(R.id.tvInmuebleDireccion);
            tvPrecio = itemView.findViewById(R.id.tvInmueblePrecio);
        }
    }
}
