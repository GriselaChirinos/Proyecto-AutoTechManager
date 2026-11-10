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
public class OrdenTrabajo {
    private int id;
    private String numeroOrden;
    private int idCliente;
    private int idVehiculo;
    private LocalDateTime fechaIngreso;
    private int kilometraje;
    private String sintomas;
    private String diagnosticoPreliminar;
    private EstadoOrden estado = EstadoOrden.RECIBIDA;
    private String clienteNombre;
    private String vehiculoDescripcion;
    private String responsable;

    // Constructor vacío
    public OrdenTrabajo() {
        this.fechaIngreso = LocalDateTime.now();
        this.estado = EstadoOrden.RECIBIDA;
    }

    // Constructor con parámetros principales
    public OrdenTrabajo(int id, String numeroOrden, int idCliente, int idVehiculo, LocalDateTime fechaIngreso, int kilometraje, String sintomas, String diagnosticoPreliminar, String clienteNombre, String vehiculoDescripcion, String responsable) {
        this.id = id;
        this.numeroOrden = numeroOrden;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.fechaIngreso = fechaIngreso != null ? fechaIngreso : LocalDateTime.now();
        this.kilometraje = kilometraje;
        this.sintomas = sintomas;
        this.diagnosticoPreliminar = diagnosticoPreliminar;
        this.estado = EstadoOrden.RECIBIDA;
        this.clienteNombre = clienteNombre;
        this.vehiculoDescripcion = vehiculoDescripcion;
        this.responsable = responsable;
    }

    @Override
    public String toString() {
        return numeroOrden + " | " + vehiculoDescripcion;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getDiagnosticoPreliminar() {
        return diagnosticoPreliminar;
    }

    public void setDiagnosticoPreliminar(String diagnosticoPreliminar) {
        this.diagnosticoPreliminar = diagnosticoPreliminar;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getVehiculoDescripcion() {
        return vehiculoDescripcion;
    }

    public void setVehiculoDescripcion(String vehiculoDescripcion) {
        this.vehiculoDescripcion = vehiculoDescripcion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
}