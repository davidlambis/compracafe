package com.example.user.comprarcafe.Models;

public class CafeVerde {
    private long idCafeVerde;
    private long idVenta;
    private int descuentoEstandar;
    private double cafeDañado;
    private String variedad;
    private int muestra;
    private double cafeBueno;
    private double valorCarga;
    private double valorTotal;

    public CafeVerde(){
        this.idCafeVerde = idCafeVerde;
        this.idVenta = idVenta;
        this.descuentoEstandar = descuentoEstandar;
        this.cafeDañado = cafeDañado;
        this.variedad = variedad;
        this.muestra = muestra;
        this.cafeBueno = cafeBueno;
        this.valorCarga = valorCarga;
        this.valorTotal = valorTotal;
    }

    public long getIdCafeVerde() {
        return idCafeVerde;
    }

    public void setIdCafeVerde(long idCafeVerde) {
        this.idCafeVerde = idCafeVerde;
    }

    public long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(long idVenta) {
        this.idVenta = idVenta;
    }

    public int getDescuentoEstandar() {
        return descuentoEstandar;
    }

    public void setDescuentoEstandar(int descuentoEstandar) {
        this.descuentoEstandar = descuentoEstandar;
    }

    public String getVariedad() {
        return variedad;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public double getCafeDañado() {
        return cafeDañado;
    }

    public void setCafeDañado(double cafeDañado) {
        this.cafeDañado = cafeDañado;
    }

    public int getMuestra() {
        return muestra;
    }

    public void setMuestra(int muestra) {
        this.muestra = muestra;
    }

    public double getCafeBueno() {
        return cafeBueno;
    }

    public void setCafeBueno(double cafeBueno) {
        this.cafeBueno = cafeBueno;
    }

    public double getValorCarga() {
        return valorCarga;
    }

    public void setValorCarga(double valorCarga) {
        this.valorCarga = valorCarga;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
