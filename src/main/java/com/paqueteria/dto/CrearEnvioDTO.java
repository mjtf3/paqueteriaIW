package com.paqueteria.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.paqueteria.model.DistanciaEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private DistanciaEnum distancia;

    private Boolean fragil = false;

    @NotNull(message = "El número de paquetes no puede ser nulo")
    @Min(value = 1, message = "Debe haber al menos un paquete")
    private Integer numeroPaquetes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "La fecha no puede ser nula")
    private LocalDate fecha;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

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

    public DistanciaEnum getDistancia() {
        return distancia;
    }

    public void setDistancia(DistanciaEnum distancia) {
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
