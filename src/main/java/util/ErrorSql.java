/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import exception.NegocioException;
import java.sql.SQLException;

/**
 *
 * @author 50496
 */

/** Traduce errores de SQL Server a mensajes entendibles para el usuario. */
public final class ErrorSql {

    private ErrorSql() {}

    public static NegocioException traducir(SQLException e) {
        switch (e.getErrorCode()) {
            case 2627:
            case 2601:
                return new NegocioException("Registro duplicado: ya existe un dato con esa identidad, placa, VIN, código o serie.", e);
            case 547:
                return new NegocioException("Operación no permitida: el dato no cumple una restricción o tiene registros relacionados.", e);
            case 1205:
                return new NegocioException("La operación fue cancelada por concurrencia. Intente nuevamente.", e);
            default:
                // Los errores de reglas de negocio (THROW 50xxx) ya traen el mensaje correcto
                String m = e.getMessage() == null ? "Error de base de datos." : e.getMessage();
                return new NegocioException(m, e);
        }
    }
}