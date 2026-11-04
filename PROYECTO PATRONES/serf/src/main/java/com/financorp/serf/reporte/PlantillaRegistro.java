package com.financorp.serf.reporte;

import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/** PATRÓN PROTOTYPE: registro de plantillas base; cada solicitud recibe un clon, nunca la plantilla. */
@Component
public class PlantillaRegistro {
    private final Map<TipoReporte, Reporte> prototipos = new EnumMap<>(TipoReporte.class);

    public PlantillaRegistro() {
        for (TipoReporte t : TipoReporte.values()) prototipos.put(t, crear(t));
    }

    private Reporte crear(TipoReporte t) {
        SeccionReporte raiz = new SeccionReporte("");
        raiz.agregar(new SeccionReporte("Ingresos")
                .agregar(new SeccionReporte("Ingresos por país"))
                .agregar(new SeccionReporte("Ingresos por categoría de producto")));
        if (t != TipoReporte.MENSUAL)
            raiz.agregar(new SeccionReporte("Gastos").agregar(new SeccionReporte("Gastos de importación")));
        if (t == TipoReporte.ANUAL)
            raiz.agregar(new SeccionReporte("Resultado consolidado"));
        return new Reporte(t.getTitulo(), raiz);
    }

    public Reporte clonar(TipoReporte t) { return prototipos.get(t).copiar(); }
}
