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
public class Totales {
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal baseImponible;
    private BigDecimal impuesto;
    private BigDecimal total;

    // Constructor vacío
    public Totales() {
    }

    // Constructor con parámetros
    public Totales(BigDecimal subtotal, BigDecimal descuento, BigDecimal baseImponible, BigDecimal impuesto, BigDecimal total) {
        if (subtotal != null) this.subtotal = subtotal;
        if (descuento != null) this.descuento = descuento;
        if (baseImponible != null) this.baseImponible = baseImponible;
        if (impuesto != null) this.impuesto = impuesto;
        if (total != null) this.total = total;
    }

    // Getters y Setters

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        if (subtotal != null) {
            this.subtotal = subtotal;
        }
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        if (descuento != null) {
            this.descuento = descuento;
        }
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        if (baseImponible != null) {
            this.baseImponible = baseImponible;
        }
    }

    public BigDecimal getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(BigDecimal impuesto) {
        if (impuesto != null) {
            this.impuesto = impuesto;
        }
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        if (total != null) {
            this.total = total;
        }
    }
}
