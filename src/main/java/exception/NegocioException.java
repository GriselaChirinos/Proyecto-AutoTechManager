/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author 50496
 */

/** Violación de una regla de negocio o error devuelto por la base de datos. */

public class NegocioException extends AutoTechException {
    public NegocioException(String mensaje) { 
        super(mensaje); 
    }
    
    public NegocioException(String mensaje, Throwable causa) { 
        super(mensaje, causa); 
    }
}
