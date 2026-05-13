package frgp.utn.edu.lunchflow;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    // IP especial para que el emulador vea la PC (localhost)
    private static final String BASE_URL = "http://192.168.0.88:8080/";

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}