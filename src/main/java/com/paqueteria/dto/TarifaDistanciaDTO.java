package com.paqueteria.dto;

import java.math.BigDecimal;

import com.paqueteria.model.TarifaDistancia;

public class TarifaDistanciaDTO {
    
    private Integer id;
    private String distancia;
    private BigDecimal coste;
    private Boolean activa;
    
    // Constructor vacío
    public TarifaDistanciaDTO() {}
    
    // Constructor desde entidad
    public TarifaDistanciaDTO(TarifaDistancia tarifa) {
        this.id = tarifa.getId();
        this.distancia = tarifa.getDistancia().name();
        this.coste = tarifa.getCoste();
        this.activa = tarifa.getActiva();
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
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
    
    public Boolean getActiva() {
        return activa;
    }
    
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
}
