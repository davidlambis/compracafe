package com.example.user.comprarcafe.Models;

public class Venta {
    private long idVenta;
    private long idUsuario;
    private String fecha;
    private String hora;
    private int precioDia;
    private double kilosTotales;
    private double valorTotal;
    private String tipo;
    private int muestra;

    public Venta() {
        this.idVenta = idVenta;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
        this.hora = hora;
        this.precioDia = precioDia;
        this.kilosTotales = kilosTotales;
        this.valorTotal = valorTotal;
        this.tipo = tipo;
        this.muestra = muestra;
    }

    public long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(long idVenta) {
        this.idVenta = idVenta;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(int precioDia) {
        this.precioDia = precioDia;
    }

    public double getKilosTotales() {
        return kilosTotales;
    }

    public void setKilosTotales(double kilosTotales) {
        this.kilosTotales = kilosTotales;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getMuestra() {
        return muestra;
    }

    public void setMuestra(int muestra) {
        this.muestra = muestra;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
