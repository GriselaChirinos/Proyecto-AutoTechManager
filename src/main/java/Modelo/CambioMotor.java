/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;


import java.time.LocalDateTime;
/**
 *
 * @author 50496
 */
public class CambioMotor {
    private int idOrden;
    private String serieMotorAnterior;
    private int idMotorNuevo;
    private String destinoMotorRetirado;
    private LocalDateTime fechaDesmontaje;
    private LocalDateTime fechaInstalacion;
    private String resultadoPruebas;
    private boolean pruebasAprobadas;

      // Constructor vacío
    public CambioMotor() {
        this.fechaDesmontaje = LocalDateTime.now();
        this.fechaInstalacion = LocalDateTime.now();
    }

    // Constructor con parámetros
    public CambioMotor(int idOrden, String serieMotorAnterior, int idMotorNuevo, String destinoMotorRetirado, LocalDateTime fechaDesmontaje, LocalDateTime fechaInstalacion, String resultadoPruebas, boolean pruebasAprobadas) {
        this.idOrden = idOrden;
        this.serieMotorAnterior = serieMotorAnterior;
        this.idMotorNuevo = idMotorNuevo;
        this.destinoMotorRetirado = destinoMotorRetirado;
        this.fechaDesmontaje = fechaDesmontaje != null ? fechaDesmontaje : LocalDateTime.now();
        this.fechaInstalacion = fechaInstalacion != null ? fechaInstalacion : LocalDateTime.now();
        this.resultadoPruebas = resultadoPruebas;
        this.pruebasAprobadas = pruebasAprobadas;
    }

    // Getters y Setters

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public String getSerieMotorAnterior() {
        return serieMotorAnterior;
    }

    public void setSerieMotorAnterior(String serieMotorAnterior) {
        this.serieMotorAnterior = serieMotorAnterior;
    }

    public int getIdMotorNuevo() {
        return idMotorNuevo;
    }

    public void setIdMotorNuevo(int idMotorNuevo) {
        this.idMotorNuevo = idMotorNuevo;
    }

    public String getDestinoMotorRetirado() {
        return destinoMotorRetirado;
    }

    public void setDestinoMotorRetirado(String destinoMotorRetirado) {
        this.destinoMotorRetirado = destinoMotorRetirado;
    }

    public LocalDateTime getFechaDesmontaje() {
        return fechaDesmontaje;
    }

    public void setFechaDesmontaje(LocalDateTime fechaDesmontaje) {
        this.fechaDesmontaje = fechaDesmontaje;
    }

    public LocalDateTime getFechaInstalacion() {
        return fechaInstalacion;
    }

    public void setFechaInstalacion(LocalDateTime fechaInstalacion) {
        this.fechaInstalacion = fechaInstalacion;
    }

    public String getResultadoPruebas() {
        return resultadoPruebas;
    }

    public void setResultadoPruebas(String resultadoPruebas) {
        this.resultadoPruebas = resultadoPruebas;
    }

    public boolean isPruebasAprobadas() {
        return pruebasAprobadas;
    }

    public void setPruebasAprobadas(boolean pruebasAprobadas) {
        this.pruebasAprobadas = pruebasAprobadas;
    }
}
