package com.gestortareas.models;

// Enum con los roles del equipo Scrum (5.15.4.7 Singleton con Enum aplicado a catálogo)
public enum TipoPersona {
    SCRUM_MASTER("Scrum Master"),
    PRODUCT_OWNER("Product Owner"),
    DEVELOPER("Developer");

    private final String descripcion;

    TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
