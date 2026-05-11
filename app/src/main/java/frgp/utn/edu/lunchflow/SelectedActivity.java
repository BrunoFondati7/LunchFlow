package frgp.utn.edu.lunchflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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

        // Vinculamos vistas del XML
        TextView tvLunes = findViewById(R.id.tvResumenLunes);
        TextView tvMartes = findViewById(R.id.tvResumenMartes);
        TextView tvMiercoles = findViewById(R.id.tvResumenMiercoles);
        TextView tvJueves = findViewById(R.id.tvResumenJueves);
        TextView tvViernes = findViewById(R.id.tvResumenViernes);

        // 3. RECUPERAR EL LEGAJO REAL DESDE EL LOGIN
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String legajoReal = prefs.getString("legajoReal", "INVITADO");

        // 4. RECIBIR LOS DATOS DEL MENÚ (IDs y Nombres)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // ASIGNAR A LOS TEXTVIEWS (Para que el usuario vea qué eligió)
            tvLunes.setText(extras.getString("NombreLunes", "Sin plato"));
            tvMartes.setText(extras.getString("NombreMartes", "Sin plato"));
            tvMiercoles.setText(extras.getString("NombreMiercoles", "Sin plato"));
            tvJueves.setText(extras.getString("NombreJueves", "Sin plato"));
            tvViernes.setText(extras.getString("NombreViernes", "Sin plato"));

            // CARGAR EL REQUEST PARA EL SERVIDOR
            requestParaEnviar = new SeleccionRequest();

            // ¡CHAU HARDCODEO! Usamos el legajo recuperado de SharedPreferences
            requestParaEnviar.legajoUser = legajoReal;

            requestParaEnviar.idMenu = 1; // Asumimos menú 1 o el que maneje tu lógica
            requestParaEnviar.detalles = new ArrayList<>();

            // Cargamos los IDs de los platos seleccionados vinculados a cada día (1=Lunes, 2=Martes...)
            requestParaEnviar.detalles.add(new SeleccionRequest.DetalleDTO(extras.getInt("IdLunes", 0), 1));
            requestParaEnviar.detalles.add(new SeleccionRequest.DetalleDTO(extras.getInt("IdMartes", 0), 2));
            requestParaEnviar.detalles.add(new SeleccionRequest.DetalleDTO(extras.getInt("IdMiercoles", 0), 3));
            requestParaEnviar.detalles.add(new SeleccionRequest.DetalleDTO(extras.getInt("IdJueves", 0), 4));
            requestParaEnviar.detalles.add(new SeleccionRequest.DetalleDTO(extras.getInt("IdViernes", 0), 5));
        }

        // 5. BOTÓN FINALIZAR (Envía los datos al Backend)
        Button btnFinalizar = findViewById(R.id.button2);
        btnFinalizar.setOnClickListener(v -> {
            if (requestParaEnviar != null) {
                enviarPedidoAlServidor();
            } else {
                Toast.makeText(this, "Error: No hay datos para enviar", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón de navegación atrás
        ImageButton ibVolver = findViewById(R.id.ibVolver);
        ibVolver.setOnClickListener(v -> finish());
    }

    private void enviarPedidoAlServidor() {
        Log.d("LUNCHFLOW", "Enviando pedido para el legajo: " + requestParaEnviar.legajoUser);

        // Ejecutamos la llamada que definimos en la interfaz ApiService
        apiService.confirmarSeleccion(requestParaEnviar).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // SI TODO SALIÓ BIEN: Vamos a la pantalla de confirmación final
                    Toast.makeText(SelectedActivity.this, "¡Pedido guardado con éxito!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SelectedActivity.this, FinishedActivity.class);
                    startActivity(intent);
                    finish(); // Cerramos esta pantalla para que no pueda volver a editar
                } else {
                    Log.e("LUNCHFLOW_ERR", "Error en el servidor: " + response.code());
                    Toast.makeText(SelectedActivity.this, "Error en el servidor. Código: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("LUNCHFLOW_FAIL", "Fallo de conexión: " + t.getMessage());
                Toast.makeText(SelectedActivity.this, "Sin conexión con el servidor de la UTN", Toast.LENGTH_LONG).show();
            }
        });
    }
}