package com.financorp.serf.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Venta {
    private final String numeroFactura, codigoProducto, filial, moneda, cliente, vendedor;
    private final LocalDate fecha;
    private final int cantidad;
    private final BigDecimal precioUnitario, totalEur;
    private final MetodoPago metodoPago;

    public Venta(String numeroFactura, LocalDate fecha, String codigoProducto, int cantidad,
                 BigDecimal precioUnitario, String filial, String moneda, String cliente,
                 String vendedor, MetodoPago metodoPago, BigDecimal totalEur) {
        this.numeroFactura = numeroFactura; this.fecha = fecha; this.codigoProducto = codigoProducto;
        this.cantidad = cantidad; this.precioUnitario = precioUnitario; this.filial = filial;
        this.moneda = moneda; this.cliente = cliente; this.vendedor = vendedor;
        this.metodoPago = metodoPago; this.totalEur = totalEur;
    }

    public String getNumeroFactura() { return numeroFactura; }
    public LocalDate getFecha() { return fecha; }
    public String getCodigoProducto() { return codigoProducto; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public String getFilial() { return filial; }
    public String getMoneda() { return moneda; }
    public String getCliente() { return cliente; }
    public String getVendedor() { return vendedor; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public BigDecimal getTotalEur() { return totalEur; }
}
