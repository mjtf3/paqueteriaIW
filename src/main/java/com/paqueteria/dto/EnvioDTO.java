package com.paqueteria.dto;

public class EnvioDTO {
    private String localizador;
    private String status;
    private String direccionOrigen;
    private String direccionDestino;
    private String fecha;

    public EnvioDTO(String localizador, String status, String direccionOrigen,
                    String direccionDestino, String fecha) {
        this.localizador = localizador;
        this.status = status;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.fecha = fecha;
    }

    // Getters
    public String getLocalizador() {
        return localizador;
    }

    public String getStatus() {
        return status;
    }

    public String getDireccionOrigen() {
        return direccionOrigen;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public String getFecha() {
        return fecha;
    }

    // Setters
    public void setLocalizador(String localizador) {
        this.localizador = localizador;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
