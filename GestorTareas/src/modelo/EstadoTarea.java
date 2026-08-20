package modelo;

// Representa un registro del catalogo status_task (Por hacer, En proceso, Finalizada)
public class EstadoTarea {
    private int id;
    private String nombre;
    private String descripcion;

    public EstadoTarea(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() {
        return nombre;
    }
}
