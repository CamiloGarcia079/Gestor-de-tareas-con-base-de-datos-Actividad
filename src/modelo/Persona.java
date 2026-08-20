package modelo;

// Representa la tabla person: un integrante del equipo
public class Persona {
    private int id;
    private String nombre;
    private String email;
    private TipoPersona tipo;   // relacion con type_person
    private Equipo equipo;      // relacion con team (puede ser null si aun no tiene equipo)

    public Persona(int id, String nombre, String email, TipoPersona tipo, Equipo equipo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipo = tipo;
        this.equipo = equipo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public TipoPersona getTipo() { return tipo; }
    public void setTipo(TipoPersona tipo) { this.tipo = tipo; }
    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    @Override
    public String toString() {
        String eq = (equipo != null) ? equipo.getNombre() : "Sin equipo";
        return nombre + " (" + tipo.getNombre() + " - " + eq + ")";
    }
}
