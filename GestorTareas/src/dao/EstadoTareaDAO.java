package dao;

import conexion.ConexionBD;
import modelo.EstadoTarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Solo lectura: status_task es un catalogo que ya viene poblado por db/schema.sql
public class EstadoTareaDAO {

    public List<EstadoTarea> listar() throws SQLException {
        List<EstadoTarea> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM status_task ORDER BY id";
        try (Statement st = ConexionBD.getInstancia().getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new EstadoTarea(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
            }
        }
        return lista;
    }
}
