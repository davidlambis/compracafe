package com.example.user.comprarcafe.Models;

public class CafePasilla {
    private long idCafePasilla;
    private long idVenta;
    private double kilosTotalesPasilla;
    private double kilosZarandaPasilla;
    private double valorPuntoPasilla;
    private double rindePasilla;
    private String variedadPasilla;
    private int muestraPasilla;
    private double valorArrobaPasilla;
    private double valorTotalPasilla;

    public CafePasilla(){
        this.idCafePasilla = idCafePasilla;
        this.idVenta = idVenta;
        this.kilosTotalesPasilla = kilosTotalesPasilla;
        this.kilosZarandaPasilla = kilosZarandaPasilla;
        this.valorPuntoPasilla = valorPuntoPasilla;
        this.rindePasilla = rindePasilla;
        this.variedadPasilla = variedadPasilla;
        this.muestraPasilla = muestraPasilla;
        this.valorArrobaPasilla = valorArrobaPasilla;
        this.valorTotalPasilla = valorTotalPasilla;
    }

    public long getIdCafePasilla() {
        return idCafePasilla;
    }

    public void setIdCafePasilla(long idCafePasilla) {
        this.idCafePasilla = idCafePasilla;
    }

    public long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(long idVenta) {
        this.idVenta = idVenta;
    }

    public double getKilosTotalesPasilla() {
        return kilosTotalesPasilla;
    }

    public void setKilosTotalesPasilla(double kilosTotalesPasilla) {
        this.kilosTotalesPasilla = kilosTotalesPasilla;
    }

    public double getKilosZarandaPasilla() {
        return kilosZarandaPasilla;
    }

    public void setKilosZarandaPasilla(double kilosZarandaPasilla) {
        this.kilosZarandaPasilla = kilosZarandaPasilla;
    }

    public double getValorPuntoPasilla() {
        return valorPuntoPasilla;
    }

    public void setValorPuntoPasilla(double valorPuntoPasilla) {
        this.valorPuntoPasilla = valorPuntoPasilla;
    }

    public double getRindePasilla() {
        return rindePasilla;
    }

    public void setRindePasilla(double rindePasilla) {
        this.rindePasilla = rindePasilla;
    }

    public String getVariedadPasilla() {
        return variedadPasilla;
    }

    public void setVariedadPasilla(String variedadPasilla) {
        this.variedadPasilla = variedadPasilla;
    }

    public int getMuestraPasilla() {
        return muestraPasilla;
    }

    public void setMuestraPasilla(int muestraPasilla) {
        this.muestraPasilla = muestraPasilla;
    }

    public double getValorArrobaPasilla() {
        return valorArrobaPasilla;
    }

    public void setValorArrobaPasilla(double valorArrobaPasilla) {
        this.valorArrobaPasilla = valorArrobaPasilla;
    }

    public double getValorTotalPasilla() {
        return valorTotalPasilla;
    }

    public void setValorTotalPasilla(double valorTotalPasilla) {
        this.valorTotalPasilla = valorTotalPasilla;
    }
}
