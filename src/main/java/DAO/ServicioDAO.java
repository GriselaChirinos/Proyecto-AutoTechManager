/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelo.Servicio;
import Modelo.ServicioFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */
public class ServicioDAO extends BaseDAO<Servicio>{
    @Override
    protected String tabla() {
        return "Servicios";
    }

    @Override
    protected String columnaId() {
        return "IdServicio";
    }

    @Override
    protected Servicio mapear(ResultSet rs) throws SQLException {
        Servicio s = ServicioFactory.crear(rs.getInt("Nivel")); // polimorfismo
        s.setId(rs.getInt("IdServicio"));
        s.setCodigo(rs.getString("Codigo"));
        s.setNombre(rs.getString("Nombre"));
        s.setDescripcion(rs.getString("Descripcion"));
        s.setCostoManoObra(rs.getBigDecimal("CostoManoObra"));
        s.setTiempoEstimadoHoras(rs.getDouble("TiempoEstimadoHoras"));
        s.setActivo(rs.getBoolean("Activo"));
        return s;
    }

    @Override
    public int insertar(Servicio s) throws SQLException {
        return insertarObteniendoId(
            "INSERT INTO Servicios (Codigo, Nombre, Descripcion, Nivel, CostoManoObra, TiempoEstimadoHoras) VALUES (?, ?, ?, ?, ?, ?)",
            s.getCodigo(), s.getNombre(), s.getDescripcion(), s.getNivel(), s.getCostoManoObra(), s.getTiempoEstimadoHoras()
        );
    }

    @Override
    public boolean actualizar(Servicio s) throws SQLException {
        return ejecutar(
            "UPDATE Servicios SET Codigo=?, Nombre=?, Descripcion=?, Nivel=?, CostoManoObra=?, TiempoEstimadoHoras=?, Activo=? WHERE IdServicio=?",
            s.getCodigo(), s.getNombre(), s.getDescripcion(), s.getNivel(), s.getCostoManoObra(), s.getTiempoEstimadoHoras(), s.isActivo(), s.getId()
        ) > 0;
    }

    public List<Servicio> listarActivos() throws SQLException {
        return consultar("SELECT * FROM Servicios WHERE Activo = 1 ORDER BY Nivel, Nombre", this::mapear);
    }

    public void asociarRepuesto(int idServicio, int idRepuesto, int cantidad) throws SQLException {
        ejecutar(
            "MERGE ServicioRepuesto AS t USING (SELECT ? AS IdServicio, ? AS IdRepuesto) AS s " +
            "ON t.IdServicio = s.IdServicio AND t.IdRepuesto = s.IdRepuesto " +
            "WHEN MATCHED THEN UPDATE SET Cantidad = ? " +
            "WHEN NOT MATCHED THEN INSERT (IdServicio, IdRepuesto, Cantidad) VALUES (s.IdServicio, s.IdRepuesto, ?);",
            idServicio, idRepuesto, cantidad, cantidad
        );
    }

    public void quitarRepuesto(int idServicio, int idRepuesto) throws SQLException {
        ejecutar("DELETE FROM ServicioRepuesto WHERE IdServicio = ? AND IdRepuesto = ?", idServicio, idRepuesto);
    }

    /** Filas: IdRepuesto, Código, Repuesto, Cantidad, Precio de venta. */
    public List<Object[]> listarRepuestos(int idServicio) throws SQLException {
        return consultar(
            "SELECT r.IdRepuesto, r.Codigo, r.Nombre, sr.Cantidad, r.PrecioVenta " +
            "FROM ServicioRepuesto sr INNER JOIN Repuestos r ON r.IdRepuesto = sr.IdRepuesto " +
            "WHERE sr.IdServicio = ? ORDER BY r.Nombre",
            cs -> new Object[]{cs.getInt(1), cs.getString(2), cs.getString(3), cs.getInt(4), cs.getBigDecimal(5)},
            idServicio
        );
    }
}
