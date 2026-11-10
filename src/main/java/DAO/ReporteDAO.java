/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import config.Conexion;
import Modelo.ResultadoTabla;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 50496
 */
public class ReporteDAO {
    
    
    /**
     * Ejecuta cualquier SELECT parametrizado y devuelve columnas + filas.
     * @param sql La sentencia SQL a ejecutar.
     * @param p Los parámetros opcionales para el PreparedStatement.
     * @return Un objeto ResultadoTabla con las columnas y filas obtenidas.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    
    /** Ejecuta cualquier SELECT parametrizado y devuelve columnas + filas. */
    public ResultadoTabla ejecutar(String sql, Object... p) throws SQLException {
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < p.length; i++) {
                ps.setObject(i + 1, p[i]);
            }
            try (ResultSet cs = ps.executeQuery()) {
                ResultSetMetaData md = cs.getMetaData();
                int n = md.getColumnCount();
                String[] columnas = new String[n];
                for (int i = 0; i < n; i++) {
                    columnas[i] = md.getColumnLabel(i + 1);
                }
                List<Object[]> filas = new ArrayList<>();
                while (cs.next()) {
                    Object[] fila = new Object[n];
                    for (int i = 0; i < n; i++) {
                        fila[i] = cs.getObject(i + 1);
                    }
                    filas.add(fila);
                }
                return new ResultadoTabla(columnas, filas);
            }
        }
    }

    /** Historial de un vehículo: fechas, servicios, mecánicos, repuestos y costos (vista). */
    public ResultadoTabla historialVehiculo(int idVehiculo) throws SQLException {
        return ejecutar(
            "SELECT CONVERT(VARCHAR(16), FechaIngreso, 120) AS Fecha, NumeroOrden AS [N. Orden], Servicios, Mecanicos, " +
            "ListaRepuestos AS Repuestos, ManoObra AS [Mano de obra], CostoRepuestos AS [Costo repuestos], " +
            "(ManoObra + CostoRepuestos) AS Total " +
            "FROM vw_HistorialVehiculo WHERE IdVehiculo = ? ORDER BY FechaIngreso DESC",
            idVehiculo
        );
    }

    public ResultadoTabla stockCritico() throws SQLException {
        return ejecutar("SELECT Codigo, Nombre AS Repuesto, Stock, StockMinimo AS Minimo FROM vw_StockCritico ORDER BY Faltante DESC");
    }

    public ResultadoTabla saldosOrdenes() throws SQLException {
        return ejecutar("SELECT NumeroOrden AS [N. Orden], Cliente, TotalPresupuesto AS Total, Pagado, Saldo FROM vw_SaldoOrdenes ORDER BY Saldo DESC");
    }

    public ResultadoTabla productividadMecanicos() throws SQLException {
        return ejecutar("SELECT Mecanico, Especialidad, OrdenesAsignadas AS Ordenes, HorasTrabajadas AS Horas FROM vw_ProductividadMecanicos ORDER BY Horas DESC");
    }

    /** LEFT JOIN + GROUP BY: incluye clientes sin vehículos. */
    public ResultadoTabla clientesConVehiculos() throws SQLException {
        return ejecutar(
            "SELECT c.Identidad, c.Nombre + ' ' + c.Apellido AS Cliente, COUNT(v.IdVehiculo) AS Vehiculos " +
            "FROM Clientes c LEFT JOIN Vehiculos v ON v.IdCliente = c.IdCliente " +
            "GROUP BY c.Identidad, c.Nombre, c.Apellido ORDER BY Vehiculos DESC"
        );
    }

    /** INNER JOIN + GROUP BY + agregaciones. */
    public ResultadoTabla serviciosMasSolicitados() throws SQLException {
        return ejecutar(
            "SELECT s.Codigo, s.Nombre AS Servicio, COUNT(d.IdDetalle) AS Veces, " +
            "SUM(d.PrecioManoObra) AS Ingreso, AVG(d.PrecioManoObra) AS Promedio " +
            "FROM Servicios s INNER JOIN DetalleOrden d ON d.IdServicio = s.IdServicio " +
            "GROUP BY s.Codigo, s.Nombre ORDER BY Veces DESC"
        );
    }

    /** Subconsulta NOT EXISTS: repuestos que nunca han salido del inventario. */
    public ResultadoTabla repuestosSinSalidas() throws SQLException {
        return ejecutar(
            "SELECT r.Codigo, r.Nombre AS Repuesto, r.Stock FROM Repuestos r " +
            "WHERE NOT EXISTS (SELECT 1 FROM MovimientosInventario m " +
            "WHERE m.IdRepuesto = r.IdRepuesto AND m.TipoMovimiento = 'SALIDA')"
        );
    }

    /** Subconsulta escalar: repuestos con stock menor al promedio. */
    public ResultadoTabla repuestosBajoPromedio() throws SQLException {
        return ejecutar(
            "SELECT Codigo, Nombre AS Repuesto, Stock FROM Repuestos " +
            "WHERE Stock < (SELECT AVG(CAST(Stock AS DECIMAL(10,2))) FROM Repuestos)"
        );
    }

    public ResultadoTabla auditoriaOrdenes() throws SQLException {
        return ejecutar(
            "SELECT a.IdOrden, o.NumeroOrden AS [N. Orden], a.EstadoAnterior AS Anterior, a.EstadoNuevo AS Nuevo, " +
            "a.Usuario, CONVERT(VARCHAR(19), a.Fecha, 120) AS Fecha " +
            "FROM AuditoriaOrdenes a INNER JOIN OrdenesTrabajo o ON o.IdOrden = a.IdOrden ORDER BY a.IdAuditoria DESC"
        );
    }
    
}
