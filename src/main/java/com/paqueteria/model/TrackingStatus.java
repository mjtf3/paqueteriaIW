package com.paqueteria.model;

public enum TrackingStatus {
    EN_ALMACEN("En almacén"),
    EN_REPARTO("En reparto"),
    ENTREGADO("Entregado");

    private final String label;

    TrackingStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

