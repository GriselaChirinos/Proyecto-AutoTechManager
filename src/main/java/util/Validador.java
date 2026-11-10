/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author 50496
 */

import exception.ValidacionException;
import java.math.BigDecimal;
import java.time.Year;

public final class Validador {

    private Validador() {}

    public static String requerido(String valor, String campo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacionException("El campo '" + campo + "' es obligatorio.");
        }
        return valor.trim();
    }

    public static int entero(String valor, String campo) throws ValidacionException {
        try {
            return Integer.parseInt(requerido(valor, campo));
        } catch (NumberFormatException e) {
            throw new ValidacionException("El campo '" + campo + "' debe ser un número entero.");
        }
    }

    public static int enteroPositivo(String valor, String campo) throws ValidacionException {
        int n = entero(valor, campo);
        if (n <= 0) {
            throw new ValidacionException("El campo '" + campo + "' debe ser mayor que cero.");
        }
        return n;
    }

    public static int enteroNoNegativo(String valor, String campo) throws ValidacionException {
        int n = entero(valor, campo);
        if (n < 0) {
            throw new ValidacionException("El campo '" + campo + "' no puede ser negativo.");
        }
        return n;
    }

    public static BigDecimal decimal(String valor, String campo) throws ValidacionException {
        BigDecimal d;
        try {
            d = new BigDecimal(requerido(valor, campo).replace(",", ""));
        } catch (NumberFormatException e) {
            throw new ValidacionException("El campo '" + campo + "' debe ser un número válido.");
        }
        if (d.signum() < 0) {
            throw new ValidacionException("El campo '" + campo + "' no puede ser negativo.");
        }
        return d;
    }

    public static BigDecimal decimalPositivo(String valor, String campo) throws ValidacionException {
        BigDecimal d = decimal(valor, campo);
        if (d.signum() == 0) {
            throw new ValidacionException("El campo '" + campo + "' debe ser mayor que cero.");
        }
        return d;
    }

    public static void placa(String placa) throws ValidacionException {
        if (!requerido(placa, "Placa").matches("[A-Za-z0-9-]{5,10}")) {
            throw new ValidacionException("La placa debe tener entre 5 y 10 caracteres alfanuméricos.");
        }
    }

    public static void anio(int anio) throws ValidacionException {
        if (anio < 1950 || anio > Year.now().getValue() + 1) {
            throw new ValidacionException("El año del vehículo no es válido.");
        }
    }

    public static void correo(String correo) throws ValidacionException {
        if (correo != null && !correo.isBlank() && !correo.matches("^[\\w.-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new ValidacionException("El correo electrónico no tiene un formato válido.");
        }
    }
}