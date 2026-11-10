/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelo.Mecanico;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */

public class MecanicoDAO extends BaseDAO<Mecanico>{
    @Override
    protected String tabla() {
        return "Mecanicos";
    }

    @Override
    protected String columnaId() {
        return "IdMecanico";
    }

    @Override
    protected Mecanico mapear(ResultSet rs) throws SQLException {
        Mecanico m = new Mecanico();
        m.setId(rs.getInt("IdMecanico"));
        m.setIdentidad(rs.getString("Identidad"));
        m.setNombre(rs.getString("Nombre"));
        m.setApellido(rs.getString("Apellido"));
        m.setTelefono(rs.getString("Telefono"));
        m.setEspecialidad(rs.getString("Especialidad"));
        m.setActivo(rs.getBoolean("Activo"));
        return m;
    }

    @Override
    public int insertar(Mecanico m) throws SQLException {
        return insertarObteniendoId(
            "INSERT INTO Mecanicos (Identidad, Nombre, Apellido, Telefono, Especialidad) VALUES (?, ?, ?, ?, ?)",
            m.getIdentidad(), m.getNombre(), m.getApellido(), m.getTelefono(), m.getEspecialidad()
        );
    }

    @Override
    public boolean actualizar(Mecanico m) throws SQLException {
        return ejecutar(
            "UPDATE Mecanicos SET Identidad=?, Nombre=?, Apellido=?, Telefono=?, Especialidad=?, Activo=? WHERE IdMecanico=?",
            m.getIdentidad(), m.getNombre(), m.getApellido(), m.getTelefono(), m.getEspecialidad(), m.isActivo(), m.getId()
        ) > 0;
    }

    public List<Mecanico> listarActivos() throws SQLException {
        return consultar("SELECT * FROM Mecanicos WHERE Activo = 1 ORDER BY Apellido", this::mapear);
    }

    /** Órdenes en las que el mecánico participa actualmente (control de disponibilidad). */
    public int contarOrdenesActivas(int idMecanico) throws SQLException {
        Integer n = consultarUno(
            "SELECT COUNT(*) FROM OrdenMecanico om INNER JOIN OrdenesTrabajo o ON o.IdOrden = om.IdOrden " +
            "WHERE om.IdMecanico = ? AND o.Estado IN ('EN_DIAGNOSTICO', 'EN_REPARACION', 'EN_CONTROL_CALIDAD')",
            cs -> cs.getInt(1),
            idMecanico
        );
        return n == null ? 0 : n;
    }
}
