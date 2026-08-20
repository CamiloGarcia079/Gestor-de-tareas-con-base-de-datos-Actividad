package dao;

import conexion.ConexionBD;
import modelo.Equipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {

    public void insertar(Equipo e) throws SQLException {
        String sql = "INSERT INTO team (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.executeUpdate();
        }
    }

    public List<Equipo> listar() throws SQLException {
        List<Equipo> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM team ORDER BY id";
        try (Statement st = ConexionBD.getInstancia().getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Equipo(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
            }
        }
        return lista;
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM team WHERE id = ?";
        try (PreparedStatement ps = ConexionBD.getInstancia().getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
