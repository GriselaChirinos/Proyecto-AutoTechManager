/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 50496
 */
public interface ICrud<T>{
    int insertar(T entidad) throws SQLException;        // devuelve el Id generado
    boolean actualizar(T entidad) throws SQLException;
    boolean desactivar(int id) throws SQLException;     // baja lógica
    T buscarPorId(int id) throws SQLException;
    List<T> listar() throws SQLException;
}
