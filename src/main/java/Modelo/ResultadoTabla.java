/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.List;

/**
 *
 * @author 50496
 */
/** Resultado genérico de una consulta: nombres de columnas y filas, listo para un JTable. */
public class ResultadoTabla {
    private final String[] columnas;
    private final List<Object[]> filas;

    public ResultadoTabla(String[] columnas, List<Object[]> filas) {
        this.columnas = columnas;
        this.filas = filas;
    }

    public String[] getColumnas() {
        return columnas;
    }

    public List<Object[]> getFilas() {
        return filas;
    }
}
