/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelo.Cliente;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */
public class ClienteDAO extends BaseDAO<Cliente>{
    @Override
    protected String tabla() {
        return "Clientes";
    }

    @Override
    protected String columnaId() {
        return "IdCliente";
    }

    @Override
    protected Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("IdCliente"));
        c.setIdentidad(rs.getString("Identidad"));
        c.setNombre(rs.getString("Nombre"));
        c.setApellido(rs.getString("Apellido"));
        c.setTelefono(rs.getString("Telefono"));
        c.setCorreo(rs.getString("Correo"));
        c.setDireccion(rs.getString("Direccion"));
        c.setActivo(rs.getBoolean("Activo"));
        return c;
    }

    @Override
    public int insertar(Cliente c) throws SQLException {
        return insertarObteniendoId(
            "INSERT INTO Clientes (Identidad, Nombre, Apellido, Telefono, Correo, Direccion) VALUES (?, ?, ?, ?, ?, ?)",
            c.getIdentidad(), c.getNombre(), c.getApellido(), c.getTelefono(), c.getCorreo(), c.getDireccion()
        );
    }

    @Override
    public boolean actualizar(Cliente c) throws SQLException {
        return ejecutar(
            "UPDATE Clientes SET Identidad=?, Nombre=?, Apellido=?, Telefono=?, Correo=?, Direccion=?, Activo=? WHERE IdCliente=?",
            c.getIdentidad(), c.getNombre(), c.getApellido(), c.getTelefono(), c.getCorreo(), c.getDireccion(), c.isActivo(), c.getId()
        ) > 0;
    }

    public List<Cliente> buscar(String texto) throws SQLException {
        String t = "%" + texto.trim() + "%";
        return consultar(
            "SELECT * FROM Clientes WHERE Nombre LIKE ? OR Apellido LIKE ? OR Identidad LIKE ? ORDER BY Apellido",
            this::mapear,
            t, t, t
        );
    }

    public List<Cliente> listarActivos() throws SQLException {
        return consultar("SELECT * FROM Clientes WHERE Activo = 1 ORDER BY Apellido, Nombre", this::mapear);
    }
}
