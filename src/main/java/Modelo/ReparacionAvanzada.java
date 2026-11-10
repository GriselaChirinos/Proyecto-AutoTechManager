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
public class ReparacionAvanzada extends Servicio{

    
    //Constructor vacio
    public ReparacionAvanzada() {
        super();
    }
    
    
    //Constructor con parametros 
    public ReparacionAvanzada(int id,  String codigo, String nombre, String descripcion, BigDecimal costoManoObra, double tiempoEstimadoHoras){
        super(id, codigo, nombre, descripcion, costoManoObra, tiempoEstimadoHoras);
    }
    
    @Override 
    public int getNivel() { 
        return 4; 
    }

    @Override 
    public String getCategoria() { 
        return "Reparación avanzada"; 
    }

    @Override 
    public int getDiasGarantia() { 
        return 180; 
    }
}
