package com.gestortareas.ui;

import com.gestortareas.crud.*;
import com.gestortareas.models.*;
import com.gestortareas.persistence.ConexionMySQL;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Menú principal del Gestor de Tareas Scrum.
 * Aplica conceptos del temario:
 *   - Enum (5.15.4.7) para TipoPersona, Prioridad, EstadoTarea
 *   - Singleton + Double-Checked Locking (5.15.4.5) en ConexionMySQL
 *   - CRUD con PreparedStatement (5.17.5.1)
 *   - ArrayList + forEach con lambda (3.8 / 5.19)
 *   - Switch con arrow (Java 17)
 */
public class Main {

    static final Scanner sc = new Scanner(System.in);
    static final TaskCRUD           taskCRUD       = new TaskCRUD();
    static final PersonCRUD         personCRUD     = new PersonCRUD();
    static final TeamCRUD           teamCRUD       = new TeamCRUD();
    static final AssessmentTaskCRUD assessmentCRUD = new AssessmentTaskCRUD();

    public static void main(String[] args) {
        System.out.println("\n🚀 Bienvenido al Gestor de Tareas Scrum");
        int op;
        do {
            menuPrincipal();
            op = leerInt("Opción");
            switch (op) {
                case 1 -> menuEquipos();
                case 2 -> menuPersonas();
                case 3 -> menuTareas();
                case 4 -> menuAsignaciones();
                case 5 -> taskCRUD.mostrarDashboard();
                case 0 -> {
                    ConexionMySQL.getInstancia().cerrar();
                    System.out.println("👋 ¡Hasta luego!");
                }
                default -> System.out.println("⚠️  Opción no válida.");
            }
        } while (op != 0);
        sc.close();
    }

    // ── MENÚS ──────────────────────────────────────────────────────────────────

    static void menuPrincipal() {
        System.out.println("""
                \n╔═════════════════════════════════╗
                ║    GESTOR DE TAREAS SCRUM       ║
                ╠═════════════════════════════════╣
                ║  1. Equipos                     ║
                ║  2. Personas                    ║
                ║  3. Tareas                      ║
                ║  4. Asignaciones tarea-persona  ║
                ║  5. Dashboard                   ║
                ║  0. Salir                       ║
                ╚═════════════════════════════════╝""");
    }

    // ── EQUIPOS ────────────────────────────────────────────────────────────────
    static void menuEquipos() {
        int op;
        do {
            System.out.println("""
                    \n── EQUIPOS ──────────────────────────
                    1. Crear equipo
                    2. Listar equipos
                    3. Actualizar equipo
                    4. Eliminar equipo
                    0. Volver""");
            op = leerInt("Opción");
            switch (op) {
                case 1 -> crearEquipo();
                case 2 -> listarEquipos();
                case 3 -> actualizarEquipo();
                case 4 -> { int id = leerInt("ID del equipo a eliminar"); teamCRUD.eliminar(id); }
                case 0 -> {}
                default -> System.out.println("⚠️  Opción no válida.");
            }
        } while (op != 0);
    }

    static void crearEquipo() {
        System.out.print("Nombre del equipo: ");
        String nombre = sc.nextLine();
        System.out.print("Descripción: ");
        String desc = sc.nextLine();
        teamCRUD.crear(new Team(0, nombre, desc));
    }

    static void listarEquipos() {
        List<Team> equipos = teamCRUD.listarTodos();
        if (equipos.isEmpty()) { System.out.println("Sin equipos registrados."); return; }
        System.out.println("\n📋 Equipos:");
        equipos.forEach(e -> System.out.println("  " + e));
    }

    static void actualizarEquipo() {
        listarEquipos();
        int id = leerInt("ID del equipo a actualizar");
        Team t = teamCRUD.buscarPorId(id);
        if (t == null) { System.out.println("No existe."); return; }
        System.out.print("Nuevo nombre [" + t.getNombre() + "]: ");
        String n = sc.nextLine(); if (!n.isBlank()) t.setNombre(n);
        System.out.print("Nueva descripción [" + t.getDescripcion() + "]: ");
        String d = sc.nextLine(); if (!d.isBlank()) t.setDescripcion(d);
        teamCRUD.actualizar(t);
    }

    // ── PERSONAS ───────────────────────────────────────────────────────────────
    static void menuPersonas() {
        int op;
        do {
            System.out.println("""
                    \n── PERSONAS ─────────────────────────
                    1. Crear persona
                    2. Listar todas las personas
                    3. Listar personas por equipo
                    4. Actualizar persona
                    5. Eliminar persona
                    0. Volver""");
            op = leerInt("Opción");
            switch (op) {
                case 1 -> crearPersona();
                case 2 -> listarPersonas();
                case 3 -> { int id = leerInt("ID del equipo"); personCRUD.listarPorEquipo(id).forEach(p -> System.out.println("  " + p)); }
                case 4 -> actualizarPersona();
                case 5 -> { int id = leerInt("ID persona a eliminar"); personCRUD.eliminar(id); }
                case 0 -> {}
                default -> System.out.println("⚠️  Opción no válida.");
            }
        } while (op != 0);
    }

    static void crearPersona() {
        listarEquipos();
        int idEquipo = leerInt("ID del equipo");
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.println("Tipo de persona:");
        for (TipoPersona tp : TipoPersona.values())
            System.out.println("  " + (tp.ordinal() + 1) + ". " + tp.getDescripcion());
        int t = leerInt("Tipo") - 1;
        TipoPersona tipo = TipoPersona.values()[Math.max(0, Math.min(t, TipoPersona.values().length - 1))];
        personCRUD.crear(new Person(0, nombre, email, tipo, idEquipo));
    }

    static void listarPersonas() {
        List<Person> personas = personCRUD.listarTodos();
        if (personas.isEmpty()) { System.out.println("Sin personas registradas."); return; }
        System.out.println("\n👥 Personas:");
        personas.forEach(p -> System.out.println("  " + p));
    }

    static void actualizarPersona() {
        listarPersonas();
        int id = leerInt("ID de la persona a actualizar");
        Person p = personCRUD.buscarPorId(id);
        if (p == null) { System.out.println("No existe."); return; }
        System.out.print("Nuevo nombre [" + p.getNombre() + "]: ");
        String n = sc.nextLine(); if (!n.isBlank()) p.setNombre(n);
        System.out.print("Nuevo email [" + p.getEmail() + "]: ");
        String e = sc.nextLine(); if (!e.isBlank()) p.setEmail(e);
        personCRUD.actualizar(p);
    }

    // ── TAREAS ─────────────────────────────────────────────────────────────────
    static void menuTareas() {
        int op;
        do {
            System.out.println("""
                    \n── TAREAS ───────────────────────────
                    1. Crear tarea
                    2. Listar todas las tareas
                    3. Listar por equipo
                    4. Filtrar por prioridad
                    5. Filtrar por estado
                    6. Cambiar estado de tarea
                    7. Actualizar tarea
                    8. Eliminar tarea
                    0. Volver""");
            op = leerInt("Opción");
            switch (op) {
                case 1 -> crearTarea();
                case 2 -> listarTareas();
                case 3 -> { int id = leerInt("ID del equipo"); taskCRUD.listarPorEquipo(id).forEach(t -> System.out.println("  " + t)); }
                case 4 -> filtrarPrioridad();
                case 5 -> filtrarEstado();
                case 6 -> cambiarEstado();
                case 7 -> actualizarTarea();
                case 8 -> { int id = leerInt("ID tarea a eliminar"); taskCRUD.eliminar(id); }
                case 0 -> {}
                default -> System.out.println("⚠️  Opción no válida.");
            }
        } while (op != 0);
    }

    static void crearTarea() {
        listarEquipos();
        int idEquipo = leerInt("ID del equipo");
        System.out.print("Título: ");
        String titulo = sc.nextLine();
        System.out.print("Descripción: ");
        String desc = sc.nextLine();
        System.out.println("Prioridad:  1=Alta  2=Media  3=Baja");
        int p = leerInt("Prioridad") - 1;
        Prioridad prioridad = Prioridad.values()[Math.max(0, Math.min(p, 2))];
        System.out.print("Fecha límite (YYYY-MM-DD, Enter para omitir): ");
        String fechaStr = sc.nextLine();
        LocalDate fechaLimite = fechaStr.isBlank() ? null : LocalDate.parse(fechaStr);
        Task t = new Task(0, titulo, desc, prioridad, EstadoTarea.POR_HACER,
                          LocalDate.now(), fechaLimite, idEquipo);
        taskCRUD.crear(t);
    }

    static void listarTareas() {
        List<Task> tareas = taskCRUD.listarTodas();
        if (tareas.isEmpty()) { System.out.println("Sin tareas."); return; }
        System.out.println("\n📝 Tareas:");
        tareas.forEach(t -> System.out.println("  " + t));
    }

    static void filtrarPrioridad() {
        System.out.println("1=Alta  2=Media  3=Baja");
        int p = leerInt("Prioridad") - 1;
        Prioridad pr = Prioridad.values()[Math.max(0, Math.min(p, 2))];
        taskCRUD.listarPorPrioridad(pr).forEach(t -> System.out.println("  " + t));
    }

    static void filtrarEstado() {
        EstadoTarea[] estados = EstadoTarea.values();
        for (int i = 0; i < estados.length; i++)
            System.out.println((i + 1) + ". " + estados[i].getDescripcion());
        int e = leerInt("Estado") - 1;
        EstadoTarea estado = estados[Math.max(0, Math.min(e, estados.length - 1))];
        taskCRUD.listarPorEstado(estado).forEach(t -> System.out.println("  " + t));
    }

    static void cambiarEstado() {
        listarTareas();
        int id = leerInt("ID de la tarea");
        EstadoTarea[] estados = EstadoTarea.values();
        for (int i = 0; i < estados.length; i++)
            System.out.println((i + 1) + ". " + estados[i].getDescripcion());
        int e = leerInt("Nuevo estado") - 1;
        EstadoTarea nuevoEstado = estados[Math.max(0, Math.min(e, estados.length - 1))];
        taskCRUD.cambiarEstado(id, nuevoEstado);
    }

    static void actualizarTarea() {
        listarTareas();
        int id = leerInt("ID de la tarea a actualizar");
        Task t = taskCRUD.buscarPorId(id);
        if (t == null) { System.out.println("No existe."); return; }
        System.out.print("Nuevo título [" + t.getTitulo() + "]: ");
        String titulo = sc.nextLine(); if (!titulo.isBlank()) t.setTitulo(titulo);
        System.out.print("Nueva descripción [" + t.getDescripcion() + "]: ");
        String desc = sc.nextLine(); if (!desc.isBlank()) t.setDescripcion(desc);
        taskCRUD.actualizar(t);
    }

    // ── ASIGNACIONES ──────────────────────────────────────────────────────────
    static void menuAsignaciones() {
        int op;
        do {
            System.out.println("""
                    \n── ASIGNACIONES ─────────────────────
                    1. Asignar tarea a persona
                    2. Ver personas asignadas a una tarea
                    3. Ver tareas asignadas a una persona
                    4. Eliminar asignación
                    0. Volver""");
            op = leerInt("Opción");
            switch (op) {
                case 1 -> asignarTarea();
                case 2 -> { int id = leerInt("ID de la tarea"); System.out.println("\nPersonas asignadas:"); assessmentCRUD.listarPorTarea(id); }
                case 3 -> { int id = leerInt("ID de la persona"); System.out.println("\nTareas asignadas:"); assessmentCRUD.listarPorPersona(id); }
                case 4 -> { int id = leerInt("ID de la asignación"); assessmentCRUD.eliminar(id); }
                case 0 -> {}
                default -> System.out.println("⚠️  Opción no válida.");
            }
        } while (op != 0);
    }

    static void asignarTarea() {
        listarTareas();
        int idTask = leerInt("ID de la tarea");
        listarPersonas();
        int idPerson = leerInt("ID de la persona");
        System.out.print("Nota (opcional): ");
        String nota = sc.nextLine();
        assessmentCRUD.asignar(new AssessmentTask(0, idTask, idPerson, nota));
    }

    // ── Utilidades ────────────────────────────────────────────────────────────
    static int leerInt(String label) {
        System.out.print(label + ": ");
        while (true) {
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Solo números. " + label + ": ");
            }
        }
    }
}
