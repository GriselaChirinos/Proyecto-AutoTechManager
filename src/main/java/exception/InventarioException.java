/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author 50496
 */
/** Stock insuficiente o movimiento de inventario inválido. */

public class InventarioException extends NegocioException {
    public InventarioException(String mensaje) { 
        super(mensaje);
    }
}
