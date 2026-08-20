package modelo;

// Representa un registro del catalogo type_person (Scrum Master, Product Owner, Developer)
public class TipoPersona {
    private int id;
    private String nombre;
    private String descripcion;

    public TipoPersona(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() {
        return nombre; // asi se ve bonito dentro de un JComboBox
    }
}
