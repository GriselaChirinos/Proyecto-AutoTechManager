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
public abstract class Servicio {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private BigDecimal costoManoObra = BigDecimal.ZERO;
    private double tiempoEstimadoHoras;
    private boolean activo = true;
    
    //Constructor vacio

    public Servicio() {
    }
    
    
    //Constructor con parametros 
    public Servicio(int id,String codigo,String nombre,String descripcion,BigDecimal costoManoObra,double tiempoEstimadoHoras){
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        if(costoManoObra != null){
            this.costoManoObra = costoManoObra;
        }
        this.tiempoEstimadoHoras = tiempoEstimadoHoras;
        this.activo = true;
    }
    
    //Comportamientos que cambian segun el tipo de servicio (polimorfismo)
    public abstract int getNivel();
    public abstract String getCategoria();
    public abstract int getDiasGarantia();
    
    /* Los servicios de nivel 3 en adelante exigen control de calidad */
    public boolean requiereControlCalidad (){
        return getNivel() >= 3;
    }
    
    @Override 
    public String toString (){
        return codigo + " - " + nombre;
    }
    
    //Metodos Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCostoManoObra() {
        return costoManoObra;
    }

    public void setCostoManoObra(BigDecimal costoManoObra) {
        this.costoManoObra = costoManoObra;
    }

    public double getTiempoEstimadoHoras() {
        return tiempoEstimadoHoras;
    }

    public void setTiempoEstimadoHoras(double tiempoEstimadoHoras) {
        this.tiempoEstimadoHoras = tiempoEstimadoHoras;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    
    
}
