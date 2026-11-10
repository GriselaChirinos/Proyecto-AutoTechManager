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
public class ServicioPreventivo extends Servicio{
    
    //Constructor Vacio
    public ServicioPreventivo() {
        super();
    }
    
    //Constructor con parametros 
    public ServicioPreventivo(int id,  String codigo, String nombre, String descripcion, BigDecimal costoManoObra, double tiempoEstimadoHoras){
        super(id, codigo, nombre, descripcion, costoManoObra, tiempoEstimadoHoras);
    }
    
   @Override
   public int getNivel(){
       return 2;
   }
    
   @Override
   public String getCategoria(){
       return "Mantenimiento preventivo";
   }
   
   @Override
   public int getDiasGarantia(){
       return 30;
   }
    
}
