/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author 50496
 */
public class Mecanico extends Persona{
    private String especialidad;
    
    //Constructor vacio

    public Mecanico() {
        super();
    }
    
    //Constructor ya con parametros heredados y propios
    public Mecanico(int id, String identidad, String nombre, String apellido, String telefono, String especialidad){
        super(id, identidad, nombre, apellido, telefono);
        this.especialidad = especialidad;
    }
    
    @Override 
    public String getTipo(){
        return "Mecanico";
    }
    
    @Override
    public String toString(){
        return getNombreCompleto() + "  ( " + especialidad + ")";
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
