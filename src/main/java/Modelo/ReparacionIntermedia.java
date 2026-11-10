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
public class ReparacionIntermedia extends Servicio{

    
    //Constructor vacio
    public ReparacionIntermedia() {
    }
    
    //Constructor con parametros 
    public ReparacionIntermedia(int id,  String codigo, String nombre, String descripcion, BigDecimal costoManoObra, double tiempoEstimadoHoras){
        super(id, codigo, nombre, descripcion, costoManoObra, tiempoEstimadoHoras);
    }
    
    @Override
    public int getNivel(){
        return 3;
    }
    
    @Override 
    public String getCategoria(){
        return "Reparacion Intermedia";
    }
    
    @Override 
    public int getDiasGarantia(){
        return 90;
    } 
}
