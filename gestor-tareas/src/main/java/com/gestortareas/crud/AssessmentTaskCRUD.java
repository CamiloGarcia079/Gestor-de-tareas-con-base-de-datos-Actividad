package com.gestortareas.crud;

import com.gestortareas.models.AssessmentTask;
import com.gestortareas.persistence.ConexionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// CRUD de la tabla assement_task — asignación tarea ↔ persona
public class AssessmentTaskCRUD {

    private final Connection con = ConexionMySQL.getInstancia().getConexion();

    // ── INSERT ────────────────────────────────────────────────────────────────
    public boolean asignar(AssessmentTask at) {
        String sql = "INSERT INTO assement_task (id_task, id_person, nota) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, at.getIdTask());
            ps.setInt(2, at.getIdPerson());
            ps.setString(3, at.getNota());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) at.setId(rs.getInt(1));
            System.out.println("✅ Tarea asignada a persona correctamente.");
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al asignar tarea: " + e.getMessage());
            return false;
        }
    }

    // ── SELECT BY TAREA ───────────────────────────────────────────────────────
    public List<AssessmentTask> listarPorTarea(int idTask) {
        List<AssessmentTask> lista = new ArrayList<>();
        String sql = "SELECT at.*, p.nombre AS nombre_persona " +
                     "FROM assement_task at JOIN person p ON at.id_person = p.id " +
                     "WHERE at.id_task = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTask);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AssessmentTask a = new AssessmentTask(
                        rs.getInt("id"), rs.getInt("id_task"),
                        rs.getInt("id_person"), rs.getString("nota"));
                lista.add(a);
                System.out.println("  → " + rs.getString("nombre_persona") + " | " + a.getNota());
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar asignaciones: " + e.getMessage());
        }
        return lista;
    }

    // ── SELECT BY PERSONA ─────────────────────────────────────────────────────
    public List<AssessmentTask> listarPorPersona(int idPerson) {
        List<AssessmentTask> lista = new ArrayList<>();
        String sql = "SELECT at.*, t.titulo FROM assement_task at " +
                     "JOIN task t ON at.id_task = t.id WHERE at.id_person = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPerson);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AssessmentTask a = new AssessmentTask(
                        rs.getInt("id"), rs.getInt("id_task"),
                        rs.getInt("id_person"), rs.getString("nota"));
                lista.add(a);
                System.out.println("  → Tarea: " + rs.getString("titulo") + " | " + a.getNota());
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar asignaciones: " + e.getMessage());
        }
        return lista;
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean eliminar(int id) {
        String sql = "DELETE FROM assement_task WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) System.out.println("🗑️  Asignación eliminada.");
            return ok;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al eliminar asignación: " + e.getMessage());
            return false;
        }
    }
}
