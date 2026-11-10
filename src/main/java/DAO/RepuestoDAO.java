/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelo.Proveedor;
import Modelo.Repuesto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */
public class RepuestoDAO extends BaseDAO<Repuesto> {
    @Override
    protected String tabla() {
        return "Repuestos";
    }

    @Override
    protected String columnaId() {
        return "IdRepuesto";
    }

    @Override
    protected Repuesto mapear(ResultSet rs) throws SQLException {
        Repuesto r = new Repuesto();
        r.setId(rs.getInt("IdRepuesto"));
        r.setCodigo(rs.getString("Codigo"));
        r.setNombre(rs.getString("Nombre"));
        r.setCategoria(rs.getString("Categoria"));
        r.setIdProveedor(rs.getInt("IdProveedor"));
        r.setCostoUnitario(rs.getBigDecimal("CostoUnitario"));
        r.setPrecioVenta(rs.getBigDecimal("PrecioVenta"));
        r.setStock(rs.getInt("Stock"));
        r.setStockMinimo(rs.getInt("StockMinimo"));
        r.setActivo(rs.getBoolean("Activo"));
        return r;
    }

    /** El stock inicia en 0: solo cambia mediante movimientos de inventario. */
    @Override
    public int insertar(Repuesto r) throws SQLException {
        return insertarObteniendoId(
            "INSERT INTO Repuestos (Codigo, Nombre, Categoria, IdProveedor, CostoUnitario, PrecioVenta, StockMinimo) VALUES (?, ?, ?, ?, ?, ?, ?)",
            r.getCodigo(), r.getNombre(), r.getCategoria(), r.getIdProveedor(), r.getCostoUnitario(), r.getPrecioVenta(), r.getStockMinimo()
        );
    }

    @Override
    public boolean actualizar(Repuesto r) throws SQLException {
        return ejecutar(
            "UPDATE Repuestos SET Codigo=?, Nombre=?, Categoria=?, IdProveedor=?, CostoUnitario=?, PrecioVenta=?, StockMinimo=?, Activo=? WHERE IdRepuesto=?",
            r.getCodigo(), r.getNombre(), r.getCategoria(), r.getIdProveedor(), r.getCostoUnitario(), r.getPrecioVenta(), r.getStockMinimo(), r.isActivo(), r.getId()
        ) > 0;
    }

    public List<Repuesto> listarActivos() throws SQLException {
        return consultar("SELECT * FROM Repuestos WHERE Activo = 1 ORDER BY Categoria, Nombre", this::mapear);
    }

    public List<Repuesto> listarCriticos() throws SQLException {
        return consultar("SELECT * FROM Repuestos WHERE Activo = 1 AND Stock <= StockMinimo ORDER BY Stock", this::mapear);
    }

    public List<Proveedor> listarProveedores() throws SQLException {
        return consultar(
            "SELECT IdProveedor, Nombre, Telefono, Correo FROM Proveedores WHERE Activo = 1 ORDER BY Nombre",
            cs -> {
                Proveedor p = new Proveedor();
                p.setId(cs.getInt(1));
                p.setNombre(cs.getString(2));
                p.setTelefono(cs.getString(3));
                p.setCorreo(cs.getString(4));
                return p;
            }
        );
    }
}
