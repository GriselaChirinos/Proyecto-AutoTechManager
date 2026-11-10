/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author 50496
 */

/** Datos de entrada inválidos (campos vacíos, formatos, rangos). */

public class ValidacionException extends AutoTechException {
    public ValidacionException(String mensaje) {
        super(mensaje); 
    }
}
