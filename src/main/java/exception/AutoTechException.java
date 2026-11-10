/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception;

/**
 *
 * @author 50496
 */
public class AutoTechException extends Exception{
    public AutoTechException(String mensaje) { 
        super(mensaje); 
    }
    public AutoTechException(String mensaje, Throwable causa) { 
        super(mensaje, causa); 
    }
}
