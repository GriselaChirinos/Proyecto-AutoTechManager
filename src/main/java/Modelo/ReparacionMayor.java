/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.math.BigDecimal;

/**
 *
 * @author 50496
 */
public class ReparacionMayor extends Servicio{

    public ReparacionMayor() {
        super();
    }

    public ReparacionMayor(int id, String codigo, String nombre, String descripcion, BigDecimal costoManoObra, double tiempoEstimadoHoras) {
        super(id, codigo, nombre, descripcion, costoManoObra, tiempoEstimadoHoras);
    }
    
    @Override 
    public int getNivel() { 
        return 5; 
    }

    @Override 
    public String getCategoria() { 
        return "Reparación mayor"; 
    }

    @Override 
    public int getDiasGarantia() { 
        return 365; 
    } 
}
