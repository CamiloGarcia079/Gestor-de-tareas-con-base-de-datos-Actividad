package modelo;

import java.time.LocalDate;

// Representa la tabla task
public class Tarea {
    private int id;
    private String titulo;
    private String descripcion;
    private String prioridad;      // Alta / Media / Baja
    private EstadoTarea estado;    // relacion con status_task
    private LocalDate fechaCreacion;
    private LocalDate fechaLimite;
    private Equipo equipo;         // relacion con team

    public Tarea(int id, String titulo, String descripcion, String prioridad,
                  EstadoTarea estado, LocalDate fechaCreacion, LocalDate fechaLimite, Equipo equipo) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaLimite = fechaLimite;
        this.equipo = equipo;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public EstadoTarea getEstado() { return estado; }
    public void setEstado(EstadoTarea estado) { this.estado = estado; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public LocalDate getFechaLimite() { return fechaLimite; }
    public Equipo getEquipo() { return equipo; }

    @Override
    public String toString() {
        return "[" + prioridad + "] " + titulo + " - " + estado.getNombre() + " (" + equipo.getNombre() + ")";
    }
}
