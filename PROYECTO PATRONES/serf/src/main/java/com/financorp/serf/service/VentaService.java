package com.financorp.serf.service;

import com.financorp.serf.config.ConfiguracionGlobal;
import com.financorp.serf.model.*;
import com.financorp.serf.model.Requests.VentaRequest;
import com.financorp.serf.repository.ProductoRepositorio;
import com.financorp.serf.repository.VentaRepositorio;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VentaService {
    private final VentaRepositorio ventas;
    private final ProductoRepositorio productos;

    public VentaService(VentaRepositorio ventas, ProductoRepositorio productos) {
        this.ventas = ventas;
        this.productos = productos;
    }

    public synchronized Venta registrar(VentaRequest r) {
        var cfg = ConfiguracionGlobal.getInstance();
        Producto p = productos.porCodigo(r.codigoProducto())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + r.codigoProducto()));
        if (r.fecha() == null) throw new IllegalArgumentException("La fecha de venta es obligatoria");
        if (r.precioUnitario() == null || r.precioUnitario().signum() <= 0)
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        if (r.numeroFactura() == null || r.numeroFactura().isBlank())
            throw new IllegalArgumentException("El número de factura es obligatorio");
        if (r.metodoPago() == null) throw new IllegalArgumentException("El método de pago es obligatorio");
        String moneda = cfg.monedaDeFilial(r.filial());
        MetodoPago pago = MetodoPago.valueOf(r.metodoPago().trim().toUpperCase());
        p.descontarStock(r.cantidad());                                   // actualización automática de stock
        BigDecimal totalLocal = r.precioUnitario().multiply(BigDecimal.valueOf(r.cantidad()));
        BigDecimal totalEur = cfg.convertir(totalLocal, moneda);          // conversión a moneda corporativa
        Venta v = new Venta(r.numeroFactura(), r.fecha(), p.getCodigo(), r.cantidad(), r.precioUnitario(),
                r.filial().trim(), moneda, r.cliente(), r.vendedor(), pago, totalEur);
        ventas.guardar(v);
        return v;
    }

    public List<Venta> listar() { return ventas.todas(); }
}
