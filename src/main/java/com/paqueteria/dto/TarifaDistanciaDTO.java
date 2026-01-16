package com.paqueteria.dto;

import java.math.BigDecimal;

import com.paqueteria.model.TarifaDistancia;

public class TarifaDistanciaDTO {

    private String distancia;
    private BigDecimal coste;

    // Constructor vacío
    public TarifaDistanciaDTO() {
    }

    // Constructor desde entidad
    public TarifaDistanciaDTO(TarifaDistancia tarifa) {
        this.distancia = tarifa.getDistancia().name();
        this.coste = tarifa.getCoste();
    }

    // Getters y Setters
    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public BigDecimal getCoste() {
        return coste;
    }

    public void setCoste(BigDecimal coste) {
        this.coste = coste;
    }
}
