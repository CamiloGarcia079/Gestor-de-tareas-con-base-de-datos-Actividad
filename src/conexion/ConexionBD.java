package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Maneja la conexion a MySQL usando el patron Singleton:
 * en toda la aplicacion solo existe UNA instancia de conexion,
 * y cualquier DAO que la necesite la pide con getInstancia().
 *
 * IMPORTANTE: si cambias de computador (casa/campus), solo
 * hay que ajustar estas 4 constantes, el resto del programa
 * no se toca.
 */
public class ConexionBD {

    // ==== Datos de conexion: AJUSTAR SEGUN EL EQUIPO DONDE SE EJECUTE ====
    private static final String HOST = "localhost";
    private static final String PUERTO = "3307";           // contenedor docker-compose del campus
    private static final String BASE_DATOS = "gestor_tareas";
    private static final String USUARIO = "campus";
    private static final String CLAVE = "campus123";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static ConexionBD instancia;
    private Connection conexion;

    // Constructor privado: nadie puede hacer "new ConexionBD()" desde afuera
    private ConexionBD() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver de MySQL (mysql-connector-j) en el classpath.", e);
        }
    }

    /** Punto unico de acceso a la conexion (patron Singleton). */
    public static ConexionBD getInstancia() throws SQLException {
        if (instancia == null || instancia.getConexion().isClosed()) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }
}
