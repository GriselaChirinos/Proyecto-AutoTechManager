/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;


import DAO.OrdenDAO;
import DAO.ReporteDAO;
import exception.AutoTechException;
import exception.NegocioException;
import exception.ValidacionException;
import Modelo.EstadoOrden;
import Modelo.OrdenTrabajo;
import Modelo.ResultadoTabla;
import util.ErrorSql;
import util.Transaccion;
import util.Validador;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */
public class OrdenService {
    private final OrdenDAO ordenDAO = new OrdenDAO();
    private final ReporteDAO reporteDAO = new ReporteDAO();

    public OrdenTrabajo crearOrden(OrdenTrabajo o) throws AutoTechException {
        if (o.getIdCliente() <= 0) throw new ValidacionException("Seleccione un cliente.");
        if (o.getIdVehiculo() <= 0) throw new ValidacionException("Seleccione un vehículo.");
        Validador.requerido(o.getSintomas(), "Síntomas");
        if (o.getKilometraje() < 0) throw new ValidacionException("El kilometraje no puede ser negativo.");
        try {
            int id = ordenDAO.insertar(o);
            return ordenDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<OrdenTrabajo> listar() throws AutoTechException {
        try {
            return ordenDAO.listar();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<OrdenTrabajo> listarActivas() throws AutoTechException {
        try {
            return ordenDAO.listarActivas();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public OrdenTrabajo buscar(int id) throws AutoTechException {
        try {
            return ordenDAO.buscarPorId(id);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Estados a los que puede avanzar la orden desde su estado actual (para llenar el combo). */
    public List<EstadoOrden> siguientesEstados(EstadoOrden actual) {
        List<EstadoOrden> lista = new java.util.ArrayList<>();
        for (EstadoOrden e : EstadoOrden.values()) {
            if (actual.puedePasarA(e)) lista.add(e);
        }
        return lista;
    }

    public void asignarMecanico(int idOrden, int idMecanico, boolean responsable) throws AutoTechException {
        try {
            Transaccion.ejecutar(con -> {
                try (CallableStatement cs = con.prepareCall("{call sp_AsignarMecanico(?, ?, ?)}")) {
                    cs.setInt(1, idOrden);
                    cs.setInt(2, idMecanico);
                    cs.setBoolean(3, responsable);
                    cs.execute();
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public ResultadoTabla mecanicosDeOrden(int idOrden) throws AutoTechException {
        try {
            return reporteDAO.ejecutar(
                    "SELECT m.IdMecanico, m.Nombre + ' ' + m.Apellido AS Mecanico, "
                    + "       CASE WHEN om.EsResponsable = 1 THEN 'Sí' ELSE 'No' END AS Responsable "
                    + "FROM OrdenMecanico om INNER JOIN Mecanicos m ON m.IdMecanico = om.IdMecanico "
                    + "WHERE om.IdOrden = ? ORDER BY om.EsResponsable DESC", idOrden);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Agrega un servicio a la orden guardando el precio vigente como precio histórico. */
    public void agregarServicio(int idOrden, int idServicio) throws AutoTechException {
        try {
            Transaccion.ejecutar(con -> {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO DetalleOrden (IdOrden, IdServicio, HorasEstimadas, PrecioManoObra) "
                        + "SELECT o.IdOrden, s.IdServicio, s.TiempoEstimadoHoras, s.CostoManoObra "
                        + "FROM OrdenesTrabajo o, Servicios s WHERE o.IdOrden = ? AND s.IdServicio = ? AND s.Activo = 1 "
                        + "AND o.Estado IN ('RECIBIDA','EN_DIAGNOSTICO','EN_REPARACION')")) {
                    ps.setInt(1, idOrden);
                    ps.setInt(2, idServicio);
                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("No se puede agregar el servicio: revise el estado de la orden y que el servicio esté activo.");
                    }
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public ResultadoTabla serviciosDeOrden(int idOrden) throws AutoTechException {
        try {
            return reporteDAO.ejecutar(
                    "SELECT d.IdDetalle, s.Codigo, s.Nombre AS Servicio, s.Nivel, d.HorasEstimadas AS Horas, "
                    + "d.PrecioManoObra AS Precio "
                    + "FROM DetalleOrden d INNER JOIN Servicios s ON s.IdServicio = d.IdServicio WHERE d.IdOrden = ?",
                    idOrden);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Cambia el estado respetando el flujo definido en EstadoOrden. */
    public void cambiarEstado(int idOrden, EstadoOrden nuevo) throws AutoTechException {
        OrdenTrabajo o = buscar(idOrden);
        if (o == null) throw new NegocioException("La orden no existe.");
        EstadoOrden actual = o.getEstado();
        if (!actual.puedePasarA(nuevo)) {
            throw new NegocioException("Transición no permitida: de '" + actual + "' a '" + nuevo + "'.");
        }
        if (nuevo == EstadoOrden.PENDIENTE_APROBACION || (nuevo == EstadoOrden.EN_REPARACION && actual != EstadoOrden.EN_CONTROL_CALIDAD)) {
            throw new NegocioException("Este cambio se realiza desde 'Diagnóstico y presupuesto'.");
        }
        if (nuevo == EstadoOrden.FINALIZADO) { finalizar(idOrden); return; }
        try {
            if (actual == EstadoOrden.RECIBIDA && nuevo == EstadoOrden.EN_DIAGNOSTICO && !ordenDAO.tieneResponsable(idOrden)) {
                throw new NegocioException("Asigne un mecánico responsable antes de iniciar el diagnóstico.");
            }
            Transaccion.ejecutar(con -> {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE OrdenesTrabajo SET Estado = ? WHERE IdOrden = ? AND Estado = ?")) {
                    ps.setString(1, nuevo.name());
                    ps.setInt(2, idOrden);
                    ps.setString(3, actual.name());
                    if (ps.executeUpdate() == 0) throw new SQLException("La orden cambió de estado mientras se editaba. Actualice la lista.");
                }
                if (nuevo == EstadoOrden.EN_CONTROL_CALIDAD) {
                    try (CallableStatement cs = con.prepareCall("{call sp_GenerarControlesCalidad(?)}")) {
                        cs.setInt(1, idOrden);
                        cs.execute();
                    }
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    /** Cierra la orden: sp_FinalizarOrden valida actividades, consumos y controles de calidad. */
    public void finalizar(int idOrden) throws AutoTechException {
        try {
            Transaccion.ejecutar(con -> {
                try (CallableStatement cs = con.prepareCall("{call sp_FinalizarOrden(?)}")) {
                    cs.setInt(1, idOrden);
                    cs.execute();
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    // ---------- Actividades de trabajo ----------
    public void registrarActividad(int idOrden, int idDetalle, int idMecanico, String descripcion) throws AutoTechException {
        Validador.requerido(descripcion, "Descripción de la actividad");
        try {
            Transaccion.ejecutar(con -> {
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO ActividadesTrabajo (IdOrden, IdDetalle, IdMecanico, Descripcion) "
                        + "SELECT ?, d.IdDetalle, om.IdMecanico, ? FROM DetalleOrden d "
                        + "INNER JOIN OrdenMecanico om ON om.IdOrden = d.IdOrden AND om.IdMecanico = ? "
                        + "INNER JOIN OrdenesTrabajo o ON o.IdOrden = d.IdOrden "
                        + "WHERE d.IdDetalle = ? AND d.IdOrden = ? AND o.Estado IN ('EN_DIAGNOSTICO','EN_REPARACION')")) {
                    ps.setInt(1, idOrden);
                    ps.setString(2, descripcion.trim());
                    ps.setInt(3, idMecanico);
                    ps.setInt(4, idDetalle);
                    ps.setInt(5, idOrden);
                    if (ps.executeUpdate() == 0) {
                        throw new SQLException("El mecánico no está asignado a la orden o la orden no admite actividades en su estado actual.");
                    }
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public void iniciarActividad(int idActividad) throws AutoTechException {
        cambiarActividad("UPDATE ActividadesTrabajo SET Estado = 'EN_PROCESO', FechaInicio = SYSDATETIME() "
                + "WHERE IdActividad = ? AND Estado = 'PENDIENTE'", idActividad, "Solo se puede iniciar una actividad pendiente.");
    }

    public void completarActividad(int idActividad) throws AutoTechException {
        cambiarActividad("UPDATE ActividadesTrabajo SET Estado = 'COMPLETADA', FechaFin = SYSDATETIME(), "
                + "HorasReales = CAST(DATEDIFF(MINUTE, FechaInicio, SYSDATETIME()) / 60.0 AS DECIMAL(6,2)) "
                + "WHERE IdActividad = ? AND Estado = 'EN_PROCESO'", idActividad, "Solo se puede completar una actividad en proceso.");
    }

    private void cambiarActividad(String sql, int idActividad, String errorSiNinguna) throws AutoTechException {
        try {
            Transaccion.ejecutar(con -> {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, idActividad);
                    if (ps.executeUpdate() == 0) throw new SQLException(errorSiNinguna);
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public ResultadoTabla actividadesDeOrden(int idOrden) throws AutoTechException {
        try {
            return reporteDAO.ejecutar(
                    "SELECT a.IdActividad, s.Nombre AS Servicio, a.Descripcion, m.Nombre + ' ' + m.Apellido AS Mecanico, "
                    + "       a.Estado, a.HorasReales AS Horas "
                    + "FROM ActividadesTrabajo a INNER JOIN DetalleOrden d ON d.IdDetalle = a.IdDetalle "
                    + "INNER JOIN Servicios s ON s.IdServicio = d.IdServicio "
                    + "INNER JOIN Mecanicos m ON m.IdMecanico = a.IdMecanico WHERE a.IdOrden = ? ORDER BY a.IdActividad",
                    idOrden);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public ResultadoTabla consumosDeOrden(int idOrden) throws AutoTechException {
        try {
            return reporteDAO.ejecutar(
                    "SELECT c.IdConsumo, r.Codigo, r.Nombre AS Repuesto, c.Cantidad, c.PrecioUnitario AS Precio, "
                    + "       CASE WHEN c.Descontado = 1 THEN 'Descontado' ELSE 'Pendiente' END AS Estado "
                    + "FROM ConsumosRepuesto c INNER JOIN Repuestos r ON r.IdRepuesto = c.IdRepuesto WHERE c.IdOrden = ?",
                    idOrden);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    // ---------- Control de calidad ----------
    public ResultadoTabla controlesDeOrden(int idOrden) throws AutoTechException {
        try {
            return reporteDAO.ejecutar(
                    "SELECT IdControl, Criterio, CASE WHEN Obligatorio = 1 THEN 'Sí' ELSE 'No' END AS Obligatorio, "
                    + "       CASE WHEN Aprobado IS NULL THEN 'Pendiente' WHEN Aprobado = 1 THEN 'Aprobado' ELSE 'Rechazado' END AS Resultado "
                    + "FROM ControlesCalidad WHERE IdOrden = ? ORDER BY IdControl", idOrden);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public void resolverControl(int idControl, boolean aprobado, int idMecanico, String observacion) throws AutoTechException {
        try {
            Transaccion.ejecutar(con -> {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE ControlesCalidad SET Aprobado = ?, IdMecanico = ?, FechaControl = SYSDATETIME(), Observacion = ? "
                        + "WHERE IdControl = ? AND IdOrden IN (SELECT IdOrden FROM OrdenesTrabajo WHERE Estado = 'EN_CONTROL_CALIDAD')")) {
                    ps.setBoolean(1, aprobado);
                    ps.setInt(2, idMecanico);
                    ps.setString(3, observacion);
                    ps.setInt(4, idControl);
                    if (ps.executeUpdate() == 0) throw new SQLException("La orden no está En control de calidad.");
                }
                return null;
            });
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }
}
