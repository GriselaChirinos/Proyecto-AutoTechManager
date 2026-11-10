/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.RepuestoDAO;
import exception.AutoTechException;
import exception.InventarioException;
import exception.NegocioException;
import exception.ValidacionException;
import Modelo.Proveedor;
import Modelo.Repuesto;
import util.ErrorSql;
import util.Transaccion;
import util.Validador;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */
public class InventarioService {
    private static final int REINTENTOS = 3;
    private final RepuestoDAO dao = new RepuestoDAO();

    public int guardar(Repuesto r) throws AutoTechException {
        Validador.requerido(r.getCodigo(), "Código");
        Validador.requerido(r.getNombre(), "Nombre");
        Validador.requerido(r.getCategoria(), "Categoría");
        if (r.getIdProveedor() <= 0) throw new ValidacionException("Seleccione un proveedor.");
        if (r.getCostoUnitario() == null || r.getCostoUnitario().signum() < 0 ||
            r.getPrecioVenta() == null || r.getPrecioVenta().signum() < 0) {
            throw new ValidacionException("Costo y precio no pueden ser negativos.");
        }
        if (r.getStockMinimo() < 0) throw new ValidacionException("El stock mínimo no puede ser negativo.");
        try {
            if (r.getId() == 0) return dao.insertar(r);
            if (!dao.actualizar(r)) throw new NegocioException("El repuesto ya no existe.");
            return r.getId();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Repuesto> listar() throws AutoTechException {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Repuesto> listarActivos() throws AutoTechException {
        try {
            return dao.listarActivos();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Repuesto> listarCriticos() throws AutoTechException {
        try {
            return dao.listarCriticos();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Proveedor> listarProveedores() throws AutoTechException {
        try {
            return dao.listarProveedores();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Entrada de inventario: actualiza stock + movimiento (+ motor con serie) en una sola transacción. */
    public void registrarEntrada(Repuesto r, int cantidad, BigDecimal costo, String referencia, String serieMotor)
            throws AutoTechException {
        if (cantidad <= 0) throw new InventarioException("La cantidad de entrada debe ser mayor que cero.");
        if (costo == null || costo.signum() < 0) throw new InventarioException("El costo no puede ser negativo.");
        boolean esMotor = "Motor".equals(r.getCategoria());
        if (esMotor) {
            if (serieMotor == null || serieMotor.isBlank())
                throw new InventarioException("Un motor requiere número de serie.");
            if (cantidad != 1) throw new InventarioException("Los motores se ingresan de uno en uno (cada uno con su serie).");
        }
        try {
            Transaccion.ejecutar(con -> {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE Repuestos SET Stock = Stock + ?, CostoUnitario = ? WHERE IdRepuesto = ?")) {
                    ps.setInt(1, cantidad);
                    ps.setBigDecimal(2, costo);
                    ps.setInt(3, r.getId());
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO MovimientosInventario (IdRepuesto, TipoMovimiento, Cantidad, CostoUnitario, Referencia) "
                        + "VALUES (?, 'ENTRADA', ?, ?, ?)")) {
                    ps.setInt(1, r.getId());
                    ps.setInt(2, cantidad);
                    ps.setBigDecimal(3, costo);
                    ps.setString(4, referencia);
                    ps.executeUpdate();
                }
                if (esMotor) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO Motores (IdRepuesto, NumeroSerie) VALUES (?, ?)")) {
                        ps.setInt(1, r.getId());
                        ps.setString(2, serieMotor.trim().toUpperCase());
                        ps.executeUpdate();
                    }
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Agrega un consumo PENDIENTE a la orden (el precio se guarda como histórico). */
    public void agregarConsumo(int idOrden, int idRepuesto, int cantidad) throws AutoTechException {
        if (cantidad <= 0) throw new InventarioException("La cantidad debe ser mayor que cero.");
        try {
            Transaccion.ejecutar(con -> {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO ConsumosRepuesto (IdOrden, IdRepuesto, Cantidad, PrecioUnitario) "
                        + "SELECT ?, IdRepuesto, ?, PrecioVenta FROM OrdenesTrabajo o, Repuestos r "
                        + "WHERE o.IdOrden = ? AND r.IdRepuesto = ? AND r.Activa = 1 AND o.Estado = 'EN_REPARACION'")) {
                    ps.setInt(1, idOrden);
                    ps.setInt(2, cantidad);
                    ps.setInt(3, idOrden);
                    ps.setInt(4, idRepuesto);
                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("La orden debe estar En reparación y el repuesto debe estar activo.");
                    }
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public void quitarConsumoPendiente(int idConsumo) throws AutoTechException {
        try {
            Transaccion.ejecutar(con -> {
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM ConsumosRepuesto WHERE IdConsumo = ? AND Descontado = 0")) {
                    ps.setInt(1, idConsumo);
                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("Solo se pueden quitar consumos pendientes.");
                    }
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Descuenta el inventario de la orden con sp_DescontarInventarioOrden. Reintenta si hay deadlock. */
    public void descontarInventario(int idOrden) throws AutoTechException {
        for (int intento = 1; ; intento++) {
            try {
                Transaccion.ejecutar(con -> {
                    try (CallableStatement cs = con.prepareCall("{call sp_DescontarInventarioOrden(?)}")) {
                        cs.setInt(1, idOrden);
                        cs.execute();
                    }
                    return null;
                });
                return;
            } catch (SQLException e) {
                if (e.getErrorCode() == 1205 && intento < REINTENTOS) continue; // víctima de deadlock
                if (e.getErrorCode() == 50022) throw new InventarioException(e.getMessage());
                throw ErrorSql.traducir(e);
            }
        }
    }
}
