package frgp.utn.edu.lunchflow;

import java.util.List;

public class SeleccionRequest {
    public String legajoUser;
    public Integer idMenu;
    public List<DetalleDTO> detalles;

    public static class DetalleDTO {
        public Integer idPlato;
        public Integer diaSemana;
        public Integer cantidad;

        public DetalleDTO(Integer idPlato, Integer diaSemana) {
            this.idPlato = idPlato;
            this.diaSemana = diaSemana;
            this.cantidad = 1;
        }
    }
}