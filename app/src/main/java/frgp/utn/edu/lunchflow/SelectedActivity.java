package frgp.utn.edu.lunchflow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import frgp.utn.edu.lunchflow.R;

public class SelectedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);

        // 1. Vinculamos los TextViews (asegurante que los IDs coincidan con tu XML)
        TextView tvLunes = findViewById(R.id.tvResumenLunes);
        TextView tvMartes = findViewById(R.id.tvResumenMartes);
        TextView tvMiercoles = findViewById(R.id.tvResumenMiercoles);
        TextView tvJueves = findViewById(R.id.tvResumenJueves);
        TextView tvViernes = findViewById(R.id.tvResumenViernes);

        // 2. Recibimos los datos del Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvLunes.setText(extras.getString("Lunes"));
            tvMartes.setText(extras.getString("Martes"));
            tvMiercoles.setText(extras.getString("Miercoles"));
            tvJueves.setText(extras.getString("Jueves"));
            tvViernes.setText(extras.getString("Viernes"));
        }
        // 1. Vinculamos los botones por su ID
        ImageButton ibVolver = findViewById(R.id.ibVolver); // El ID de tu XML era imageButton2
        ImageButton ibCuenta = findViewById(R.id.ibCuenta);  // El ID de tu XML era imageButton

        // 2. Funcionalidad para VOLVER
        ibVolver.setOnClickListener(v -> {
            // Al usar finish() volvemos a la pantalla anterior (MainActivity)
            // sin crear una nueva instancia, ahorrando memoria.
            finish();
        });
        // 3. Funcionalidad para ir a CUENTA
        ibCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(SelectedActivity.this, AccountActivity.class);
            startActivity(intent);
        });
        // 1. Vinculamos el botón por su ID
        Button btnFinalizar = findViewById(R.id.button2);

        // 2. Le damos la funcionalidad para saltar a la última pantalla
        btnFinalizar.setOnClickListener(v -> {
            Intent intent = new Intent(SelectedActivity.this, FinishedActivity.class);
            startActivity(intent);

            // Opcional: Podés usar finish() si no querés que el usuario
            // pueda volver atrás al resumen una vez que ya terminó.
            finish();
        });
    }
}