package modelo;

// Representa la tabla assement_task: vincula una tarea con la persona responsable
public class Asignacion {
    private int id;
    private Tarea tarea;
    private Persona persona;
    private String nota;

    public Asignacion(int id, Tarea tarea, Persona persona, String nota) {
        this.id = id;
        this.tarea = tarea;
        this.persona = persona;
        this.nota = nota;
    }

    public int getId() { return id; }
    public Tarea getTarea() { return tarea; }
    public Persona getPersona() { return persona; }
    public String getNota() { return nota; }

    @Override
    public String toString() {
        return tarea.getTitulo() + " -> " + persona.getNombre() +
                (nota != null && !nota.trim().isEmpty() ? " (" + nota + ")" : "");
    }
}
