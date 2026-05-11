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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import frgp.utn.edu.lunchflow.ApiService;
import frgp.utn.edu.lunchflow.SeleccionRequest;

public class MainActivity extends AppCompatActivity {

    // --- VISTAS ---
    private ViewPager2 viewPager;
    private LinearLayout layoutIndicadores;
    private Button btnLun, btnMar, btnMie, btnJue, btnVie;

    // --- DATOS Y API ---
    private ApiService apiService;
    private List<Plato> listaPlatos; // Variable global para los datos del Backend
    private PlatoAdapter adapter;

    // --- LÓGICA DE SELECCIÓN ---
    private String diaSeleccionado = null; // El día que el usuario tiene "marcado" actualmente
    private HashMap<String, Plato> eleccionesSemanales = new HashMap<>();
    private Map<String, Button> botonesDias = new HashMap<>(); // Para manejar los colores fácilmente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Enlazamos vistas (ViewPager e Indicadores)
        viewPager = findViewById(R.id.viewPagerPlatos);
        layoutIndicadores = findViewById(R.id.layoutIndicadores);
        ImageButton botonCuenta = findViewById(R.id.botonCuenta);
        Button btnConfirmarSemana = findViewById(R.id.button2);
        TextView tvSemanaActual = findViewById(R.id.textView);

        // 2. Inicializamos los botones de los días y el mapa
        btnLun = findViewById(R.id.btnLun);
        btnMar = findViewById(R.id.btnMar);
        btnMie = findViewById(R.id.btnMie);
        btnJue = findViewById(R.id.btnJue);
        btnVie = findViewById(R.id.btnVie);

        botonesDias.put("Lunes", btnLun);
        botonesDias.put("Martes", btnMar);
        botonesDias.put("Miercoles", btnMie);
        botonesDias.put("Jueves", btnJue);
        botonesDias.put("Viernes", btnVie);

        // 2.5 Asignamos los clics a los botones de los días
        for (Map.Entry<String, Button> entry : botonesDias.entrySet()) {
            String dia = entry.getKey();
            Button boton = entry.getValue();

            boton.setOnClickListener(v -> {
                diaSeleccionado = dia; // Ahora sí, el día deja de ser null
                actualizarEstiloBotonesDias(); // Para que se pinte el seleccionado
                Toast.makeText(this, "Día seleccionado: " + dia, Toast.LENGTH_SHORT).show();
            });
        }

        // 3. Inicializamos datos y adaptador
        listaPlatos = new ArrayList<>();
        adapter = new PlatoAdapter(listaPlatos, plato -> {
            if (diaSeleccionado == null) {
                Toast.makeText(this, "Elegí un día", Toast.LENGTH_SHORT).show();
            } else {
                // Guardamos el objeto Plato completo, no solo el nombre
                eleccionesSemanales.put(diaSeleccionado, plato);

                actualizarEstiloBotonesDias();
                Toast.makeText(this, "Elegiste: " + plato.getTitulo(), Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Configuración visual del ViewPager2
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                marcarIndicadorSeleccionado(position);
            }
        });

        // 5. Listeners de los botones de días
        btnLun.setOnClickListener(v -> { diaSeleccionado = "Lunes"; actualizarEstiloBotonesDias(); });
        btnMar.setOnClickListener(v -> { diaSeleccionado = "Martes"; actualizarEstiloBotonesDias(); });
        btnMie.setOnClickListener(v -> { diaSeleccionado = "Miercoles"; actualizarEstiloBotonesDias(); });
        btnJue.setOnClickListener(v -> { diaSeleccionado = "Jueves"; actualizarEstiloBotonesDias(); });
        btnVie.setOnClickListener(v -> { diaSeleccionado = "Viernes"; actualizarEstiloBotonesDias(); });

        // 6. Botón Cuenta y Confirmar Semana
        botonCuenta.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AccountActivity.class)));

        btnConfirmarSemana.setOnClickListener(v -> {
            if (eleccionesSemanales.size() < 5) {
                Toast.makeText(this, "Faltan días", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, SelectedActivity.class);

                // Pasamos los datos uno por uno para asegurar que lleguen
                intent.putExtra("NombreLunes", eleccionesSemanales.get("Lunes").getTitulo());
                intent.putExtra("IdLunes", eleccionesSemanales.get("Lunes").getId());

                intent.putExtra("NombreMartes", eleccionesSemanales.get("Martes").getTitulo());
                intent.putExtra("IdMartes", eleccionesSemanales.get("Martes").getId());

                intent.putExtra("NombreMiercoles", eleccionesSemanales.get("Miercoles").getTitulo());
                intent.putExtra("IdMiercoles", eleccionesSemanales.get("Miercoles").getId());

                intent.putExtra("NombreJueves", eleccionesSemanales.get("Jueves").getTitulo());
                intent.putExtra("IdJueves", eleccionesSemanales.get("Jueves").getId());

                intent.putExtra("NombreViernes", eleccionesSemanales.get("Viernes").getTitulo());
                intent.putExtra("IdViernes", eleccionesSemanales.get("Viernes").getId());

                startActivity(intent);
            }
        });
        // 7. Texto de la semana y carga de API
        tvSemanaActual.setText(obtenerRangoSemana());

        // Usamos el metodo que creaste en tu RetrofitClient
        apiService = RetrofitClient.getApiService();

        cargarPlatosDesdeApi();


    }


    private void cargarPlatosDesdeApi() {
        apiService.obtenerPlatos().enqueue(new Callback<List<Plato>>() {
            @Override
            public void onResponse(Call<List<Plato>> call, Response<List<Plato>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 1. Limpiamos y llenamos la lista con datos de Postgres
                    listaPlatos.clear();
                    listaPlatos.addAll(response.body());

                    // 2. Avisamos al adaptador que hay platos nuevos
                    adapter.notifyDataSetChanged();

                    // 3. ¡IMPORTANTE! Ahora que sabemos cuántos platos hay,
                    // creamos los puntitos (indicadores)
                    configurarIndicadores(listaPlatos.size());
                    marcarIndicadorSeleccionado(0);
                }
            }

            @Override
            public void onFailure(Call<List<Plato>> call, Throwable t) {
                // Si el servidor está apagado o no hay internet, entra acá
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metodo para calcular el rango de fechas
    private String obtenerRangoSemana() {
        // Usamos Locale Argentina para que los nombres de los meses sean en español
        Calendar cal = Calendar.getInstance(new Locale("es", "AR"));

        // Lógica RF#4: Si hoy es viernes > 16hs, sábado o domingo, mostrar semana siguiente
        int diaActual = cal.get(Calendar.DAY_OF_WEEK);
        int horaActual = cal.get(Calendar.HOUR_OF_DAY);

        if (diaActual == Calendar.SATURDAY || diaActual == Calendar.SUNDAY ||
                (diaActual == Calendar.FRIDAY && horaActual >= 16)) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // FUERZA EL LUNES: Primero llevamos el calendario al inicio de la semana
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // Formato para el primer día (solo el número)
        SimpleDateFormat sdfInicio = new SimpleDateFormat("d", new Locale("es", "AR"));
        String inicio = sdfInicio.format(cal.getTime());

        // Formato para el viernes (número y mes)
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

    private void enviarSeleccionAlServidor() {
        // 0. MAPA DE DÍAS (Asegurate que esté dentro del método)
        Map<String, Integer> diasMap = new HashMap<>();
        diasMap.put("Lunes", 1);
        diasMap.put("Martes", 2);
        diasMap.put("Miercoles", 3);
        diasMap.put("Jueves", 4);
        diasMap.put("Viernes", 5);

        // 1. OBJETO REQUEST
        SeleccionRequest request = new SeleccionRequest();
        request.legajoUser = "12345";
        request.idMenu = 1;
        request.detalles = new ArrayList<>();

        // 2. CARGA DE DETALLES
        for (String nombreDia : eleccionesSemanales.keySet()) {
            Plato platoElegido = eleccionesSemanales.get(nombreDia);
            Integer nroDia = diasMap.get(nombreDia);

            if (platoElegido != null && nroDia != null) {
                SeleccionRequest.DetalleDTO nuevoDetalle = new SeleccionRequest.DetalleDTO(
                        platoElegido.getId(),
                        nroDia
                );
                request.detalles.add(nuevoDetalle);
            }
        }

        // 3. LLAMADA A RETROFIT
        apiService.confirmarSeleccion(request).enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // ¡ÉXITO!
                    Toast.makeText(MainActivity.this, "Selección confirmada correctamente", Toast.LENGTH_SHORT).show();

                    // RECIÉN ACÁ HACEMOS EL SALTO
                    Intent intent = new Intent(MainActivity.this, SelectedActivity.class);

                    // Pasamos los datos para el próximo layout
                    for (Map.Entry<String, Plato> entry : eleccionesSemanales.entrySet()) {
                        intent.putExtra(entry.getKey(), entry.getValue().getTitulo());
                    }

                    startActivity(intent);
                    finish(); // Opcional: para que no pueda volver atrás a editar
                } else {
                    // El servidor respondió, pero hubo un error (ej: error 500)
                    Toast.makeText(MainActivity.this, "Error: No se pudo confirmar la selección en el servidor", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Ni siquiera se pudo llegar al servidor (ej: PC apagada o sin WiFi)
                Toast.makeText(MainActivity.this, "Fallo de conexión: Revisá que el servidor esté corriendo", Toast.LENGTH_LONG).show();
            }
        });
    }

}