package frgp.utn.edu.lunchflow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageButton btnVolver = findViewById(R.id.btnVolverHistory);
        btnVolver.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.rvHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- RECUPERACIÓN DINÁMICA DEL LEGAJO ---
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        // Usamos "legajoReal" para coincidir con tu Login.
        // Si no existe, usamos el tuyo por defecto para que no falle la prueba.
        String legajo = prefs.getString("legajoReal", "44173050");

        Log.d("LUNCHFLOW_DEBUG", "Consultando historial para el legajo: " + legajo);

        // Llamada a la API
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getHistorial(legajo).enqueue(new Callback<List<PedidoDTO>>() {
            @Override
            public void onResponse(Call<List<PedidoDTO>> call, Response<List<PedidoDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PedidoDTO> lista = response.body();

                    if (lista.isEmpty()) {
                        Toast.makeText(HistoryActivity.this, "No hay pedidos para este legajo", Toast.LENGTH_SHORT).show();
                    } else {
                        // IMPORTANTE: Tu HistoryAdapter ahora debe recibir List<PedidoDTO>
                        adapter = new HistoryAdapter(lista);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(HistoryActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PedidoDTO>> call, Throwable t) {
                Log.e("LUNCHFLOW_ERR", "Fallo de red: ", t);
                Toast.makeText(HistoryActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}