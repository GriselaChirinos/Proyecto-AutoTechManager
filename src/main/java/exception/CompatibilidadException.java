/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author 50496
 */

/** Motor/repuesto incompatible, no disponible o con datos incompletos. */

public class CompatibilidadException extends NegocioException {
    public CompatibilidadException(String mensaje) {
        super(mensaje); 
    }
}
