/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelo.Motor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */
public class MotorDAO extends BaseDAO<Motor> {
    
    private static final String BASE = 
        "SELECT m.IdMotor, m.IdRepuesto, m.NumeroSerie, m.Estado, r.Nombre AS Descripcion " +
        "FROM Motores m INNER JOIN Repuestos r ON r.IdRepuesto = m.IdRepuesto ";

    @Override
    protected String tabla() {
        return "Motores";
    }

    @Override
    protected String columnaId() {
        return "IdMotor";
    }

    @Override
    protected Motor mapear(ResultSet rs) throws SQLException {
        Motor m = new Motor();
        m.setId(rs.getInt("IdMotor"));
        m.setIdRepuesto(rs.getInt("IdRepuesto"));
        m.setNumeroSerie(rs.getString("NumeroSerie"));
        m.setEstado(rs.getString("Estado"));
        m.setDescripcion(rs.getString("Descripcion"));
        return m;
    }

    @Override
    public Motor buscarPorId(int id) throws SQLException {
        return consultarUno(BASE + "WHERE m.IdMotor = ?", this::mapear, id);
    }

    @Override
    public List<Motor> listar() throws SQLException {
        return consultar(BASE + "ORDER BY m.IdMotor DESC", this::mapear);
    }

    /** Los motores se crean con la entrada de inventario (InventarioService). */
    @Override
    public int insertar(Motor m) throws SQLException {
        return insertarObteniendoId(
            "INSERT INTO Motores (IdRepuesto, NumeroSerie) VALUES (?, ?)",
            m.getIdRepuesto(), m.getNumeroSerie()
        );
    }

    @Override
    public boolean actualizar(Motor m) throws SQLException {
        return ejecutar(
            "UPDATE Motores SET Estado = ? WHERE IdMotor = ?",
            m.getEstado(), m.getId()
        ) > 0;
    }

    @Override
    public boolean desactivar(int id) throws SQLException {
        return ejecutar("UPDATE Motores SET Estado = 'DESCARTADO' WHERE IdMotor = ? AND Estado = 'DISPONIBLE'", id) > 0;
    }

    public List<Motor> listarDisponibles() throws SQLException {
        return consultar(BASE + "WHERE m.Estado = 'DISPONIBLE' ORDER BY r.Nombre", this::mapear);
    }
}
