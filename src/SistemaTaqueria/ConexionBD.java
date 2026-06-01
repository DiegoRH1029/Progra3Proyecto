package SistemaTaqueria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    // Configuración de los datos de tu servidor local
    private static final String URL = "jdbc:mysql://localhost:3306/taqueriadb";
    private static final String USER = "root"; // Usuario por defecto de MySQL
    private static final String PASSWORD = "1234"; // Pon la contraseña de tu Workbench

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            // Cargar el driver en memoria
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa con la base de datos de la Taquería!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el Driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error de conexión a MySQL: " + e.getMessage());
            e.printStackTrace();
        }
        return conexion;
    }
}