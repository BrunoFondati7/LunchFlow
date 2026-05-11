package frgp.utn.edu.lunchflow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import frgp.utn.edu.lunchflow.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SelectedActivity extends AppCompatActivity {

    private ApiService apiService; // 1. Declaramos el servicio
    private SeleccionRequest requestParaEnviar; // 2. Guardaremos los datos aquí

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);

        // Inicializamos el servicio de Retrofit
        apiService = RetrofitClient.getApiService();

        // Vinculamos vistas
        TextView tvLunes = findViewById(R.id.tvResumenLunes);
        TextView tvMartes = findViewById(R.id.tvResumenMartes);
        TextView tvMiercoles = findViewById(R.id.tvResumenMiercoles);
        TextView tvJueves = findViewById(R.id.tvResumenJueves);
        TextView tvViernes = findViewById(R.id.tvResumenViernes);

        // 3. RECIBIR LOS DATOS (Necesitamos IDs para el servidor y Nombres para la vista)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // 1. ASIGNAR A LOS TEXTVIEWS (Fijate que los nombres coincidan con el putExtra de arriba)
            tvLunes.setText(extras.getString("NombreLunes", "No llegó"));
            tvMartes.setText(extras.getString("NombreMartes", "No llegó"));
            tvMiercoles.setText(extras.getString("NombreMiercoles", "No llegó"));
            tvJueves.setText(extras.getString("NombreJueves", "No llegó"));
            tvViernes.setText(extras.getString("NombreViernes", "No llegó"));

            // 2. CARGAR EL REQUEST PARA EL SERVIDOR
            requestParaEnviar = new SeleccionRequest();
            requestParaEnviar.legajoUser = "BRUNO_TEST";
            requestParaEnviar.idMenu = 1;
            requestParaEnviar.detalles = new ArrayList<>();

            // Cargamos los IDs usando las mismas claves
            requestParaEnviar.detalles.add(new SeleccionRequest.DetalleDTO(extras.getInt("IdLunes", 0), 1));
            requestParaEnviar.detalles.add(new SeleccionRequest.DetalleDTO(extras.getInt("IdMartes", 0), 2));
            requestParaEnviar.detalles.add(new SeleccionRequest.DetalleDTO(extras.getInt("IdMiercoles", 0), 3));
            requestParaEnviar.detalles.add(new SeleccionRequest.DetalleDTO(extras.getInt("IdJueves", 0), 4));
            requestParaEnviar.detalles.add(new SeleccionRequest.DetalleDTO(extras.getInt("IdViernes", 0), 5));
        }

        // 4. BOTÓN FINALIZAR (El que envía los datos)
        Button btnFinalizar = findViewById(R.id.button2);
        btnFinalizar.setOnClickListener(v -> {
            enviarPedidoAlServidor();
        });

        // Botones de navegación
        ImageButton ibVolver = findViewById(R.id.ibVolver);
        ibVolver.setOnClickListener(v -> finish());
    }

    private void enviarPedidoAlServidor() {
        // Ejecutamos la llamada que definimos en la interfaz ApiService
        apiService.confirmarSeleccion(requestParaEnviar).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // SITODO SALIO BIEN: Vamos a la pantalla final
                    Toast.makeText(SelectedActivity.this, "¡Pedido guardado!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SelectedActivity.this, FinishedActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SelectedActivity.this, "Error en el servidor: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SelectedActivity.this, "Sin conexión con el servidor de la UTN", Toast.LENGTH_LONG).show();
            }
        });
    }
}