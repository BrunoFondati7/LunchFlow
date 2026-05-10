package frgp.utn.edu.lunchflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // No olvides el import de Toast

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlatoAdapter extends RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder> {

    private final List<Plato> listaPlatos;
    private final OnPlatoSeleccionadoListener listener; // El puente

    // 1. Definimos la interfaz (el contrato)
    public interface OnPlatoSeleccionadoListener {
        void onPlatoSeleccionado(Plato plato);
    }

    // 2. Actualizamos el constructor para recibir el listener
    public PlatoAdapter(List<Plato> listaPlatos, OnPlatoSeleccionadoListener listener) {
        this.listaPlatos = listaPlatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plato, parent, false);
        return new PlatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
        Plato plato = listaPlatos.get(position);
        holder.tvTitulo.setText(plato.getTitulo());
        holder.tvDescripcion.setText(plato.getDescripcion());
        holder.ivPlato.setImageResource(plato.getImagenResId());

        // 3. Cuando se hace clic, le avisamos a la MainActivity

        holder.btnSeleccionar.setOnClickListener(v -> {
            if (listener != null) {
                // Solo le avisamos al listener (MainActivity) que se tocó el botón
                listener.onPlatoSeleccionado(plato);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPlatos.size();
    }

    static class PlatoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion;
        ImageView ivPlato;
        Button btnSeleccionar;

        public PlatoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloPlato);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionPlato);
            ivPlato = itemView.findViewById(R.id.ivPlato);
            btnSeleccionar = itemView.findViewById(R.id.btnSeleccionar);
        }
    }
}