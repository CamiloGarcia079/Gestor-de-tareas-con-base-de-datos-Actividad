package com.gestortareas.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexión Singleton a MySQL con Double-Checked Locking.
 * Patrón Singleton (5.15.4.5) aplicado a la capa de persistencia.
 * Una sola instancia de conexión durante toda la vida del programa.
 */
public class ConexionMySQL {

    // ── Parámetros — ajustar según tu entorno ─────────────────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/gestor_tareas?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
private static final String USER     = "root";
private static final String PASSWORD = "camper123";
    // ──────────────────────────────────────────────────────────────────────────

    private static volatile ConexionMySQL instancia;
    private Connection conexion;

    // Constructor privado: carga el driver y abre la conexión
    private ConexionMySQL() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión establecida con MySQL — gestor_tareas");
        } catch (ClassNotFoundException e) {
            System.out.println("⚠️ Driver JDBC no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("⚠️ Error al conectar con MySQL: " + e.getMessage());
        }
    }

    // Double-Checked Locking (5.15.4.5)
    public static ConexionMySQL getInstancia() {
        if (instancia == null) {
            synchronized (ConexionMySQL.class) {
                if (instancia == null) {
                    instancia = new ConexionMySQL();
                }
            }
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    // Cierra la conexión al salir
    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al cerrar conexión: " + e.getMessage());
        }
    }
}
