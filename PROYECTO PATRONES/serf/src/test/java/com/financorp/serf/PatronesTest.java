package com.financorp.serf;

import static org.junit.jupiter.api.Assertions.*;

import com.financorp.serf.config.ConfiguracionGlobal;
import com.financorp.serf.reporte.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PatronesTest {

    @Test void singletonDevuelveSiempreLaMismaInstancia() {
        assertSame(ConfiguracionGlobal.getInstance(), ConfiguracionGlobal.getInstance());
    }

    @Test void convierteCnyAEur() {
        assertEquals(new BigDecimal("130.00"), ConfiguracionGlobal.getInstance().convertir(new BigDecimal("1000"), "CNY"));
    }

    @Test void prototypeClonaEnProfundidad() {
        var registro = new PlantillaRegistro();
        Reporte a = registro.clonar(TipoReporte.MENSUAL);
        a.getRaiz().buscar("Ingresos por país").orElseThrow().agregar(new TextoReporte("SOLO-EN-A"));
        Reporte b = registro.clonar(TipoReporte.MENSUAL);
        assertTrue(a.contenido().contains("SOLO-EN-A"));
        assertFalse(b.contenido().contains("SOLO-EN-A"));
    }

    @Test void compositeAnidaSinLimite() {
        var s = new SeccionReporte("A").agregar(new SeccionReporte("B").agregar(new SeccionReporte("C")));
        assertTrue(s.buscar("C").isPresent());
        assertTrue(s.renderizar(2).contains("<h4>C</h4>"));
    }

    @Test void builderArmaReporteConSeccionesYConclusiones() {
        Reporte r = new ReporteBuilder(new PlantillaRegistro().clonar(TipoReporte.ANUAL))
                .periodo("2026").moneda("EUR").conclusion("OK").build();
        String html = r.contenido();
        assertTrue(html.contains("Resultado consolidado"));
        assertTrue(html.contains("<li>OK</li>"));
    }

    @Test void decoratorAnadeMarcaYFirmaSinModificarElOriginal() {
        Reporte r = new PlantillaRegistro().clonar(TipoReporte.MENSUAL);
        String html = new MarcaAguaDecorator(new FirmaDigitalDecorator(r, "CFO"), "CONFIDENCIAL").contenido();
        assertTrue(html.contains("CONFIDENCIAL"));
        assertTrue(html.contains("SHA-256"));
        assertFalse(r.contenido().contains("SHA-256"));
    }
}
