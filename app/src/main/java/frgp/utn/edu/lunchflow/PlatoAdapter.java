package frgp.utn.edu.lunchflow;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlatoAdapter extends RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder> {

    private final List<Plato> listaPlatos;

    public PlatoAdapter(List<Plato> listaPlatos) {
        this.listaPlatos = listaPlatos;
    }

    @NonNull
    @Override
    public PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // IMPORTANTE: Debe ser R.layout.item_plato, NO R.id.item_plato
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plato, parent, false);
        return new PlatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
        Plato plato = listaPlatos.get(position);
        holder.tvTitulo.setText(plato.getTitulo());
        holder.tvDescripcion.setText(plato.getDescripcion());
        holder.ivPlato.setImageResource(plato.getImagenResId());

        // Esto evita que el botón tire error al hacerle clic
        holder.btnSeleccionar.setOnClickListener(v -> {
            // Por ahora solo mostramos un mensaje
            android.widget.Toast.makeText(v.getContext(),
                    "Seleccionaste: " + plato.getTitulo(), android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listaPlatos.size();
    }

    static class PlatoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion;
        ImageView ivPlato;
        Button btnSeleccionar; // 1. Agregamos la declaración del botón

        public PlatoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloPlato);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionPlato);
            ivPlato = itemView.findViewById(R.id.ivPlato);

            // 2. Lo vinculamos con el ID que pusimos en el XML
            btnSeleccionar = itemView.findViewById(R.id.btnSeleccionar);
        }
    }
}