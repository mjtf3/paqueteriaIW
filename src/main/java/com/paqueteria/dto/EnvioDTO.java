package com.paqueteria.dto;

import java.util.List;

public class EnvioDTO {
    private String code;
    private String status;
    private String label;
    private String direccionOrigen;
    private String direccionDestino;
    private String fecha;
    private List<String> historial;

    public EnvioDTO(String code, String status, String label, String direccionOrigen,
                    String direccionDestino, String fecha, List<String> historial) {
        this.code = code;
        this.status = status;
        this.label = label;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.fecha = fecha;
        this.historial = historial;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getLabel() {
        return label;
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

    public List<String> getHistorial() {
        return historial;
    }

    // Setters
    public void setCode(String code) {
        this.code = code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public void setHistorial(List<String> historial) {
        this.historial = historial;
    }
}
