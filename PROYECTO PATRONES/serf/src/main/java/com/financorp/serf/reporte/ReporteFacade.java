package com.financorp.serf.reporte;

import com.financorp.serf.config.ConfiguracionGlobal;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * PATRÓN FACADE: un único método oculta Singleton + Prototype + Builder + Composite + Decorator.
 */
@Service
public class ReporteFacade {
    private final ConsolidadorDatos datos;
    private final PlantillaRegistro plantillas;

    public ReporteFacade(ConsolidadorDatos datos, PlantillaRegistro plantillas) {
        this.datos = datos;
        this.plantillas = plantillas;
    }

    public String generar(TipoReporte tipo, int anio, int mes, boolean marcaAgua, boolean firma) {
        ConfiguracionGlobal cfg = ConfiguracionGlobal.getInstance();                 // 1. Singleton
        LocalDate[] r = tipo.rango(anio, mes);
        Map<String, BigDecimal> porPais = datos.ingresosPorPais(r[0], r[1]);
        Map<String, BigDecimal> porCat = datos.ingresosPorCategoria(r[0], r[1]);
        Map<String, BigDecimal> gastos = datos.gastosImportacion(r[0], r[1]);
        BigDecimal ingresos = suma(porPais), egresos = suma(gastos);

        Reporte reporte = new ReporteBuilder(plantillas.clonar(tipo))                 // 2. Prototype
                .periodo(r[0] + " a " + r[1])                                         // 3. Builder
                .moneda(cfg.getMonedaCorporativa())
                .tabla("Ingresos por país", TablaReporte.desdeMapa("País", "Ingresos", porPais))   // 4. Composite
                .grafico("Ingresos por país", new GraficoReporte("Comparativo de ingresos por país", porPais))
                .tabla("Ingresos por categoría de producto", TablaReporte.desdeMapa("Categoría", "Ingresos", porCat))
                .tabla("Gastos de importación", TablaReporte.desdeMapa("Proveedor", "Gasto", gastos))
                .texto("Resultado consolidado", new TextoReporte("Ingresos " + cfg.formatear(ingresos)
                        + " - gastos de importación " + cfg.formatear(egresos)
                        + " = " + cfg.formatear(ingresos.subtract(egresos))))
                .conclusion("Ingresos totales del periodo: " + cfg.formatear(ingresos))
                .conclusion("Gastos de importación del periodo: " + cfg.formatear(egresos))
                .conclusion("Resultado: " + cfg.formatear(ingresos.subtract(egresos)))
                .build();

        DocumentoReporte doc = reporte;                                               // 5. Decorator
        if (firma) doc = new FirmaDigitalDecorator(doc, cfg.getFirmante());
        if (marcaAgua) doc = new MarcaAguaDecorator(doc, cfg.getMarcaAgua());
        return doc.contenido();
    }

    private static BigDecimal suma(Map<String, BigDecimal> m) {
        return m.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
