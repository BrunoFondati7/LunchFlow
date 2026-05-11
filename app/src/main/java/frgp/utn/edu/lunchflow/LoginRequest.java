package frgp.utn.edu.lunchflow;

public class LoginRequest {
    private String legajo;
    private String password;

    public LoginRequest(String legajo, String password) {
        this.legajo = legajo;
        this.password = password;
    }

    // Getters y Setters (Necesarios para que Retrofit pueda leerlos)
    public String getLegajo() { return legajo; }
    public void setLegajo(String legajo) { this.legajo = legajo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}