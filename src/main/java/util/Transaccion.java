/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import config.Conexion;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author 50496
 */
/** Ejecuta varias sentencias en UNA transacción: commit si todo sale bien, rollback si algo falla. */
public final class Transaccion {

    private Transaccion() {}

    @FunctionalInterface
    public interface Tarea<R> {
        R ejecutar(Connection con) throws SQLException;
    }

    public static <R> R ejecutar(Tarea<R> tarea) throws SQLException {
        try (Connection con = Conexion.obtener()) {
            con.setAutoCommit(false);
            try {
                R resultado = tarea.ejecutar(con);
                con.commit();
                return resultado;
            } catch (SQLException | RuntimeException e) {
                try {
                    con.rollback();
                } catch (SQLException ignorar) {}
                throw e;
            }
        }
    }
}
