/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import config.Conexion;
import Modelo.EstadoOrden;
import Modelo.OrdenTrabajo;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

/**
 *
 * @author 50496
 */
public class OrdenDAO extends BaseDAO<OrdenTrabajo> {
    private static final String BASE = 
        "SELECT o.IdOrden, o.NumeroOrden, o.IdCliente, o.IdVehiculo, o.FechaIngreso, o.Kilometraje, o.Sintomas, " +
        "o.DiagnosticoPreliminar, o.Estado, c.Nombre + ' ' + c.Apellido AS Cliente, " +
        "v.Placa + ' - ' + v.Marca + ' ' + v.Modelo AS Vehiculo, " +
        "(SELECT TOP 1 m.Nombre + ' ' + m.Apellido FROM OrdenMecanico om " +
        "INNER JOIN Mecanicos m ON m.IdMecanico = om.IdMecanico " +
        "WHERE om.IdOrden = o.IdOrden AND om.EsResponsable = 1) AS Responsable " +
        "FROM OrdenesTrabajo o " +
        "INNER JOIN Clientes c ON c.IdCliente = o.IdCliente " +
        "INNER JOIN Vehiculos v ON v.IdVehiculo = o.IdVehiculo ";

    @Override
    protected String tabla() {
        return "OrdenesTrabajo";
    }

    @Override
    protected String columnaId() {
        return "IdOrden";
    }

    @Override
    protected OrdenTrabajo mapear(ResultSet rs) throws SQLException {
        OrdenTrabajo o = new OrdenTrabajo();
        o.setId(rs.getInt("IdOrden"));
        o.setNumeroOrden(rs.getString("NumeroOrden"));
        o.setIdCliente(rs.getInt("IdCliente"));
        o.setIdVehiculo(rs.getInt("IdVehiculo"));
        
        Timestamp t = rs.getTimestamp("FechaIngreso");
        o.setFechaIngreso(t == null ? null : t.toLocalDateTime());
        
        o.setKilometraje(rs.getInt("Kilometraje"));
        o.setSintomas(rs.getString("Sintomas"));
        o.setDiagnosticoPreliminar(rs.getString("DiagnosticoPreliminar"));
      //  o.setEstado(EstadoOrden.valueOf(rs.getString("Estado")));
        o.setEstado(EstadoOrden.valueOf(rs.getString("Estado").toUpperCase().replace(" ", "_")));
        
        o.setClienteNombre(rs.getString("Cliente"));
        o.setVehiculoDescripcion(rs.getString("Vehiculo"));
        o.setResponsable(rs.getString("Responsable"));
        return o;
    }

    @Override
    public OrdenTrabajo buscarPorId(int id) throws SQLException {
        return consultarUno(BASE + "WHERE o.IdOrden = ?", this::mapear, id);
    }

    @Override
    public List<OrdenTrabajo> listar() throws SQLException {
        return consultar(BASE + "ORDER BY o.IdOrden DESC", this::mapear);
    }

    //Ordenes que todavía no están cerradas
    public List<OrdenTrabajo> listarActivas() throws SQLException {
        return consultar(BASE + "WHERE o.Estado NOT IN ('FINALIZADO', 'RECHAZADA', 'CANCELADA') ORDER BY o.IdOrden DESC", this::mapear);
    }

    /** Crea la orden con el procedimiento almacenado sp_CrearOrden. */
    @Override
    public int insertar(OrdenTrabajo o) throws SQLException {
        try (Connection con = Conexion.obtener();
             CallableStatement cs = con.prepareCall("{call sp_CrearOrden(?, ?, ?, ?, ?, ?)}")) {
            cs.setInt(1, o.getIdCliente());
            cs.setInt(2, o.getIdVehiculo());
            cs.setInt(3, o.getKilometraje());
            cs.setString(4, o.getSintomas());
            cs.setString(5, o.getDiagnosticoPreliminar());
            cs.registerOutParameter(6, Types.INTEGER);
            cs.execute();
            return cs.getInt(6);
        }
    }

    @Override
    public boolean actualizar(OrdenTrabajo o) throws SQLException {
        return ejecutar(
            "UPDATE OrdenesTrabajo SET Sintomas = ?, DiagnosticoPreliminar = ? WHERE IdOrden = ? " +
            "AND Estado NOT IN ('FINALIZADA', 'RECHAZADA', 'CANCELADA')",
            o.getSintomas(), o.getDiagnosticoPreliminar(), o.getId()
        ) > 0;
    }

    /** Para una orden, "desactivar" significa cancelarla (solo si no está cerrada). */
    @Override
    public boolean desactivar(int id) throws SQLException {
        return ejecutar(
            "UPDATE OrdenesTrabajo SET Estado = 'CANCELADA' WHERE IdOrden = ? " +
            "AND Estado NOT IN ('FINALIZADA', 'RECHAZADA', 'CANCELADA')", 
            id
        ) > 0;
    }

    public boolean tieneResponsable(int idOrden) throws SQLException {
        Integer n = consultarUno(
            "SELECT COUNT(*) FROM OrdenMecanico WHERE IdOrden = ? AND EsResponsable = 1",
            cs -> cs.getInt(1), idOrden
        );
        return n != null && n > 0;
    }
}
