package com.financorp.serf.reporte;

import com.financorp.serf.model.Producto;
import com.financorp.serf.model.Venta;
import com.financorp.serf.repository.ProductoRepositorio;
import com.financorp.serf.repository.VentaRepositorio;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

/** Única responsabilidad: consolidar datos de todas las filiales (en EUR) para un periodo. */
@Component
public class ConsolidadorDatos {
    private final VentaRepositorio ventas;
    private final ProductoRepositorio productos;

    public ConsolidadorDatos(VentaRepositorio ventas, ProductoRepositorio productos) {
        this.ventas = ventas;
        this.productos = productos;
    }

    private Stream<Venta> ventasDe(LocalDate d, LocalDate h) {
        return ventas.todas().stream().filter(v -> !v.getFecha().isBefore(d) && !v.getFecha().isAfter(h));
    }

    private static <T> Map<String, BigDecimal> sumar(Stream<T> s, Function<T, String> clave, Function<T, BigDecimal> valor) {
        return s.collect(Collectors.groupingBy(clave, TreeMap::new,
                Collectors.reducing(BigDecimal.ZERO, valor, BigDecimal::add)));
    }

    public Map<String, BigDecimal> ingresosPorPais(LocalDate d, LocalDate h) {
        return sumar(ventasDe(d, h), Venta::getFilial, Venta::getTotalEur);
    }

    public Map<String, BigDecimal> ingresosPorCategoria(LocalDate d, LocalDate h) {
        return sumar(ventasDe(d, h),
                v -> productos.porCodigo(v.getCodigoProducto()).map(p -> p.getCategoria().name()).orElse("SIN CATEGORÍA"),
                Venta::getTotalEur);
    }

    /** Gasto de importación = costo unitario (EUR) x stock inicial de los lotes importados en el periodo. */
    public Map<String, BigDecimal> gastosImportacion(LocalDate d, LocalDate h) {
        return sumar(productos.todos().stream()
                        .filter(p -> !p.getFechaImportacion().isBefore(d) && !p.getFechaImportacion().isAfter(h)),
                p -> p.getProveedor() == null ? "Sin proveedor" : p.getProveedor(),
                (Producto p) -> p.getCostoImportacionEur().multiply(BigDecimal.valueOf(p.getStockInicial())));
    }
}
