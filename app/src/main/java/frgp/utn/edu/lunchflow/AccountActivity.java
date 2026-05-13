package frgp.utn.edu.lunchflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // 1. Vinculamos el TextView del usuario
        TextView tvUser = findViewById(R.id.tvUser);

        // 2. Recuperamos el nombre real de la sesión
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String nombreUsuario = prefs.getString("nombreReal", "Usuario");

        // 3. Lo mostramos en pantalla (reemplaza el "nameUser" del XML)
        tvUser.setText(nombreUsuario);

        Button btnHistorial = findViewById(R.id.button3);
        btnHistorial.setOnClickListener(v -> {
            startActivity(new Intent(AccountActivity.this, HistoryActivity.class));
        });

        // 4. Botón Volver al Menú
        Button btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerramos esta pantalla para regresar al MainActivity
                finish();
            }
        });

        // 5. Botón Cerrar Sesión
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // --- LIMPIEZA DE SESIÓN ---
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear(); // Borra legajo y nombre
                editor.apply();

                // Navegación al Login limpiando el historial
                Intent intent = new Intent(AccountActivity.this, activity_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}