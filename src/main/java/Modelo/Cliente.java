/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author 50496
 */
public class Cliente extends Persona{
    private String correo;
    private String direccion;
    
    
    //Constructor vacio
    public Cliente() {
        super();
    }
    
    
    //Constructor con sus parametros propios
    public Cliente(int id,String identidad, String nombre, String apellido, String telefono, String correo, String direccion){
        super(id, identidad, nombre, apellido, telefono);
        this.correo = correo;
        this.direccion = direccion;
    }
    
    //Sobreescritura del metodo getTipo() lo cual lo pide el diseño
    @Override
    public String getTipo(){
        return "Cliente";
    }
    
    //Metodos Getters y Setters de los atributos propios del cliente

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
}
