package frgp.utn.edu.lunchflow;

public class PedidoDTO {
    private String fecha;
    private String nombreMenu; // <--- DEBE llamarse igual que en el Backend

    public PedidoDTO() {}

    public PedidoDTO(String fecha, String nombreMenu) {
        this.fecha = fecha;
        this.nombreMenu = nombreMenu;
    }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getNombreMenu() { return nombreMenu; }
    public void setNombreMenu(String nombreMenu) { this.nombreMenu = nombreMenu; }
}