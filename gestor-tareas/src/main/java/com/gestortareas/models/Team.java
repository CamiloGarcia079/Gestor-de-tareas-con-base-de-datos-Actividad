package com.gestortareas.models;

// Modelo de Equipo — tabla team en la BD
public class Team {

    private int id;
    private String nombre;
    private String descripcion;

    public Team() {}

    public Team(int id, String nombre, String descripcion) {
        this.id          = id;
        this.nombre      = nombre;
        this.descripcion = descripcion;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public String getNombre()                   { return nombre; }
    public void setNombre(String nombre)        { this.nombre = nombre; }

    public String getDescripcion()              { return descripcion; }
    public void setDescripcion(String d)        { this.descripcion = d; }

    @Override
    public String toString() {
        return String.format("[%d] %s — %s", id, nombre, descripcion);
    }
}
