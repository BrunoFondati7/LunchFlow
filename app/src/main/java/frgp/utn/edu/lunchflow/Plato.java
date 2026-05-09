package frgp.utn.edu.lunchflow;

public class Plato {
    private String titulo;
    private String descripcion;
    private int imagenResId; // Para el ID del dibujo en R.drawable

    public Plato(String titulo, String descripcion, int imagenResId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenResId = imagenResId;
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public int getImagenResId() { return imagenResId; }
}