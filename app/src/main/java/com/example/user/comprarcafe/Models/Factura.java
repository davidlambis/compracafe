package com.example.user.comprarcafe.Models;

public class Factura {
    private long idFactura;
    private long idVenta;
    private long idCliente;
    private String VoC;
    private String nombreEmpresa;
    private String nombresUsuario;
    private String apellidosUsuario;
    private String tipoCafe;
    private String kilosTotales;
    private String valorPago;
    private String fecha;
    private String nitEmpresa;
    private String nombresCliente;
    private String cedulaCliente;
    private String telefonoCliente;
    private String hora;
    private String ciudad;

    public Factura() {
        this.idFactura = idFactura;
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.VoC = VoC;
        this.nombreEmpresa = nombreEmpresa;
        this.nombresUsuario = nombresUsuario;
        this.apellidosUsuario = apellidosUsuario;
        this.tipoCafe = tipoCafe;
        this.kilosTotales = kilosTotales;
        this.valorPago = valorPago;
        this.fecha = fecha;
        this.nitEmpresa = nitEmpresa;
        this.nombresCliente = nombresCliente;
        this.cedulaCliente = cedulaCliente;
        this.telefonoCliente = telefonoCliente;
        this.hora = hora;
    }

    public long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(long idFactura) {
        this.idFactura = idFactura;
    }

    public long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(long idVenta) {
        this.idVenta = idVenta;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getVoC() {
        return VoC;
    }

    public void setVoC(String voC) {
        VoC = voC;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombresUsuario() {
        return nombresUsuario;
    }

    public void setNombresUsuario(String nombresUsuario) {
        this.nombresUsuario = nombresUsuario;
    }

    public String getTipoCafe() {
        return tipoCafe;
    }

    public void setTipoCafe(String tipoCafe) {
        this.tipoCafe = tipoCafe;
    }

    public String getApellidosUsuario() {
        return apellidosUsuario;
    }

    public void setApellidosUsuario(String apellidosUsuario) {
        this.apellidosUsuario = apellidosUsuario;
    }

    public String getKilosTotales() {
        return kilosTotales;
    }

    public void setKilosTotales(String kilosTotales) {
        this.kilosTotales = kilosTotales;
    }

    public String getValorPago() {
        return valorPago;
    }

    public void setValorPago(String valorPago) {
        this.valorPago = valorPago;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNitEmpresa() {
        return nitEmpresa;
    }

    public void setNitEmpresa(String nitEmpresa) {
        this.nitEmpresa = nitEmpresa;
    }

    public String getNombresCliente() {
        return nombresCliente;
    }

    public void setNombresCliente(String nombresCliente) {
        this.nombresCliente = nombresCliente;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
