/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author 50496
 */
public class Vehiculo {
    private int id;
    private int idCliente;
    private String placa;
    private String vin;
    private String marca;
    private String modelo;
    private int año;
    private String color;
    private int kilometraje;
    private String combustible;
    private String serieMotor;
    private boolean activo = true;
    
    //Constructor vacio

    public Vehiculo() {
    }
    
    
    //Constructor con parametros
    public Vehiculo(int id, int idCliente, String placa, String vin, String marca,String modelo,int año,String color,int kilometraje,String combustible,String serieMotor){
        this.id = id;
        this.idCliente = idCliente;
        this.placa = placa;
        this.vin = vin;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.color = color;
        this.kilometraje = kilometraje;
        this.combustible = combustible;
        this.serieMotor = serieMotor;
        this.activo = true;
    }
    
    @Override
    public String toString(){
        return placa + " - " + marca + " " + modelo + " " + año;
    }
    
    
    //Metodos Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public String getSerieMotor() {
        return serieMotor;
    }

    public void setSerieMotor(String serieMotor) {
        this.serieMotor = serieMotor;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    
    
}
