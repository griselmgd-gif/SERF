package com.financorp.serf.reporte;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/** Gráfico comparativo de barras (HTML/CSS, sin librerías externas). */
public class GraficoReporte implements ComponenteReporte {
    private final String titulo;
    private final Map<String, BigDecimal> datos;

    public GraficoReporte(String titulo, Map<String, BigDecimal> datos) {
        this.titulo = titulo;
        this.datos = new LinkedHashMap<>(datos);
    }

    public String renderizar(int nivel) {
        BigDecimal max = datos.values().stream().max(Comparator.naturalOrder()).orElse(BigDecimal.ONE);
        if (max.signum() == 0) max = BigDecimal.ONE;
        StringBuilder sb = new StringBuilder("<div style=\"margin:8px 0\"><b>" + ComponenteReporte.esc(titulo) + "</b>");
        for (var e : datos.entrySet()) {
            int pct = e.getValue().multiply(BigDecimal.valueOf(100)).divide(max, 0, RoundingMode.HALF_UP).intValue();
            sb.append("<div style=\"font-size:13px;margin-top:6px\">").append(ComponenteReporte.esc(e.getKey()))
              .append("<div style=\"background:#7c3aed;height:14px;width:").append(Math.max(pct, 1)).append("%\"></div></div>");
        }
        return sb.append("</div>").toString();
    }

    public ComponenteReporte copiar() { return new GraficoReporte(titulo, datos); }
}
