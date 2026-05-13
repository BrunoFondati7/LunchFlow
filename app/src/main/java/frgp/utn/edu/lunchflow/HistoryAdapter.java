package frgp.utn.edu.lunchflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<PedidoDTO> listaPedidos;

    public HistoryAdapter(List<PedidoDTO> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PedidoDTO pedido = listaPedidos.get(position);

        holder.tvFechaHistorial.setText(pedido.getFecha());
        // Usamos getNombreMenu() que es donde viajan los platos
        holder.tvMenuInfoHistorial.setText(pedido.getNombreMenu());
    }

    @Override
    public int getItemCount() {
        return listaPedidos != null ? listaPedidos.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // 1. Declaramos los nombres que usaste en el onBind
        TextView tvFechaHistorial, tvMenuInfoHistorial;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 2. Los vinculamos con el XML
            tvFechaHistorial = itemView.findViewById(R.id.tvFechaHistorial);
            tvMenuInfoHistorial = itemView.findViewById(R.id.tvMenuInfoHistorial);
        }
    }
}