/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author 50496
 */
/** Flujo de la orden: Recibida > En diagnóstico > Pendiente de aprobación > En reparación
 * > En control de calidad > Finalizada y entregada (más Rechazada y Cancelada). */

public enum EstadoOrden {
    RECIBIDA("Recibida"),
    EN_DIAGNOSTICO("En diagnostico"),
    PENDIENTE_APROBACION("Pendiente de aprobacion"),
    EN_REPARACION("En reparacion"),
    EN_CONTROL_CALIDAD("En control de calidad"),
    FINALIZADO("Finalizada y entregada"),
    RECHAZADA("Rechazada"),
    CANCELADA("Cancelada");

    private final String etiqueta;

    EstadoOrden(String etiqueta) { 
        this.etiqueta = etiqueta; 
    }

    public boolean puedePasarA(EstadoOrden nuevo) {
        switch (this) {
            case RECIBIDA:           
                return nuevo == EN_DIAGNOSTICO || nuevo == CANCELADA;
            case EN_DIAGNOSTICO:     
                return nuevo == PENDIENTE_APROBACION || nuevo == CANCELADA;
            case PENDIENTE_APROBACION: 
                return nuevo == EN_REPARACION || nuevo == RECHAZADA || nuevo == CANCELADA;
            case EN_REPARACION:      
                return nuevo == EN_CONTROL_CALIDAD || nuevo == CANCELADA;
            case EN_CONTROL_CALIDAD: 
                return nuevo == FINALIZADO || nuevo == EN_REPARACION;
            default:                 
                return false; // estados finales
        }
    }

    public boolean esFinal() { 
        return this == FINALIZADO || this == RECHAZADA || this == CANCELADA; 
    }

    @Override 
    public String toString() { 
        return etiqueta; 
    }
}
