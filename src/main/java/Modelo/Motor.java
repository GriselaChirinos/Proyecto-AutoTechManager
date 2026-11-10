/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author 50496
 */
public class Motor {
    private int id;
    private int idRepuesto;
    private String numeroSerie;
    private String estado;
    private String descripcion;

    // Constructor vacío
    public Motor() {
    }

    // Constructor con parámetros
    public Motor(int id, int idRepuesto, String numeroSerie, String estado, String descripcion) {
        this.id = id;
        this.idRepuesto = idRepuesto;
        this.numeroSerie = numeroSerie;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return numeroSerie + " - " + descripcion;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(int idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
