/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.ServicioDAO;
import exception.AutoTechException;
import exception.NegocioException;
import exception.ValidacionException;
import Modelo.Servicio;
import util.ErrorSql;
import util.Validador;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */
public class CatalogoService {
    private final ServicioDAO dao = new ServicioDAO();

    /** Nota: cambiar el costo aquí NO altera DetalleOrden, porque cada orden guarda su propio precio. */
    
    public int guardar(Servicio s) throws AutoTechException {
        Validador.requerido(s.getCodigo(), "Código");
        Validador.requerido(s.getNombre(), "Nombre");
        if (s.getCostoManoObra() == null || s.getCostoManoObra().signum() < 0) {
            throw new ValidacionException("El costo de mano de obra no puede ser negativo.");
        }
        if (s.getTiempoEstimadoHoras() <= 0) {
            throw new ValidacionException("El tiempo estimado debe ser mayor que cero.");
        }
        try {
            if (s.getId() == 0) return dao.insertar(s);
            if (!dao.actualizar(s)) throw new NegocioException("El servicio ya no existe.");
            return s.getId();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public void desactivar(int id) throws AutoTechException {
        try {
            dao.desactivar(id);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Servicio> listar() throws AutoTechException {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Servicio> listarActivos() throws AutoTechException {
        try {
            return dao.listarActivos();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public void asociarRepuesto(int idServicio, int idRepuesto, int cantidad) throws AutoTechException {
        if (cantidad <= 0) throw new ValidacionException("La cantidad debe ser mayor que cero.");
        try {
            dao.asociarRepuesto(idServicio, idRepuesto, cantidad);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public void quitarRepuesto(int idServicio, int idRepuesto) throws AutoTechException {
        try {
            dao.quitarRepuesto(idServicio, idRepuesto);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Object[]> repuestosDelServicio(int idServicio) throws AutoTechException {
        try {
            return dao.listarRepuestos(idServicio);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }
}
