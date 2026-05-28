package com.example.tp_inmobiliariaceli.modelo;

import java.io.Serializable;

public class Contrato implements Serializable {
    private int idContrato;
    private String fechaInicio;
    private String fechaFin;
    private double monto;
    private Inmueble inmueble;
    private Inquilino inquilino;

    public Contrato() {
    }

    public Contrato(int idContrato, String fechaInicio, String fechaFin, double monto, Inmueble inmueble, Inquilino inquilino) {
        this.idContrato = idContrato;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.monto = monto;
        this.inmueble = inmueble;
        this.inquilino = inquilino;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }
}
