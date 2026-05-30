package com.example.tp_inmobiliariaceli.modelo;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Inmueble implements Serializable {
    private int idInmueble;
    private String direccion;
    private int ambientes;
    private String tipo;
    private String uso;

    @SerializedName("valor")
    private double precio;

    private boolean disponible;
    private int idPropietario;
    private String imagen;

    // --- NUEVOS CAMPOS OBLIGATORIOS PARA LA API ---
    private int superficie;
    private double latitud;
    private double longitud;

    public Inmueble() {
    }

    public Inmueble(int idInmueble, String direccion, int ambientes, String tipo, String uso, double precio, boolean disponible, int idPropietario, String imagen, int superficie, double latitud, double longitud) {
        this.idInmueble = idInmueble;
        this.direccion = direccion;
        this.ambientes = ambientes;
        this.tipo = tipo;
        this.uso = uso;
        this.precio = precio;
        this.disponible = disponible;
        this.idPropietario = idPropietario;
        this.imagen = imagen;
        this.superficie = superficie;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // --- GETTERS Y SETTERS ORIGINALES ---
    public int getIdInmueble() { return idInmueble; }
    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public int getAmbientes() { return ambientes; }
    public void setAmbientes(int ambientes) { this.ambientes = ambientes; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public int getIdPropietario() { return idPropietario; }
    public void setIdPropietario(int idPropietario) { this.idPropietario = idPropietario; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    // --- GETTERS Y SETTERS NUEVOS ---
    public int getSuperficie() { return superficie; }
    public void setSuperficie(int superficie) { this.superficie = superficie; }
    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }
    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    @Override
    public String toString() {
        return direccion;
    }
}