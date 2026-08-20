package com.gestortareas.models;

// Estados del ciclo de vida de una tarea (status_task)
public enum EstadoTarea {
    POR_HACER("Por hacer"),
    EN_PROCESO("En proceso"),
    EN_REVISION("En revisión"),
    FINALIZADO("Finalizado");

    private final String descripcion;

    EstadoTarea(String descripcion) {
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
