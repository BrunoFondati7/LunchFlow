package frgp.utn.edu.lunchflow;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("legajoUser")
    private String legajoUser;

    @SerializedName("nombreUser")
    private String nombreUser;

    @SerializedName("apellidoUser")
    private String apellidoUser;

    @SerializedName("adminUser")
    private boolean adminUser;

    // Getters
    public String getLegajoUser() { return legajoUser; }
    public String getNombreUser() { return nombreUser; }
    public String getApellidoUser() { return apellidoUser; }
    public boolean isAdminUser() { return adminUser; }
}