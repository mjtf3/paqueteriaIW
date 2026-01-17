package com.paqueteria.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CrearEnvioDTO {

    @NotBlank(message = "La dirección de origen no puede estar vacía")
    private String direccionOrigen;

    @NotBlank(message = "La dirección de destino no puede estar vacía")
    private String direccionDestino;

    @NotBlank(message = "El nombre del comprador no puede estar vacío")
    private String nombreComprador;

    private String nota;

    @NotNull(message = "El peso no puede ser nulo")
    @Min(value = 0, message = "El peso debe ser positivo")
    private BigDecimal peso;

    @NotNull(message = "La distancia no puede ser nula")
    @Min(value = 1, message = "La distancia debe estar entre 1 y 4")
    @Max(value = 4, message = "La distancia debe estar entre 1 y 4")
    private Integer distancia; // 1=CIUDAD, 2=PROVINCIAL, 3=NACIONAL, 4=INTERNACIONAL

    private Boolean fragil = false;

    @NotNull(message = "El número de paquetes no puede ser nulo")
    @Min(value = 1, message = "Debe haber al menos un paquete")
    private Integer numeroPaquetes;

    // Getters y Setters
    public String getDireccionOrigen() {
        return direccionOrigen;
    }

    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public Integer getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }

    public Boolean getFragil() {
        return fragil;
    }

    public void setFragil(Boolean fragil) {
        this.fragil = fragil;
    }

    public Integer getNumeroPaquetes() {
        return numeroPaquetes;
    }

    public void setNumeroPaquetes(Integer numeroPaquetes) {
        this.numeroPaquetes = numeroPaquetes;
    }
}
