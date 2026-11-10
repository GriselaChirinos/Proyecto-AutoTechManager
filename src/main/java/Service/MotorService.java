/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.MotorDAO;
import DAO.ReporteDAO;
import exception.AutoTechException;
import exception.CompatibilidadException;
import exception.ValidacionException;
import Modelo.CambioMotor;
import Modelo.Motor;
import Modelo.ResultadoTabla;
import util.ErrorSql;
import util.Transaccion;
import util.Validador;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author 50496
 */
public class MotorService {
    private final MotorDAO motorDAO = new MotorDAO();
    private final ReporteDAO reporteDAO = new ReporteDAO();

    public List<Motor> listarDisponibles() throws AutoTechException {
        try {
            return motorDAO.listarDisponibles();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Pasos 3 y 4: bloquea motores incompatibles, no disponibles o sin datos, e informa la causa. */
    public void validarCompatibilidad(int idMotor, int idVehiculo) throws AutoTechException {
        try {
            Motor m = motorDAO.buscarPorId(idMotor);
            if (m == null) throw new CompatibilidadException("El motor seleccionado no existe.");
            if (m.getNumeroSerie() == null || m.getNumeroSerie().isBlank())
                throw new CompatibilidadException("El motor no tiene número de serie registrado (dato obligatorio).");
            if (!"DISPONIBLE".equals(m.getEstado()))
                throw new CompatibilidadException("El motor " + m.getNumeroSerie() + " no está disponible (estado: " + m.getEstado() + ").");
            ResultadoTabla ok = reporteDAO.ejecutar("SELECT dbo.fn_EsCompatible(?, ?)", idMotor, idVehiculo);
            boolean compatible = Boolean.TRUE.equals(ok.getFilas().get(0)[0]);
            if (!compatible) {
                ResultadoTabla compat = reporteDAO.ejecutar(
                        "SELECT Marca + ' ' + Modelo + ' ' + CAST(AnioDesde AS VARCHAR(4)) + '-' + CAST(AnioHasta AS VARCHAR(4)) "
                        + " + ISNULL(' ' + Combustible, '') FROM CompatibilidadRepuesto WHERE IdRepuesto = ?",
                        m.getIdRepuesto());
                StringBuilder sb = new StringBuilder("Motor incompatible con el vehículo. Compatible con: ");
                if (compat.getFilas().isEmpty()) sb.append("(sin compatibilidades registradas)");
                else {
                    for (Object[] f : compat.getFilas()) sb.append(f[0]).append("; ");
                }
                throw new CompatibilidadException(sb.toString());
            }
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Pasos 8 a 10 en UNA transacción: registra el cambio y descuenta el inventario; si algo falla, todo se revierte. */
    public void registrarCambio(CambioMotor c) throws AutoTechException {
        Validador.requerido(c.getSerieMotorAnterior(), "Serie del motor anterior");
        Validador.requerido(c.getDestinoMotorRetirado(), "Destino del motor retirado");
        Validador.requerido(c.getResultadoPruebas(), "Resultado de las pruebas");
        if (c.getFechaDesmontaje() == null || c.getFechaInstalacion() == null)
            throw new ValidacionException("Indique las fechas de desmontaje e instalación (aaaa-mm-dd).");
        if (c.getFechaInstalacion().isBefore(c.getFechaDesmontaje()))
            throw new ValidacionException("La instalación no puede ser anterior al desmontaje.");
        try {
            Transaccion.ejecutar(con -> {
                try (CallableStatement cs = con.prepareCall("{call sp_RegistrarCambioMotor(?, ?, ?, ?, ?, ?, ?, ?)}")) {
                    cs.setInt(1, c.getIdOrden());
                    cs.setString(2, c.getSerieMotorAnterior().trim());
                    cs.setInt(3, c.getIdMotorNuevo());
                    cs.setString(4, c.getDestinoMotorRetirado());
                    cs.setTimestamp(5, Timestamp.valueOf(c.getFechaDesmontaje()));
                    cs.setTimestamp(6, Timestamp.valueOf(c.getFechaInstalacion()));
                    cs.setString(7, c.getResultadoPruebas().trim());
                    cs.setBoolean(8, c.isPruebasAprobadas());
                    cs.execute();
                }
                try (CallableStatement cs = con.prepareCall("{call sp_DescontarInventarioOrden(?)}")) {
                    cs.setInt(1, c.getIdOrden());
                    cs.execute();
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public ResultadoTabla listarCambios() throws AutoTechException {
        try {
            return reporteDAO.ejecutar(
                    "SELECT o.NumeroOrden AS [N. Orden], cm.SerieMotorAnterior AS [Serie anterior], cm.SerieMotorNuevo AS [Serie nueva], "
                    + "       cm.DestinoMotorRetirado AS Destino, CASE WHEN cm.PruebasAprobadas = 1 THEN 'Aprobadas' ELSE 'No aprobadas' END AS Pruebas, "
                    + "       CONVERT(VARCHAR(10), cm.FechaInstalacion, 120) AS Fecha "
                    + "FROM CambiosMotor cm INNER JOIN OrdenesTrabajo o ON o.IdOrden = cm.IdOrden ORDER BY cm.IdCambio DESC");
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public ResultadoTabla garantiaDeOrden(int idOrden) throws AutoTechException {
        try {
            return reporteDAO.ejecutar("SELECT Descripcion, FechaInicio AS Inicio, FechaFin AS Fin, KilometrajeLimite AS [Km limite] "
                    + "FROM Garantias WHERE IdOrden = ?", idOrden);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }
}
