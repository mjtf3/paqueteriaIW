package com.paqueteria.dto;

import java.math.BigDecimal;

import com.paqueteria.model.TarifaRangoPeso;

public class TarifaRangoPesoDTO {

    private Integer pesoMinimo;
    private Integer pesoMaximo;
    private BigDecimal coste;
    private String descripcion;

    // Constructor vacío
    public TarifaRangoPesoDTO() {
    }

    // Constructor desde entidad
    public TarifaRangoPesoDTO(TarifaRangoPeso tarifa) {
        this.pesoMinimo = tarifa.getPesoMinimo();
        this.pesoMaximo = tarifa.getPesoMaximo();
        this.coste = tarifa.getCoste();
        this.descripcion = tarifa.getDescripcion();
    }

    // Getters y Setters
    public Integer getPesoMinimo() {
        return pesoMinimo;
    }

    public void setPesoMinimo(Integer pesoMinimo) {
        this.pesoMinimo = pesoMinimo;
    }

    public Integer getPesoMaximo() {
        return pesoMaximo;
    }

    public void setPesoMaximo(Integer pesoMaximo) {
        this.pesoMaximo = pesoMaximo;
    }

    public BigDecimal getCoste() {
        return coste;
    }

    public void setCoste(BigDecimal coste) {
        this.coste = coste;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
