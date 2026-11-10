/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author 50496
 */
public class Conexion { 
    
    private static final String URL = "jdbc:sqlserver://CHIRINOS98:1433;"
            + "databaseName=AutoTechManager;"
            + "encrypt=true;"
            + "trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "1998";

    private static Connection conexion = null;

    private Conexion() {
    }

    public static Connection obtener() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                //establecer coneccion
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("->conexion exitosa a sql");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error no se encontro el drive de mssql-jdbc");
        } catch (SQLException e) {
            System.err.println("Error al conectar con SQL" + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("->La conexion con SQL fue cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexion" + e.getMessage());
        }
    }
}
