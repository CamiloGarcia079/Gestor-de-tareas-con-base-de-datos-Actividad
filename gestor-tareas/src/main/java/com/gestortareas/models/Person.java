package com.gestortareas.models;

// Modelo de Persona — tabla person en la BD
public class Person {

    private int id;
    private String nombre;
    private String email;
    private TipoPersona tipo;      // FK → type_person (Scrum Master, PO, Developer)
    private int idEquipo;          // FK → team_person → team

    public Person() {}

    public Person(int id, String nombre, String email, TipoPersona tipo, int idEquipo) {
        this.id       = id;
        this.nombre   = nombre;
        this.email    = email;
        this.tipo     = tipo;
        this.idEquipo = idEquipo;
    }

    // Getters y setters
    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }

    public String getNombre()             { return nombre; }
    public void setNombre(String nombre)  { this.nombre = nombre; }

    public String getEmail()              { return email; }
    public void setEmail(String email)    { this.email = email; }

    public TipoPersona getTipo()                    { return tipo; }
    public void setTipo(TipoPersona tipo)            { this.tipo = tipo; }

    public int getIdEquipo()              { return idEquipo; }
    public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s)", id, nombre, tipo.getDescripcion());
    }
}
