package com.paqueteria.model;

public enum EstadoEnum {
    PENDIENTE("EN ALMACEN"),
    RUTA("EN REPARTO"),
    ENTREGADO("ENTREGADO"),
    AUSENTE("AUSENTE"),
    RECHAZADO("RECHAZADO");

    private final String displayName;

    EstadoEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}