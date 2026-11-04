package com.financorp.serf.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Encapsulado: estado privado, validaciones en el constructor y stock modificable solo vía descontarStock. */
public class Producto {
    private final String codigo, nombre, descripcionTecnica, proveedor, monedaOrigen;
    private final Categoria categoria;
    private final BigDecimal costoImportacion, costoImportacionEur, precioSugerido;
    private final LocalDate fechaImportacion;
    private final int stockInicial;
    private int stock;

    public Producto(String codigo, String nombre, String descripcionTecnica, Categoria categoria,
                    BigDecimal costoImportacion, String monedaOrigen, BigDecimal costoImportacionEur,
                    BigDecimal precioSugerido, String proveedor, LocalDate fechaImportacion, int stockInicial) {
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("El código es obligatorio");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre es obligatorio");
        if (categoria == null) throw new IllegalArgumentException("La categoría es obligatoria");
        if (costoImportacion == null || costoImportacion.signum() <= 0)
            throw new IllegalArgumentException("El costo de importación debe ser mayor a 0");
        if (precioSugerido == null || precioSugerido.signum() <= 0)
            throw new IllegalArgumentException("El precio sugerido debe ser mayor a 0");
        if (fechaImportacion == null) throw new IllegalArgumentException("La fecha de importación es obligatoria");
        if (stockInicial < 0) throw new IllegalArgumentException("El stock inicial no puede ser negativo");
        this.codigo = codigo.trim(); this.nombre = nombre.trim(); this.descripcionTecnica = descripcionTecnica;
        this.categoria = categoria; this.costoImportacion = costoImportacion; this.monedaOrigen = monedaOrigen;
        this.costoImportacionEur = costoImportacionEur; this.precioSugerido = precioSugerido;
        this.proveedor = proveedor; this.fechaImportacion = fechaImportacion;
        this.stockInicial = stockInicial; this.stock = stockInicial;
    }

    public synchronized void descontarStock(int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        if (cantidad > stock)
            throw new IllegalArgumentException("Stock insuficiente de " + codigo + ": disponible " + stock);
        stock -= cantidad;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcionTecnica() { return descripcionTecnica; }
    public Categoria getCategoria() { return categoria; }
    public BigDecimal getCostoImportacion() { return costoImportacion; }
    public String getMonedaOrigen() { return monedaOrigen; }
    public BigDecimal getCostoImportacionEur() { return costoImportacionEur; }
    public BigDecimal getPrecioSugerido() { return precioSugerido; }
    public String getProveedor() { return proveedor; }
    public LocalDate getFechaImportacion() { return fechaImportacion; }
    public int getStockInicial() { return stockInicial; }
    public synchronized int getStock() { return stock; }
}
