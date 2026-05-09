package frgp.utn.edu.lunchflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private PlatoAdapter adapter;
    private LinearLayout layoutIndicadores;

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

        // 2. Configuramos el adaptador
        adapter = new PlatoAdapter(listaPlatos);
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

        ImageButton botonCuenta = findViewById(R.id.botonCuenta);

        botonCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void configurarIndicadores(int cantidad) {
        ImageView[] indicadores = new ImageView[cantidad];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 0, 8, 0);

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



}