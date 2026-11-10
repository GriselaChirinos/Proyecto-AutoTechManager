/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author 50496
 */
public abstract class BaseDAO<T> implements ICrud<T>{
@FunctionalInterface
    public interface Mapeador<R> {
        R mapear(ResultSet rs) throws SQLException;
    }

    protected abstract String tabla();
    protected abstract String columnaId();
    protected abstract T mapear(ResultSet rs) throws SQLException;

    protected static void asignar(PreparedStatement ps, Object...p) throws SQLException {
        for (int i = 0; i < p.length; i++) {
            ps.setObject(i + 1, p[i]);
        }
    }

    protected int ejecutar(String sql, Object... p) throws SQLException {
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            asignar(ps, p);
            return ps.executeUpdate();
        }
    }

    protected int insertarObteniendoId(String sql, Object... p) throws SQLException {
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            asignar(ps, p);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    protected <R> List<R> consultar(String sql, Mapeador<R> mapeador, Object... p) throws SQLException {
        List<R> lista = new ArrayList<>();
        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            asignar(ps, p);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapeador.mapear(rs));
                }
            }
        }
        return lista;
    }

    protected <R> R consultarUno(String sql, Mapeador<R> mapeador, Object... p) throws SQLException {
        List<R> lista = consultar(sql, mapeador, p);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // --- Implementaciones comunes del contrato ---

    @Override
    public T buscarPorId(int id) throws SQLException {
        return consultarUno("SELECT * FROM " + tabla() + " WHERE " + columnaId() + " = ?", this::mapear, id);
    }

    @Override
    public List<T> listar() throws SQLException {
        return consultar("SELECT * FROM " + tabla() + " ORDER BY " + columnaId() + " DESC", this::mapear);
    }

    @Override
    public boolean desactivar(int id) throws SQLException {
        return ejecutar("UPDATE " + tabla() + " SET Activo = 0 WHERE " + columnaId() + " = ?", id) > 0;
    }
    
}
