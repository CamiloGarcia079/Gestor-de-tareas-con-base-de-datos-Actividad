package dao;

import conexion.ConexionBD;
import modelo.Equipo;
import modelo.EstadoTarea;
import modelo.Tarea;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO {

    public void insertar(Tarea t) throws SQLException {
        String sql = "INSERT INTO task (titulo, descripcion, prioridad, id_estado, fecha_creacion, fecha_limite, id_equipo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setString(1, t.getTitulo());
            ps.setString(2, t.getDescripcion());
            ps.setString(3, t.getPrioridad());
            ps.setInt(4, t.getEstado().getId());
            ps.setDate(5, Date.valueOf(t.getFechaCreacion()));
            ps.setDate(6, t.getFechaLimite() != null ? Date.valueOf(t.getFechaLimite()) : null);
            ps.setInt(7, t.getEquipo().getId());
            ps.executeUpdate();
        }
    }

    public void actualizarEstado(int idTarea, int idEstado) throws SQLException {
        String sql = "UPDATE task SET id_estado = ? WHERE id = ?";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, idEstado);
            ps.setInt(2, idTarea);
            ps.executeUpdate();
        }
    }

    public List<Tarea> listar() throws SQLException {
        List<Tarea> lista = new ArrayList<>();
        String sql = "SELECT t.id, t.titulo, t.descripcion, t.prioridad, t.fecha_creacion, t.fecha_limite, " +
                "s.id AS estado_id, s.nombre AS estado_nombre, s.descripcion AS estado_desc, " +
                "e.id AS equipo_id, e.nombre AS equipo_nombre, e.descripcion AS equipo_desc " +
                "FROM task t " +
                "JOIN status_task s ON t.id_estado = s.id " +
                "JOIN team e ON t.id_equipo = e.id " +
                "ORDER BY t.id";
        try (Statement st = ConexionBD.getInstancia().getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                EstadoTarea estado = new EstadoTarea(rs.getInt("estado_id"), rs.getString("estado_nombre"), rs.getString("estado_desc"));
                Equipo equipo = new Equipo(rs.getInt("equipo_id"), rs.getString("equipo_nombre"), rs.getString("equipo_desc"));
                Date limite = rs.getDate("fecha_limite");
                lista.add(new Tarea(
                        rs.getInt("id"), rs.getString("titulo"), rs.getString("descripcion"), rs.getString("prioridad"),
                        estado, rs.getDate("fecha_creacion").toLocalDate(),
                        limite != null ? limite.toLocalDate() : null,
                        equipo));
            }
        }
        return lista;
    }

    // usado por el dashboard para contar tareas por estado
    public List<Object[]> contarPorEstado() throws SQLException {
        List<Object[]> resultado = new ArrayList<>();
        String sql = "SELECT s.nombre, COUNT(t.id) AS total FROM status_task s " +
                "LEFT JOIN task t ON t.id_estado = s.id GROUP BY s.id, s.nombre ORDER BY s.id";
        try (Statement st = ConexionBD.getInstancia().getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                resultado.add(new Object[]{rs.getString(1), rs.getInt(2)});
            }
        }
        return resultado;
    }

    public List<Object[]> contarPorPrioridad() throws SQLException {
        List<Object[]> resultado = new ArrayList<>();
        String sql = "SELECT prioridad, COUNT(*) FROM task GROUP BY prioridad";
        try (Statement st = ConexionBD.getInstancia().getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                resultado.add(new Object[]{rs.getString(1), rs.getInt(2)});
            }
        }
        return resultado;
    }
}
