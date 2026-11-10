/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.ClienteDAO;
import exception.AutoTechException;
import exception.NegocioException;
import Modelo.Cliente;
import util.ErrorSql;
import util.Validador;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author 50496
 */
public class ClienteService {
    private final ClienteDAO dao = new ClienteDAO();

    /** Inserta si id == 0; actualiza en caso contrario. Devuelve el id resultante.
     * @param c */
    
    public int guardar(Cliente c) throws AutoTechException {
        Validador.requerido(c.getIdentidad(), "Identidad");
        Validador.requerido(c.getNombre(), "Nombre");
        Validador.requerido(c.getApellido(), "Apellido");
        Validador.requerido(c.getTelefono(), "Teléfono");
        Validador.correo(c.getCorreo());
        try {
            if (c.getId() == 0) {
                return dao.insertar(c);
            }
            if (!dao.actualizar(c)) {
                throw new NegocioException("El cliente ya no existe.");
            }
            return c.getId();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public void desactivar(int idCliente) throws AutoTechException {
        try {
            if (!dao.desactivar(idCliente)) {
                throw new NegocioException("El cliente ya no existe.");
            }
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Cliente> listar() throws AutoTechException {
        try {
            return dao.listar();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Cliente> listarActivos() throws AutoTechException {
        try {
            return dao.listarActivos();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Cliente> buscar(String texto) throws AutoTechException {
        try {
            return dao.buscar(texto);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }
    
}
