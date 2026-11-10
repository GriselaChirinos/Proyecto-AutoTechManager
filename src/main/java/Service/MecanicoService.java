/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.MecanicoDAO;
import exception.AutoTechException;
import exception.NegocioException;
import exception.ValidacionException;
import Modelo.Mecanico;
import util.ErrorSql;
import util.Validador;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author 50496
 */
public class MecanicoService {
    private final MecanicoDAO dao = new MecanicoDAO();

    public int guardar(Mecanico m) throws AutoTechException {
        m.setIdentidad(Validador.requerido(m.getIdentidad(), "Identidad"));
        m.setNombre(Validador.requerido(m.getNombre(), "Nombre"));
        m.setApellido(Validador.requerido(m.getApellido(), "Apellido"));
        m.setEspecialidad(Validador.requerido(m.getEspecialidad(), "Especialidad"));
        try {
            if (m.getId() == 0) {
                return dao.insertar(m);
            }
            if (!dao.actualizar(m)) {
                throw new NegocioException("El mecánico ya no existe.");
            }
            return m.getId();
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

    public List<Mecanico> listar() throws AutoTechException {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Mecanico> listarActivos() throws AutoTechException {
        try {
            return dao.listarActivos();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public int ordenesActivas(int idMecanico) throws AutoTechException {
        try {
            return dao.contarOrdenesActivas(idMecanico);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }
}
