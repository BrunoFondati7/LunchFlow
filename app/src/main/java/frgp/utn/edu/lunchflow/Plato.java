package frgp.utn.edu.lunchflow;

import com.google.gson.annotations.SerializedName;

public class Plato {
    @SerializedName("id_plato") // Debe coincidir con el nombre en tu JSON/PostgreSQL
    private int id;
    @SerializedName("nombrePlato")
    private String titulo;
    @SerializedName("descripcionPlato")
    private String descripcion;
    private String urlImagen; // Cambiamos int por String

    // Actualizá el constructor y los getters/setters
    public Plato(String nombre, String descripcion, double precio, String urlImagen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }

    public String getUrlImagen() { return urlImagen; }
    // ... el resto de los métodos

    // Getters
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }

    public int getId() { return id; }
}