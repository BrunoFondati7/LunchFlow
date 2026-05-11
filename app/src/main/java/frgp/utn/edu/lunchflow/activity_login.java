package frgp.utn.edu.lunchflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_login extends AppCompatActivity {

    private EditText etLegajo, etPassword;
    private Button btnIngresar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Ajuste de insets para diseño EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Vinculamos los componentes
        etLegajo = findViewById(R.id.editTextUsuario);
        etPassword = findViewById(R.id.editTextPassword);
        btnIngresar = findViewById(R.id.button);

        // 2. Inicializamos el servicio de API
        apiService = RetrofitClient.getApiService();

        // 3. Lógica del botón
        btnIngresar.setOnClickListener(v -> {
            ejecutarLogin();
        });
    }

    private void ejecutarLogin() {
        String legajo = etLegajo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (legajo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completá todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creamos el objeto de petición que definimos antes
        LoginRequest request = new LoginRequest(legajo, password);

        // Llamada asincrónica al Backend
        apiService.login(request).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario user = response.body();

                    // --- PASO CLAVE: GUARDAR LA SESIÓN ---
                    SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("legajoReal", user.getLegajoUser());
                    editor.putString("nombreReal", user.getNombreUser());
                    editor.apply();

                    Toast.makeText(activity_login.this, "¡Hola " + user.getNombreUser() + "!", Toast.LENGTH_SHORT).show();

                    // Navegamos al MainActivity
                    Intent intent = new Intent(activity_login.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Cerramos el login para que no pueda volver atrás
                } else {
                    Toast.makeText(activity_login.this, "Legajo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("LUNCHFLOW_ERR", "Fallo en la red: " + t.getMessage());
                Toast.makeText(activity_login.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}