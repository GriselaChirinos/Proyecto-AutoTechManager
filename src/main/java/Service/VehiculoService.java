/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.VehiculoDAO;
import exception.AutoTechException;
import exception.NegocioException;
import exception.ValidacionException;
import Modelo.Vehiculo;
import util.ErrorSql;
import util.Validador;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */
public class VehiculoService {
    private final VehiculoDAO dao = new VehiculoDAO();

    public int guardar(Vehiculo v) throws AutoTechException {
        if (v.getIdCliente() <= 0) {
            throw new ValidacionException("Seleccione el cliente propietario.");
        }
        Validador.placa(v.getPlaca());
        String vin = Validador.requerido(v.getVin(), "VIN/Chasis");
        if (vin.length() < 5 || vin.length() > 25) {
            throw new ValidacionException("El VIN debe tener entre 5 y 25 caracteres.");
        }
        Validador.requerido(v.getMarca(), "Marca");
        Validador.requerido(v.getModelo(), "Modelo");
        Validador.anio(v.getAño());
        if (v.getKilometraje() < 0) {
            throw new ValidacionException("El kilometraje no puede ser negativo.");
        }
        Validador.requerido(v.getCombustible(), "Combustible");
        try {
            if (v.getId() == 0) {
                return dao.insertar(v);
            }
            if (!dao.actualizar(v)) {
                throw new NegocioException("El vehículo ya no existe.");
            }
            return v.getId();
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }

    public List<Vehiculo> listarPorCliente(int idCliente) throws AutoTechException {
        try {
            return dao.listarPorCliente(idCliente);
        } catch (SQLException e) {
            throw ErrorSql.traducir(e);
        }
    }
}
