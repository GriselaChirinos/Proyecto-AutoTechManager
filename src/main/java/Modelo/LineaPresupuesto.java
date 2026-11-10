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

/** Línea de un presupuesto: tipo SERVICIO, REPUESTO u OTRO. */
public class LineaPresupuesto {
    private String tipo;
    private int idItem; // IdServicio o IdRepuesto (0 si es OTRO)
    private String descripcion;
    private BigDecimal horas; // solo para SERVICIO
    private BigDecimal cantidad = BigDecimal.ONE;
    private BigDecimal precioUnitario = BigDecimal.ZERO;

    // Constructor vacío
    public LineaPresupuesto() {
        this.cantidad = BigDecimal.ONE;
        this.precioUnitario = BigDecimal.ZERO;
    }

    // Constructor con parámetros
    public LineaPresupuesto(String tipo, int idItem, String descripcion, BigDecimal horas, BigDecimal cantidad, BigDecimal precioUnitario) {
        this.tipo = tipo;
        this.idItem = idItem;
        this.descripcion = descripcion;
        this.horas = horas;
        if (cantidad != null) {
            this.cantidad = cantidad;
        }
        if (precioUnitario != null) {
            this.precioUnitario = precioUnitario;
        }
    }

    public BigDecimal getSubtotal() { 
        return cantidad.multiply(precioUnitario); 
    }

    // Getters y Setters

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getHoras() {
        return horas;
    }

    public void setHoras(BigDecimal horas) {
        this.horas = horas;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        if (cantidad != null) {
            this.cantidad = cantidad;
        }
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        if (precioUnitario != null) {
            this.precioUnitario = precioUnitario;
        }
    }
}
