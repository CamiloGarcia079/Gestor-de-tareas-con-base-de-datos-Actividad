package com.gestortareas.models;

// Modelo de asignación tarea ↔ persona — tabla assement_task en la BD
public class AssessmentTask {

    private int id;
    private int idTask;     // FK → task
    private int idPerson;   // FK → person
    private String nota;    // comentario de la asignación

    public AssessmentTask() {}

    public AssessmentTask(int id, int idTask, int idPerson, String nota) {
        this.id       = id;
        this.idTask   = idTask;
        this.idPerson = idPerson;
        this.nota     = nota;
    }

    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }

    public int getIdTask()                { return idTask; }
    public void setIdTask(int idTask)     { this.idTask = idTask; }

    public int getIdPerson()              { return idPerson; }
    public void setIdPerson(int idPerson) { this.idPerson = idPerson; }

    public String getNota()               { return nota; }
    public void setNota(String nota)      { this.nota = nota; }

    @Override
    public String toString() {
        return String.format("Asignación [tarea=%d → persona=%d] — %s", idTask, idPerson, nota);
    }
}
