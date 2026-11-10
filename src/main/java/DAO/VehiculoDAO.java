/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelo.Vehiculo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */
public class VehiculoDAO extends BaseDAO<Vehiculo> {
    @Override
    protected String tabla() {
        return "Vehiculos";
    }

    @Override
    protected String columnaId() {
        return "IdVehiculo";
    }

    @Override
    protected Vehiculo mapear(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo();
        v.setId(rs.getInt("IdVehiculo"));
        v.setIdCliente(rs.getInt("IdCliente"));
        v.setPlaca(rs.getString("Placa"));
        v.setVin(rs.getString("VIN"));
        v.setMarca(rs.getString("Marca"));
        v.setModelo(rs.getString("Modelo"));
        v.setAño(rs.getInt("Anio"));
        v.setColor(rs.getString("Color"));
        v.setKilometraje(rs.getInt("Kilometraje"));
        v.setCombustible(rs.getString("Combustible"));
        v.setSerieMotor(rs.getString("SerieMotor"));
        v.setActivo(rs.getBoolean("Activo"));
        return v;
    }

    @Override
    public int insertar(Vehiculo v) throws SQLException {
        return insertarObteniendoId(
            "INSERT INTO Vehiculos (IdCliente, Placa, VIN, Marca, Modelo, Anio, Color, Kilometraje, Combustible) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
            v.getIdCliente(),
            v.getPlaca() != null ? v.getPlaca().toUpperCase() : null,
            v.getVin() != null ? v.getVin().toUpperCase() : null,
            v.getMarca(),
            v.getModelo(),
            v.getAño(),
            v.getColor(),
            v.getKilometraje(),
            v.getCombustible()
        );
    }

    @Override
    public boolean actualizar(Vehiculo v) throws SQLException {
        return ejecutar(
            "UPDATE Vehiculos SET Placa=?, VIN=?, Marca=?, Modelo=?, Anio=?, Color=?, Kilometraje=?, Combustible=? WHERE IdVehiculo=?",
            v.getPlaca() != null ? v.getPlaca().toUpperCase() : null,
            v.getVin() != null ? v.getVin().toUpperCase() : null,
            v.getMarca(),
            v.getModelo(),
            v.getAño(),
            v.getColor(),
            v.getKilometraje(),
            v.getCombustible(),
            v.getId()
        ) > 0;
    }

    public List<Vehiculo> listarPorCliente(int idCliente) throws SQLException {
        return consultar(
            "SELECT * FROM Vehiculos WHERE IdCliente = ? AND Activo = 1 ORDER BY Placa",
            this::mapear,
            idCliente
        );
    }
}
