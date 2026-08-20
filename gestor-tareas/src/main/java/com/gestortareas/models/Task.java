package com.gestortareas.models;

import java.time.LocalDate;

// Modelo de Tarea — tabla task en la BD
public class Task {

    private int id;
    private String titulo;
    private String descripcion;
    private Prioridad prioridad;       // Alta / Media / Baja
    private EstadoTarea estado;        // FK → status_task
    private LocalDate fechaCreacion;
    private LocalDate fechaLimite;
    private int idEquipo;              // FK → team

    public Task() {}

    public Task(int id, String titulo, String descripcion,
                Prioridad prioridad, EstadoTarea estado,
                LocalDate fechaCreacion, LocalDate fechaLimite,
                int idEquipo) {
        this.id             = id;
        this.titulo         = titulo;
        this.descripcion    = descripcion;
        this.prioridad      = prioridad;
        this.estado         = estado;
        this.fechaCreacion  = fechaCreacion;
        this.fechaLimite    = fechaLimite;
        this.idEquipo       = idEquipo;
    }

    // Getters y setters
    public int getId()                            { return id; }
    public void setId(int id)                     { this.id = id; }

    public String getTitulo()                     { return titulo; }
    public void setTitulo(String titulo)          { this.titulo = titulo; }

    public String getDescripcion()                { return descripcion; }
    public void setDescripcion(String d)          { this.descripcion = d; }

    public Prioridad getPrioridad()               { return prioridad; }
    public void setPrioridad(Prioridad p)         { this.prioridad = p; }

    public EstadoTarea getEstado()                { return estado; }
    public void setEstado(EstadoTarea e)          { this.estado = e; }

    public LocalDate getFechaCreacion()           { return fechaCreacion; }
    public void setFechaCreacion(LocalDate f)     { this.fechaCreacion = f; }

    public LocalDate getFechaLimite()             { return fechaLimite; }
    public void setFechaLimite(LocalDate f)       { this.fechaLimite = f; }

    public int getIdEquipo()                      { return idEquipo; }
    public void setIdEquipo(int idEquipo)         { this.idEquipo = idEquipo; }

    @Override
    public String toString() {
        return String.format("[%d] [%s] %s | Estado: %s | Límite: %s",
                id, prioridad.getDescripcion(), titulo,
                estado.getDescripcion(),
                fechaLimite != null ? fechaLimite.toString() : "Sin fecha");
    }
}
