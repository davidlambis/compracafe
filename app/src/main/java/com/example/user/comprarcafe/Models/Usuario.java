package com.example.user.comprarcafe.Models;

/**
 * Created by user on 09/03/2017.
 */

public class Usuario {

    private long idUsuario;
    private long idEmpresa;
    private String estadoSesion;
    private String nombres;
    private String apellidos;
    private int cedula;
    private String direccion;
    private int telefono;
    private String correo;
    private String contraseña;

    public Usuario() {
        this.idUsuario = idUsuario;
        this.idEmpresa = idEmpresa;
        this.estadoSesion  = estadoSesion;
        this.nombres = nombres;
        this.cedula = cedula;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getEstadoSesion() {
        return estadoSesion;
    }

    public void setEstadoSesion(String estadoSesion) {
        this.estadoSesion = estadoSesion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
