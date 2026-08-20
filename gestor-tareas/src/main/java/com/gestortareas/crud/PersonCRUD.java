package com.gestortareas.crud;

import com.gestortareas.models.Person;
import com.gestortareas.models.TipoPersona;
import com.gestortareas.persistence.ConexionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// CRUD de la tabla person — PreparedStatement (5.17.5.1)
public class PersonCRUD {

    private final Connection con = ConexionMySQL.getInstancia().getConexion();

    // ── INSERT ────────────────────────────────────────────────────────────────
    public boolean crear(Person p) {
        String sql = "INSERT INTO person (nombre, email, tipo_persona, id_equipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getEmail());
            ps.setString(3, p.getTipo().name());
            ps.setInt(4, p.getIdEquipo());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) p.setId(rs.getInt(1));
            System.out.println("✅ Persona creada: " + p.getNombre());
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al crear persona: " + e.getMessage());
            return false;
        }
    }

    // ── SELECT ALL ────────────────────────────────────────────────────────────
    public List<Person> listarTodos() {
        List<Person> lista = new ArrayList<>();
        String sql = "SELECT * FROM person";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar personas: " + e.getMessage());
        }
        return lista;
    }

    // ── SELECT BY EQUIPO ──────────────────────────────────────────────────────
    public List<Person> listarPorEquipo(int idEquipo) {
        List<Person> lista = new ArrayList<>();
        String sql = "SELECT * FROM person WHERE id_equipo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar personas por equipo: " + e.getMessage());
        }
        return lista;
    }

    // ── SELECT BY ID ──────────────────────────────────────────────────────────
    public Person buscarPorId(int id) {
        String sql = "SELECT * FROM person WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("⚠️ Error al buscar persona: " + e.getMessage());
        }
        return null;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public boolean actualizar(Person p) {
        String sql = "UPDATE person SET nombre = ?, email = ?, tipo_persona = ?, id_equipo = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getEmail());
            ps.setString(3, p.getTipo().name());
            ps.setInt(4, p.getIdEquipo());
            ps.setInt(5, p.getId());
            boolean ok = ps.executeUpdate() > 0;
            if (ok) System.out.println("✅ Persona actualizada.");
            return ok;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al actualizar persona: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean eliminar(int id) {
        String sql = "DELETE FROM person WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) System.out.println("🗑️  Persona eliminada.");
            return ok;
        } catch (SQLException e) {
            System.out.println("⚠️ Error al eliminar persona: " + e.getMessage());
            return false;
        }
    }

    // ── Mapeo ResultSet → Person ──────────────────────────────────────────────
    private Person mapear(ResultSet rs) throws SQLException {
        return new Person(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                TipoPersona.valueOf(rs.getString("tipo_persona")),
                rs.getInt("id_equipo")
        );
    }
}
