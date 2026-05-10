package frgp.utn.edu.lunchflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FinishedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        // 1. Vinculamos el botón de "Volver al Menú"
        Button btnVolverMenu = findViewById(R.id.button2);

        // 2. Vinculamos los botones de la cabecera (opcional, por si querés que funcionen)
        ImageButton ibCuenta = findViewById(R.id.imageButton);
        ImageButton ibAtras = findViewById(R.id.imageButton2);

        // Lógica del botón principal
        btnVolverMenu.setOnClickListener(v -> {
            // A. LIMPIEZA DE DATOS: Borramos las SharedPreferences para que la semana empiece de cero
            SharedPreferences preferences = getSharedPreferences("SeleccionSemanal", MODE_PRIVATE);
            preferences.edit().clear().apply();

            // B. NAVEGACIÓN: Volvemos a la MainActivity
            Intent intent = new Intent(FinishedActivity.this, MainActivity.class);

            // Estas "flags" sirven para limpiar la pila de actividades y que el usuario
            // no pueda volver atrás a esta pantalla con el botón físico del celular.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish(); // Cerramos esta actividad
        });

        // Lógica para ir a la cuenta desde aquí si lo desea
        ibCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(FinishedActivity.this, AccountActivity.class);
            startActivity(intent);
        });

        // El botón atrás en esta pantalla final simplemente podría cerrar la app o volver al inicio
        ibAtras.setOnClickListener(v -> {
            finish();
        });
    }
}