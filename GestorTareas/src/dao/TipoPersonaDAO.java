package dao;

import conexion.ConexionBD;
import modelo.TipoPersona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Solo lectura: type_person es un catalogo que ya viene poblado por db/schema.sql
public class TipoPersonaDAO {

    public List<TipoPersona> listar() throws SQLException {
        List<TipoPersona> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM type_person ORDER BY id";
        try (Statement st = ConexionBD.getInstancia().getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new TipoPersona(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
            }
        }
        return lista;
    }
}
