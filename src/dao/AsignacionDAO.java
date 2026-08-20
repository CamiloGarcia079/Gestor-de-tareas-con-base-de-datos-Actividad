package dao;

import conexion.ConexionBD;
import modelo.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {

    public void insertar(Asignacion a) throws SQLException {
        String sql = "INSERT INTO assement_task (id_task, id_person, nota) VALUES (?, ?, ?)";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, a.getTarea().getId());
            ps.setInt(2, a.getPersona().getId());
            ps.setString(3, a.getNota());
            ps.executeUpdate();
        }
    }

    public List<Asignacion> listar() throws SQLException {
        List<Asignacion> lista = new ArrayList<>();
        TareaDAO tareaDAO = new TareaDAO();
        PersonaDAO personaDAO = new PersonaDAO();

        // cargamos en memoria las tareas y personas para no repetir consultas por cada fila
        List<Tarea> tareas = tareaDAO.listar();
        List<Persona> personas = personaDAO.listar();

        String sql = "SELECT id, id_task, id_person, nota FROM assement_task ORDER BY id";
        try (Statement st = ConexionBD.getInstancia().getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Tarea t = buscarTarea(tareas, rs.getInt("id_task"));
                Persona p = buscarPersona(personas, rs.getInt("id_person"));
                if (t != null && p != null) {
                    lista.add(new Asignacion(rs.getInt("id"), t, p, rs.getString("nota")));
                }
            }
        }
        return lista;
    }

    private Tarea buscarTarea(List<Tarea> tareas, int id) {
        for (Tarea t : tareas) if (t.getId() == id) return t;
        return null;
    }

    private Persona buscarPersona(List<Persona> personas, int id) {
        for (Persona p : personas) if (p.getId() == id) return p;
        return null;
    }
}
