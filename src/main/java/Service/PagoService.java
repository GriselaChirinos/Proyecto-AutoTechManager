/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.ReporteDAO;
import exception.AutoTechException;
import exception.ValidacionException;
import Modelo.ResultadoTabla;
import util.ErrorSql;
import util.Transaccion;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 *
 * @author 50496
 */
public class PagoService {
    private final ReporteDAO reporteDAO = new ReporteDAO();

    /** Devuelve {total aprobado, pagado, saldo}; todo en cero si aún no hay presupuesto aprobado. */
    public BigDecimal[] resumen(int idOrden) throws AutoTechException {
        try {
            ResultadoTabla r = reporteDAO.ejecutar(
                    "SELECT TotalPresupuesto, Pagado, Saldo FROM vw_SaldoOrdenes WHERE IdOrden = ?", idOrden);
            if (r.getFilas().isEmpty())
                return new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
            Object[] f = r.getFilas().get(0);
            return new BigDecimal[] { (BigDecimal) f[0], (BigDecimal) f[1], (BigDecimal) f[2] };
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public void registrar(int idOrden, BigDecimal monto, String metodo, String referencia) throws AutoTechException {
        if (monto == null || monto.signum() <= 0)
            throw new ValidacionException("El monto debe ser mayor que cero.");
        
       BigDecimal[] r = resumen(idOrden);

System.out.println("Orden: " + idOrden);
System.out.println("Total: " + r[0]);
System.out.println("Pagado: " + r[1]);
System.out.println("Saldo: " + r[2]); 
        
        
        
        
        BigDecimal saldo = resumen(idOrden)[2];
        if (monto.compareTo(saldo) > 0)
            throw new ValidacionException("No se permite sobrepagar. Saldo pendiente: L " + saldo);
        try {
            Transaccion.ejecutar(con -> { // el SP vuelve a validar con bloqueo (concurrencia)
                try (CallableStatement cs = con.prepareCall("{call sp_RegistrarPago(?, ?, ?, ?)}")) {
                    cs.setInt(1, idOrden);
                    cs.setBigDecimal(2, monto);
                    cs.setString(3, metodo);
                    cs.setString(4, referencia);
                    cs.execute();
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public ResultadoTabla pagosDeOrden(int idOrden) throws AutoTechException {
        try {
            return reporteDAO.ejecutar(
                    "SELECT IdPago, CONVERT(VARCHAR(16), Fecha, 120) AS Fecha, Monto, Metodo, Referencia "
                    + "FROM Pagos WHERE IdOrden = ? ORDER BY IdPago", idOrden);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }
}
