package dao;

import conexion.ConexionBD;
import modelo.Equipo;
import modelo.Persona;
import modelo.TipoPersona;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    public void insertar(Persona p) throws SQLException {
        Connection con = ConexionBD.getInstancia().getConexion();
        String sql = "INSERT INTO person (nombre, email, id_tipo_persona, id_equipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getEmail());
            ps.setInt(3, p.getTipo().getId());
            if (p.getEquipo() != null) {
                ps.setInt(4, p.getEquipo().getId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.executeUpdate();

            // registramos el alta en el historico team_person (relacion N:M persona-equipo)
            if (p.getEquipo() != null) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int idPersona = keys.getInt(1);
                        String sqlHist = "INSERT INTO team_person (id_team, id_person, fecha_alta) VALUES (?, ?, ?)";
                        try (PreparedStatement psh = con.prepareStatement(sqlHist)) {
                            psh.setInt(1, p.getEquipo().getId());
                            psh.setInt(2, idPersona);
                            psh.setDate(3, Date.valueOf(LocalDate.now()));
                            psh.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public List<Persona> listar() throws SQLException {
        List<Persona> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.email, " +
                "tp.id AS tipo_id, tp.nombre AS tipo_nombre, tp.descripcion AS tipo_desc, " +
                "e.id AS equipo_id, e.nombre AS equipo_nombre, e.descripcion AS equipo_desc " +
                "FROM person p " +
                "JOIN type_person tp ON p.id_tipo_persona = tp.id " +
                "LEFT JOIN team e ON p.id_equipo = e.id " +
                "ORDER BY p.id";
        try (Statement st = ConexionBD.getInstancia().getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                TipoPersona tipo = new TipoPersona(rs.getInt("tipo_id"), rs.getString("tipo_nombre"), rs.getString("tipo_desc"));
                Equipo equipo = null;
                if (rs.getObject("equipo_id") != null) {
                    equipo = new Equipo(rs.getInt("equipo_id"), rs.getString("equipo_nombre"), rs.getString("equipo_desc"));
                }
                lista.add(new Persona(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"), tipo, equipo));
            }
        }
        return lista;
    }
}
