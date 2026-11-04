package com.financorp.serf.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** DTOs de entrada de la API. */
public final class Requests {
    private Requests() {}

    public record ProductoRequest(String codigo, String nombre, String descripcionTecnica, String categoria,
                                  BigDecimal costoImportacion, String monedaOrigen, BigDecimal precioSugerido,
                                  String proveedor, LocalDate fechaImportacion, int stockInicial) {}

    public record VentaRequest(String numeroFactura, LocalDate fecha, String codigoProducto, int cantidad,
                               BigDecimal precioUnitario, String filial, String cliente, String vendedor,
                               String metodoPago) {}
}
