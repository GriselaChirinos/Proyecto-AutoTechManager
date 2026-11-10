/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author 50496
 */

//Se crea la subclase correcta de Servicio segun el nivel del 1 al 5//
public final class ServicioFactory {
    
    // Constructor privado para evitar la instanciación de esta clase de utilidad
    private ServicioFactory() {}

    public static Servicio crear(int nivel) {
        switch (nivel) {
            case 1: 
                return new ServicioBasico();
            case 2: 
                return new ServicioPreventivo();
            case 3: 
                return new ReparacionIntermedia();
            case 4: 
                return new ReparacionAvanzada();
            case 5: 
                return new ReparacionMayor();
            default: 
                throw new IllegalArgumentException("Nivel de servicio inválido: " + nivel);
        }
    }
    
    
}
