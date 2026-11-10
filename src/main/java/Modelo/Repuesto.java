
package Modelo;

/**
 *
 * @author 50496
 */

import java.math.BigDecimal;

public class Repuesto {
    private int id;
    private String codigo;
    private String nombre;
    private String categoria;
    private int idProveedor;
    private BigDecimal costoUnitario = BigDecimal.ZERO;
    private BigDecimal precioVenta = BigDecimal.ZERO;
    private int stock;
    private int stockMinimo;
    private boolean activo = true;

    // Constructor vacío
    public Repuesto() {
    }

    // Constructor con parámetros principales
    public Repuesto(int id, String codigo, String nombre, String categoria, int idProveedor, BigDecimal costoUnitario, BigDecimal precioVenta, int stock, int stockMinimo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.idProveedor = idProveedor;
        if (costoUnitario != null) {
            this.costoUnitario = costoUnitario;
        }
        if (precioVenta != null) {
            this.precioVenta = precioVenta;
        }
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.activo = true;
    }

    public boolean isStockCritico() { 
        return stock <= stockMinimo; 
    }

    @Override 
    public String toString() { 
        return codigo + " - " + nombre; 
    }

    // Getters y Setters

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        if (costoUnitario != null) {
            this.costoUnitario = costoUnitario;
        }
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        if (precioVenta != null) {
            this.precioVenta = precioVenta;
        }
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
