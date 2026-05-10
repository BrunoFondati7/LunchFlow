package frgp.utn.edu.lunchflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private PlatoAdapter adapter;
    private LinearLayout layoutIndicadores;

    private String diaSeleccionado = null;
    private HashMap<String, Integer> eleccionesSemanales = new HashMap<>(); // Guarda "Lunes" -> ID_Plato

    // Variable para el día que el usuario tocó (L, M, M, J, V)
    private String diaActualSeleccionado = null;

    // Lista de botones para poder cambiarlos de color fácilmente
    private Map<String, Button> botonesDias = new HashMap<>();

    private Button btnLun, btnMar, btnMie, btnJue, btnVie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPagerPlatos);
        layoutIndicadores = findViewById(R.id.layoutIndicadores);

        // 1. Creamos la lista con los 5 platos
        List<Plato> listaPlatos = new ArrayList<>();
        listaPlatos.add(new Plato("Milanesa con Puré", "Clásica milanesa de carne con puré de papa.", R.drawable.plato1));
        listaPlatos.add(new Plato("Cerdo c/ Pure de Calabaza", "Carre de cerdo con puré de calabaza, combinación de sabores intensos y delicados.", R.drawable.plato2));
        listaPlatos.add(new Plato("Ensalada Caesar", "Pollo grillado, lechuga, croutons y aderezo.", R.drawable.plato3));
        listaPlatos.add(new Plato("Tarta de Verduras", "Delicada tarta de masa dorada, rellena con una selección de verduras frescas que aportan color, sabor y textura en cada bocado.", R.drawable.plato4));
        listaPlatos.add(new Plato("Arroz con Pollo", "Arroz cremoso y aromático, cocinado con tiernos trozos de pollo y un toque de especias que realzan su sabor tradicional.", R.drawable.plato5));

// 2. Configuramos el adaptador con el Listener (el puente)
        adapter = new PlatoAdapter(listaPlatos, plato -> {
            // ESTA LÓGICA SE DISPARA CUANDO TOCÁS "SELECCIONAR ESTE PLATO"
            if (diaSeleccionado == null) {
                // Si no eligió día, le avisamos
                Toast.makeText(MainActivity.this, "Por favor, elegí un día primero", Toast.LENGTH_SHORT).show();
            } else {
                // 1. Guardamos la elección en el mapa (Día -> ID del plato)
                // Usamos el ID o el nombre para que el sistema sepa que este día ya está "listo"
                eleccionesSemanales.put(diaSeleccionado, 1);

                // 2. Refrescamos TODOS los botones.
                // Como ya guardamos el dato en el mapa, este método pintará este día de VERDE
                // y mantendrá los otros confirmados también en VERDE.
                // Guardar la eleccion del PLATO.
                guardarEleccionLocal(diaSeleccionado, plato.getTitulo());
                actualizarEstiloBotonesDias();

                Toast.makeText(MainActivity.this, "Confirmado: " + plato.getTitulo() + " para el " + diaSeleccionado, Toast.LENGTH_SHORT).show();

                // 3. Reseteamos diaSeleccionado.
                // Esto es importante para que el usuario tenga que elegir OTRO día
                // antes de confirmar el siguiente plato.
                diaSeleccionado = null;
            }
        });


        viewPager.setAdapter(adapter);

        // 3. Configuramos los indicadores (puntitos)
        configurarIndicadores(listaPlatos.size());
        marcarIndicadorSeleccionado(0); // El primero empieza seleccionado

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                marcarIndicadorSeleccionado(position);
            }
        });

        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f); // Las cartas de los lados se achican un 15%
        });


        // En el onCreate, inicializalos:
        botonesDias.put("Lunes", findViewById(R.id.btnLun));
        botonesDias.put("Martes", findViewById(R.id.btnMar));
        botonesDias.put("Miercoles", findViewById(R.id.btnMie));
        botonesDias.put("Jueves", findViewById(R.id.btnJue));
        botonesDias.put("Viernes", findViewById(R.id.btnVie));

        // Inicializamos los botones
        btnLun = findViewById(R.id.btnLun);
        btnMar = findViewById(R.id.btnMar);
        btnMie = findViewById(R.id.btnMie);
        btnJue = findViewById(R.id.btnJue);
        btnVie = findViewById(R.id.btnVie);

        btnLun.setOnClickListener(v -> {
            diaSeleccionado = "Lunes";
            actualizarEstiloBotonesDias();
        });

        btnMar.setOnClickListener(v -> {
            diaSeleccionado = "Martes";
            actualizarEstiloBotonesDias();
        });

        btnMie.setOnClickListener(v -> {
            diaSeleccionado = "Miercoles";
            actualizarEstiloBotonesDias();
        });

        btnJue.setOnClickListener(v -> {
            diaSeleccionado = "Jueves";
            actualizarEstiloBotonesDias();
        });

        btnVie.setOnClickListener(v -> {
            diaSeleccionado = "Viernes";
            actualizarEstiloBotonesDias();
        });
        ImageButton botonCuenta = findViewById(R.id.botonCuenta);

        botonCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        Button btnConfirmarSemana = findViewById(R.id.button2);

        btnConfirmarSemana.setOnClickListener(v -> {
            // Verificamos si el usuario ya eligió los 5 días (Lunes a Viernes)
            if (eleccionesSemanales.size() < 5) {
                // Si faltan días, mostramos un aviso y NO pasamos de pantalla
                Toast.makeText(MainActivity.this,
                        "Faltan seleccionar " + (5 - eleccionesSemanales.size()) + " día(s)",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Si el tamaño es 5, significa que completó la semana
                Intent intent = new Intent(MainActivity.this, SelectedActivity.class);

                // Recuperamos los nombres de los platos de SharedPreferences para pasarlos
                SharedPreferences prefs = getSharedPreferences("SeleccionSemanal", MODE_PRIVATE);
                intent.putExtra("Lunes", prefs.getString("Lunes", "Sin elegir"));
                intent.putExtra("Martes", prefs.getString("Martes", "Sin elegir"));
                intent.putExtra("Miercoles", prefs.getString("Miercoles", "Sin elegir"));
                intent.putExtra("Jueves", prefs.getString("Jueves", "Sin elegir"));
                intent.putExtra("Viernes", prefs.getString("Viernes", "Sin elegir"));

                startActivity(intent);
            }
        });

        //Declaracion para el metodo de rango semanal.
        TextView tvSemanaActual = findViewById(R.id.textView);
        tvSemanaActual.setText(obtenerRangoSemana());
    }



    // Método para calcular el rango de fechas
    private String obtenerRangoSemana() {
        Calendar cal = Calendar.getInstance(new Locale("es", "AR"));

        // Lógica para el RF #4: Si ya es viernes después de las 16:00, mostramos la semana que viene
        int diaActual = cal.get(Calendar.DAY_OF_WEEK);
        int horaActual = cal.get(Calendar.HOUR_OF_DAY);

        if (diaActual > Calendar.FRIDAY || (diaActual == Calendar.FRIDAY && horaActual >= 16)) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // Ajustamos al lunes
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat sdfInicio = new SimpleDateFormat("d", new Locale("es", "AR"));
        String inicio = sdfInicio.format(cal.getTime());

        // Ajustamos al viernes
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        SimpleDateFormat sdfFin = new SimpleDateFormat("d 'de' MMMM", new Locale("es", "AR"));
        String fin = sdfFin.format(cal.getTime());

        return "Semana actual: " + inicio + " al " + fin;
    }

    private void actualizarEstiloBotonesDias() {
        // Recorremos todos los botones que tenemos guardados en el Map
        for (Map.Entry<String, Button> entry : botonesDias.entrySet()) {
            String nombreDia = entry.getKey();
            Button boton = entry.getValue();

            // 1. Si el día ya fue confirmado (está en el HashMap de elecciones), lo dejamos VERDE
            if (eleccionesSemanales.containsKey(nombreDia)) {
                boton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                boton.setTextColor(Color.WHITE);
            }
            // 2. Si es el día que acabamos de tocar para editar, lo ponemos en GRIS/BLANCO
            else if (nombreDia.equals(diaSeleccionado)) {
                boton.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                boton.setTextColor(Color.BLACK);
            }
            // 3. Si no está confirmado ni seleccionado, vuelve al color original (Celeste/Azul)
            else {
                boton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6395BD")));
                boton.setTextColor(Color.WHITE);
            }
        }
    }
    private void configurarIndicadores(int cantidad) {
        layoutIndicadores.removeAllViews(); // Limpiamos por las dudas
        ImageView[] indicadores = new ImageView[cantidad];

        // Convertimos 8dp a píxeles para que sea igual en todos los celus
        int tamañoPx = (int) (8 * getResources().getDisplayMetrics().density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(tamañoPx, tamañoPx);
        params.setMargins(10, 0, 10, 0); // Espaciado entre puntitos

        for (int i = 0; i < cantidad; i++) {
            indicadores[i] = new ImageView(this);
            indicadores[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicador_inactivo));
            indicadores[i].setLayoutParams(params);
            layoutIndicadores.addView(indicadores[i]);
        }
    }

    private void marcarIndicadorSeleccionado(int posicion) {
        int hijos = layoutIndicadores.getChildCount();
        for (int i = 0; i < hijos; i++) {
            ImageView imagen = (ImageView) layoutIndicadores.getChildAt(i);
            if (i == posicion) {
                imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicador_activo));
            } else {
                imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.indicador_inactivo));
            }
        }
    }

    private void guardarEleccionLocal(String dia, String plato) {
        SharedPreferences preferences = getSharedPreferences("SeleccionSemanal", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(dia, plato); // Guarda por ejemplo: "Lunes" -> "Milanesa con Puré"
        editor.apply();
    }

}