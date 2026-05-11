package frgp.utn.edu.lunchflow; // Asegurate que el paquete sea correcto

import frgp.utn.edu.lunchflow.Plato; // Importá tu clase Plato
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/usuarios/login")
    Call<Usuario> login(@Body LoginRequest loginRequest);

    // Este metodo le dice a Retrofit: "Hacé un GET a /api/platos"
    @GET("api/platos")
    Call<List<Plato>> obtenerPlatos();

    @POST("api/selecciones/confirmar")
    Call<ResponseBody> confirmarSeleccion(@Body SeleccionRequest request);


}