package com.financorp.serf;

import static org.junit.jupiter.api.Assertions.*;

import com.financorp.serf.model.Requests.ProductoRequest;
import com.financorp.serf.model.Requests.VentaRequest;
import com.financorp.serf.model.Venta;
import com.financorp.serf.repository.*;
import com.financorp.serf.reporte.*;
import com.financorp.serf.service.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServiciosTest {
    private final ProductoRepositorioMemoria pr = new ProductoRepositorioMemoria();
    private final VentaRepositorioMemoria vr = new VentaRepositorioMemoria();
    private final ProductoService productos = new ProductoService(pr);
    private final VentaService ventas = new VentaService(vr, pr);

    @BeforeEach void datos() {
        productos.registrar(new ProductoRequest("T-1", "Tel", "x", "SMARTPHONE", new BigDecimal("1000"), "CNY",
                new BigDecimal("1500"), "Shenzhen", LocalDate.now(), 100));
    }

    private VentaRequest venta(int cantidad) {
        return new VentaRequest("F-1", LocalDate.now(), "T-1", cantidad, new BigDecimal("1500"), "Perú", "C", "V", "TARJETA");
    }

    @Test void registrarProductoConvierteCostoAEur() {
        assertEquals(0, new BigDecimal("130.00").compareTo(pr.porCodigo("T-1").orElseThrow().getCostoImportacionEur()));
    }

    @Test void ventaDescuentaStockYConvierteAEur() {
        Venta v = ventas.registrar(venta(10));
        assertEquals(90, pr.porCodigo("T-1").orElseThrow().getStock());
        assertEquals(0, new BigDecimal("3750.00").compareTo(v.getTotalEur()));   // 15000 PEN * 0.25
    }

    @Test void ventaSinStockSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> ventas.registrar(venta(101)));
    }

    @Test void facadeGeneraReporteCompleto() {
        ventas.registrar(venta(10));
        var facade = new ReporteFacade(new ConsolidadorDatos(vr, pr), new PlantillaRegistro());
        LocalDate hoy = LocalDate.now();
        String html = facade.generar(TipoReporte.MENSUAL, hoy.getYear(), hoy.getMonthValue(), true, true);
        assertTrue(html.contains("Ingresos por país"));
        assertTrue(html.contains("3,750.00 EUR"));
        assertTrue(html.contains("SHA-256"));
    }
}
