package com.gestortareas.crud;

import com.gestortareas.models.Team;
import com.gestortareas.persistence.ConexionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// CRUD de la tabla team — PreparedStatement (5.17.5.1)
public class TeamCRUD {

    private final Connection con = ConexionMySQL.getInstancia().getConexion();

    // ── INSERT ────────────────────────────────────────────────────────────────
    public boolean crear(Team t) {
        String sql = "INSERT INTO team (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDescripcion());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) t.setId(rs.getInt(1));
            System.out.println("✅ Equipo creado: " + t.getNombre());
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al crear equipo: " + e.getMessage());
            return false;
        }
    }

    // ── SELECT ALL ────────────────────────────────────────────────────────────
    public List<Team> listarTodos() {
        List<Team> lista = new ArrayList<>();
        String sql = "SELECT * FROM team";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Team(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar equipos: " + e.getMessage());
        }
        return lista;
    }

    // ── SELECT BY ID ──────────────────────────────────────────────────────────
    public Team buscarPorId(int id) {
        String sql = "SELECT * FROM team WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Team(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al buscar equipo: " + e.getMessage());
        }
        return null;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public boolean actualizar(Team t) {
        String sql = "UPDATE team SET nombre = ?, descripcion = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDescripcion());
            ps.setInt(3, t.getId());
            boolean ok = ps.executeUpdate() > 0;
            if (ok) System.out.println("✅ Equipo actualizado.");
            return ok;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al actualizar equipo: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean eliminar(int id) {
        String sql = "DELETE FROM team WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) System.out.println("🗑️  Equipo eliminado.");
            return ok;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al eliminar equipo: " + e.getMessage());
            return false;
        }
    }
}
