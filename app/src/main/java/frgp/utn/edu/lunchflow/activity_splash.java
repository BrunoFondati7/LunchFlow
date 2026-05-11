package frgp.utn.edu.lunchflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class activity_splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Usamos un Handler para esperar 2 segundos (el tiempo del logo)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // CHEQUEO DE SESIÓN
            SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
            String legajo = prefs.getString("legajoReal", null);

            Intent intent;
            if (legajo != null) {
                // Ya está logueado -> Al menú principal
                intent = new Intent(activity_splash.this, MainActivity.class);
            } else {
                // No hay sesión -> Al login
                intent = new Intent(activity_splash.this, activity_login.class);
            }

            startActivity(intent);
            finish(); // Cerramos el splash para que no pueda volver atrás
        }, 2000); // 2000 milisegundos = 2 segundos
    }
}