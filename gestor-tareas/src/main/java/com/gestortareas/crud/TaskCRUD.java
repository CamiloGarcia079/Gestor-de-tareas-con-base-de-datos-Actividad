package com.gestortareas.crud;

import com.gestortareas.models.EstadoTarea;
import com.gestortareas.models.Prioridad;
import com.gestortareas.models.Task;
import com.gestortareas.persistence.ConexionMySQL;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// CRUD de la tabla task — PreparedStatement (5.17.5.1)
public class TaskCRUD {

    private final Connection con = ConexionMySQL.getInstancia().getConexion();

    // ── INSERT ────────────────────────────────────────────────────────────────
    public boolean crear(Task t) {
        String sql = "INSERT INTO task (titulo, descripcion, prioridad, estado, fecha_creacion, fecha_limite, id_equipo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getTitulo());
            ps.setString(2, t.getDescripcion());
            ps.setString(3, t.getPrioridad().name());
            ps.setString(4, t.getEstado().name());
            ps.setDate(5, Date.valueOf(t.getFechaCreacion()));
            ps.setDate(6, t.getFechaLimite() != null ? Date.valueOf(t.getFechaLimite()) : null);
            ps.setInt(7, t.getIdEquipo());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) t.setId(rs.getInt(1));
            System.out.println("✅ Tarea creada: " + t.getTitulo());
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al crear tarea: " + e.getMessage());
            return false;
        }
    }

    // ── SELECT ALL ────────────────────────────────────────────────────────────
    public List<Task> listarTodas() {
        List<Task> lista = new ArrayList<>();
        String sql = "SELECT * FROM task ORDER BY prioridad, fecha_limite";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar tareas: " + e.getMessage());
        }
        return lista;
    }

    // ── SELECT BY EQUIPO ──────────────────────────────────────────────────────
    public List<Task> listarPorEquipo(int idEquipo) {
        List<Task> lista = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE id_equipo = ? ORDER BY prioridad";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar tareas por equipo: " + e.getMessage());
        }
        return lista;
    }

    // ── SELECT BY PRIORIDAD ───────────────────────────────────────────────────
    public List<Task> listarPorPrioridad(Prioridad prioridad) {
        List<Task> lista = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE prioridad = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, prioridad.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("⚠️ Error al filtrar por prioridad: " + e.getMessage());
        }
        return lista;
    }

    // ── SELECT BY ESTADO ──────────────────────────────────────────────────────
    public List<Task> listarPorEstado(EstadoTarea estado) {
        List<Task> lista = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE estado = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("⚠️ Error al filtrar por estado: " + e.getMessage());
        }
        return lista;
    }

    // ── SELECT BY ID ──────────────────────────────────────────────────────────
    public Task buscarPorId(int id) {
        String sql = "SELECT * FROM task WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("⚠️ Error al buscar tarea: " + e.getMessage());
        }
        return null;
    }

    // ── UPDATE ESTADO ─────────────────────────────────────────────────────────
    public boolean cambiarEstado(int idTask, EstadoTarea nuevoEstado) {
        String sql = "UPDATE task SET estado = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, idTask);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) System.out.println("✅ Estado actualizado a: " + nuevoEstado.getDescripcion());
            return ok;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }

    // ── UPDATE COMPLETO ───────────────────────────────────────────────────────
    public boolean actualizar(Task t) {
        String sql = "UPDATE task SET titulo=?, descripcion=?, prioridad=?, estado=?, fecha_limite=?, id_equipo=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getTitulo());
            ps.setString(2, t.getDescripcion());
            ps.setString(3, t.getPrioridad().name());
            ps.setString(4, t.getEstado().name());
            ps.setDate(5, t.getFechaLimite() != null ? Date.valueOf(t.getFechaLimite()) : null);
            ps.setInt(6, t.getIdEquipo());
            ps.setInt(7, t.getId());
            boolean ok = ps.executeUpdate() > 0;
            if (ok) System.out.println("✅ Tarea actualizada.");
            return ok;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al actualizar tarea: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean eliminar(int id) {
        String sql = "DELETE FROM task WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) System.out.println("🗑️  Tarea eliminada.");
            return ok;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al eliminar tarea: " + e.getMessage());
            return false;
        }
    }

    // ── Dashboard rápido ──────────────────────────────────────────────────────
    public void mostrarDashboard() {
        String sql = "SELECT estado, COUNT(*) as total FROM task GROUP BY estado";
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║      DASHBOARD DE TAREAS     ║");
        System.out.println("╠══════════════════════════════╣");
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int totalGlobal = 0;
            while (rs.next()) {
                String estado = rs.getString("estado");
                int total     = rs.getInt("total");
                totalGlobal  += total;
                EstadoTarea e = EstadoTarea.valueOf(estado);
                System.out.printf("║  %-20s : %3d  ║%n", e.getDescripcion(), total);
            }
            System.out.println("╠══════════════════════════════╣");
            System.out.printf("║  %-20s : %3d  ║%n", "TOTAL", totalGlobal);
            System.out.println("╚══════════════════════════════╝");
        } catch (SQLException e) {
            System.out.println("⚠️ Error al generar dashboard: " + e.getMessage());
        }
    }

    // ── Mapeo ResultSet → Task ────────────────────────────────────────────────
    private Task mapear(ResultSet rs) throws SQLException {
        Date fechaLimiteSQL = rs.getDate("fecha_limite");
        return new Task(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                Prioridad.valueOf(rs.getString("prioridad")),
                EstadoTarea.valueOf(rs.getString("estado")),
                rs.getDate("fecha_creacion").toLocalDate(),
                fechaLimiteSQL != null ? fechaLimiteSQL.toLocalDate() : null,
                rs.getInt("id_equipo")
        );
    }
}
