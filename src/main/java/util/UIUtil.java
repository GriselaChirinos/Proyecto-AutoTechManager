/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import Modelo.ResultadoTabla;
import java.awt.Component;
import java.awt.Container;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 50496
 */

/** Utilidades de la capa de presentación (mensajes, tablas y combos). */

public final class UIUtil {

    private UIUtil() {}

    public static void error(Component padre, Exception e) {
        JOptionPane.showMessageDialog(padre, e.getMessage(), "No se pudo completar la operación", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "AutoTech Manager", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmar(Component padre, String mensaje) {
        return JOptionPane.showConfirmDialog(padre, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /** Llena un JTable con un modelo no editable. */
    public static void llenar(JTable tabla, String[] columnas, List<Object[]> filas) {
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        for (Object[] f : filas) {
            modelo.addRow(f);
        }
        tabla.setModel(modelo);
    }

    public static void llenar(JTable tabla, ResultadoTabla r) {
        llenar(tabla, r.getColumnas(), r.getFilas());
    }

    /** Llena un combo con objetos (el combo debe declararse como JComboBox<Object>). */
    
    public static void llenarCombo(JComboBox<Object> combo, List<?> items) {
        combo.setModel(new DefaultComboBoxModel<>(items.toArray()));
        combo.setSelectedIndex(items.isEmpty() ? -1 : 0);
    }

    public static <T> T seleccionado(JComboBox<Object> combo, Class<T> tipo) {
        Object o = combo.getSelectedItem();
        return tipo.isInstance(o) ? tipo.cast(o) : null;
    }

    public static String texto(JTextField campo) {
        return campo.getText().trim();
    }

    /** Limpia recursivamente los campos de texto de un contenedor. */
    public static void limpiar(Container contenedor) {
        for (Component c : contenedor.getComponents()) {
            if (c instanceof JTextField) {
                ((JTextField) c).setText("");
            } else if (c instanceof JTextArea) {
                ((JTextArea) c).setText("");
            } else if (c instanceof Container) {
                limpiar((Container) c);
            }
        }
    }
}
