/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.ReporteDAO;
import exception.AutoTechException;
import exception.NegocioException;
import exception.ValidacionException;
import Modelo.LineaPresupuesto;
import Modelo.Totales;
import util.ErrorSql;
import util.Transaccion;
import util.Validador;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

/**
 *
 * @author 50496
 */
public class PresupuestoService {
    private final ReporteDAO reporteDAO = new ReporteDAO();

    /** Lee un parámetro configurable (impuesto, descuento máximo...). */
    public BigDecimal parametro(String clave) throws AutoTechException {
        try {
            Object v = reporteDAO.ejecutar("SELECT Valor FROM ParametrosSistema WHERE Clave = ?", clave)
                    .getFilas().get(0)[0];
            return new BigDecimal(v.toString());
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Cálculo puro (sin base de datos): subtotal, descuento autorizado, impuesto configurable y total. */
    public Totales calcular(List<LineaPresupuesto> lineas, BigDecimal descuentoPct, BigDecimal impuestoPct) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (LineaPresupuesto l : lineas) {
            subtotal = subtotal.add(l.getSubtotal());
        }
        BigDecimal descuento = subtotal.multiply(descentoSct(descuentoPct)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal base = subtotal.subtract(descuento);
        BigDecimal impuesto = base.multiply(impuestoPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        Totales t = new Totales();
        t.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        t.setDescuento(descuento);
        t.setBaseImponible(base.setScale(2, RoundingMode.HALF_UP));
        t.setImpuesto(impuesto);
        t.setTotal(base.add(impuesto).setScale(2, RoundingMode.HALF_UP));
        return t;
    }

    private BigDecimal descentoSct(BigDecimal desc) {
        return desc != null ? desc : BigDecimal.ZERO;
    }

    /** Diagnóstico + evidencias en una transacción. Lo firma el mecánico responsable de la orden. */
    public void guardarDiagnostico(int idOrden, String evaluacion, boolean requiereMotor, List<String> evidencias)
            throws AutoTechException {
        Validador.requerido(evaluacion, "Evaluación técnica");
        try {
            Transaccion.ejecutar(con -> {
                int idMecanico;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT IdMecanico FROM OrdenMecanico WHERE IdOrden = ? AND EsResponsable = 1")) {
                    ps.setInt(1, idOrden);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("La orden no tiene mecánico responsable asignado.");
                        idMecanico = rs.getInt(1);
                    }
                }
                int idDiag;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Diagnosticos (IdOrden, IdMecanico, EvaluacionTecnica, RequiereSustucionMotor) "
                        + "SELECT ?, ?, ?, ? FROM OrdenesTrabajo WHERE IdOrden = ? AND Estado = 'EN_DIAGNOSTICO'",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, idOrden);
                    ps.setInt(2, idMecanico);
                    ps.setString(3, evaluacion.trim());
                    ps.setBoolean(4, requiereMotor);
                    ps.setInt(5, idOrden);
                    if (ps.executeUpdate() == 0) throw new SQLException("La orden debe estar En diagnóstico.");
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) idDiag = rs.getInt(1);
                        else throw new SQLException("No se pudo obtener el ID del diagnóstico.");
                    }
                }
                if (evidencias != null) {
                    for (String ev : evidencias) {
                        try (PreparedStatement ps = con.prepareStatement(
                                "INSERT INTO EvidenciasDiagnostico (IdDiagnostico, Descripcion, RutaArchivo) VALUES (?, ?, ?)")) {
                            String[] partes = ev.split("\\|", 2); // "descripcion|ruta"
                            ps.setInt(1, idDiag);
                            ps.setString(2, partes[0]);
                            ps.setString(3, partes.length > 1 ? partes[1] : null);
                            ps.executeUpdate();
                        }
                    }
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Guarda el presupuesto, sincroniza la orden y la pasa a "Pendiente de aprobación". Todo o nada. */
    public int guardarPresupuesto(int idOrden, List<LineaPresupuesto> lineas, BigDecimal descuentoPct, String autorizadoPor)
            throws AutoTechException {
        if (lineas == null || lineas.isEmpty())
            throw new ValidacionException("Agregue al menos una línea al presupuesto.");
        BigDecimal max = parametro("DESCUENTO_MAX_PCT");
        if (descuentoPct.signum() < 0 || descuentoPct.compareTo(max) > 0)
            throw new ValidacionException("El descuento debe estar entre 0 y " + max + " %.");
        if (descuentoPct.signum() > 0 && (autorizadoPor == null || autorizadoPor.isBlank()))
            throw new ValidacionException("Todo descuento debe indicar quién lo autorizó.");
        BigDecimal impuestoPct = parametro("IMPUESTO_PCT");
        Totales t = calcular(lineas, descuentoPct, impuestoPct);
        try {
            return Transaccion.ejecutar(con -> {
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT 1 FROM Diagnosticos d INNER JOIN OrdenesTrabajo o ON o.IdOrden = d.IdOrden "
                        + "WHERE d.IdOrden = ? AND o.Estado = 'EN_DIAGNOSTICO'")) {
                    ps.setInt(1, idOrden);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next())
                            throw new SQLException("Registre el diagnóstico con la orden En diagnóstico antes del presupuesto.");
                    }
                }
                int idPpto;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Presupuestos (IdOrden, Subtotal, DescuentoPct, DescuentoMonto, AutorizadoPor, "
                        + "ImpuestoPct, ImpuestoMonto, Total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, idOrden);
                    ps.setBigDecimal(2, t.getSubtotal());
                    ps.setBigDecimal(3, descuentoPct);
                    ps.setBigDecimal(4, t.getDescuento());
                    ps.setString(5, descuentoPct.signum() > 0 ? autorizadoPor.trim() : null);
                    ps.setBigDecimal(6, impuestoPct);
                    ps.setBigDecimal(7, t.getImpuesto());
                    ps.setBigDecimal(8, t.getTotal());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) idPpto = rs.getInt(1);
                        else throw new SQLException("No se pudo generar el ID del presupuesto.");
                    }
                }
                for (LineaPresupuesto l : lineas) {
                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO DetallePresupuesto (IdPresupuesto, Tipo, IdServicio, IdRepuesto, "
                            + "Descripcion, Horas, Cantidad, PrecioUnitario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                        ps.setInt(1, idPpto);
                        ps.setString(2, l.getTipo());
                        if ("SERVICIO".equals(l.getTipo())) {
                            ps.setInt(3, l.getIdItem());
                            ps.setNull(4, Types.INTEGER);
                        } else if ("REPUESTO".equals(l.getTipo())) {
                            ps.setNull(3, Types.INTEGER);
                            ps.setInt(4, l.getIdItem());
                        } else {
                            ps.setNull(3, Types.INTEGER);
                            ps.setNull(4, Types.INTEGER);
                        }
                        ps.setString(5, l.getDescripcion());
                        ps.setBigDecimal(6, l.getHoras());
                        ps.setBigDecimal(7, l.getCantidad());
                        ps.setBigDecimal(8, l.getPrecioUnitario());
                        ps.executeUpdate();
                    }
                }
                // Presupuestos anteriores pendientes quedan reemplazados (el nuevo ya fue insertado)
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE Presupuestos SET Estado = 'REEMPLAZADO' WHERE IdOrden = ? AND Estado = 'PENDIENTE' AND IdPresupuesto <> ?")) {
                    ps.setInt(1, idOrden);
                    ps.setInt(2, idPpto);
                    ps.executeUpdate();
                }
                // El alcance del trabajo queda registrado en la orden con precios históricos
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO DetalleOrden (IdOrden, IdServicio, HorasEstimadas, PrecioManoObra) "
                        + "SELECT ?, dp.IdServicio, ISNULL(dp.Horas, 1), dp.PrecioUnitario FROM DetallePresupuesto dp "
                        + "WHERE dp.IdPresupuesto = ? AND dp.Tipo = 'SERVICIO' AND NOT EXISTS "
                        + "(SELECT 1 FROM DetalleOrden d WHERE d.IdOrden = ? AND d.IdServicio = dp.IdServicio)")) {
                    ps.setInt(1, idOrden);
                    ps.setInt(2, idPpto);
                    ps.setInt(3, idOrden);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO ConsumosRepuesto (IdOrden, IdRepuesto, Cantidad, PrecioUnitario) "
                        + "SELECT ?, dp.IdRepuesto, CAST(dp.Cantidad AS INT), dp.PrecioUnitario FROM DetallePresupuesto dp "
                        + "WHERE dp.IdPresupuesto = ? AND dp.Tipo = 'REPUESTO' AND NOT EXISTS "
                        + "(SELECT 1 FROM ConsumosRepuesto c WHERE c.IdOrden = ? AND c.IdRepuesto = dp.IdRepuesto)")) {
                    ps.setInt(1, idOrden);
                    ps.setInt(2, idPpto);
                    ps.setInt(3, idOrden);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE OrdenesTrabajo SET Estado = 'PENDIENTE_APROBACION' WHERE IdOrden = ? AND Estado = 'EN_DIAGNOSTICO'")) {
                    ps.setInt(1, idOrden);
                    ps.executeUpdate();
                }
                return idPpto;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** El cliente aprueba: presupuesto APROBADO y orden EN_REPARACION en la misma transacción. */
    public void responder(int idOrden, boolean aprobado, String observacion) throws AutoTechException {
        try {
            Transaccion.ejecutar(con -> {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE Presupuestos SET Estado = ?, FechaRespuesta = SYSDATETIME(), Observacion = ? "
                        + "WHERE IdOrden = ? AND Estado = 'PENDIENTE'")) {
                    ps.setString(1, aprobado ? "APROBADO" : "RECHAZADO");
                    ps.setString(2, observacion);
                    ps.setInt(3, idOrden);
                    if (ps.executeUpdate() == 0)
                        throw new SQLException("No hay un presupuesto pendiente de respuesta para la orden.");
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE OrdenesTrabajo SET Estado = ? WHERE IdOrden = ? AND Estado = 'PENDIENTE_APROBACION'")) {
                    ps.setString(1, aprobado ? "EN_REPARACION" : "RECHAZADA");
                    ps.setInt(2, idOrden);
                    if (ps.executeUpdate() == 0)
                        throw new SQLException("La orden no está Pendiente de aprobación.");
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }
}
