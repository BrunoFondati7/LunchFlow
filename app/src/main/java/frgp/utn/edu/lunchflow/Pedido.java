package frgp.utn.edu.lunchflow;

import com.google.gson.annotations.SerializedName;

public class Pedido {

    @SerializedName("fecha")
    private String fecha;

    @SerializedName("nombreMenu")
    private String nombreMenu;

    // Constructor vacío obligatorio para GSON
    public Pedido() {
    }

    public Pedido(String fecha, String nombreMenu) {
        this.fecha = fecha;
        this.nombreMenu = nombreMenu;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombreMenu() {
        return nombreMenu;
    }

    public void setNombreMenu(String nombreMenu) {
        this.nombreMenu = nombreMenu;
    }
}