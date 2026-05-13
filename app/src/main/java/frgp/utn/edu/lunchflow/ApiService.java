package frgp.utn.edu.lunchflow;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {


    @POST("api/usuarios/login")
    Call<Usuario> login(@Body LoginRequest loginRequest);

    @GET("api/platos")
    Call<List<Plato>> obtenerPlatos();

    @POST("api/selecciones/confirmar")
    Call<ResponseBody> confirmarSeleccion(@Body SeleccionRequest request);

    // Dejamos solo el historial, que es lo que ya tenemos listo en el Backend
    @GET("api/pedidos/historial/{legajo}")
    Call<List<PedidoDTO>> getHistorial(@Path("legajo") String legajo);
}