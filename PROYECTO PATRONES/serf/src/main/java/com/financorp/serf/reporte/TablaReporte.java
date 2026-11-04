package com.financorp.serf.reporte;

import com.financorp.serf.config.ConfiguracionGlobal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TablaReporte implements ComponenteReporte {
    private final List<String> cabecera;
    private final List<List<String>> filas;

    public TablaReporte(List<String> cabecera, List<List<String>> filas) {
        this.cabecera = new ArrayList<>(cabecera);
        this.filas = new ArrayList<>();
        for (List<String> f : filas) this.filas.add(new ArrayList<>(f));
    }

    /** Construye tabla "clave | monto" con fila TOTAL, formateada con ConfiguracionGlobal. */
    public static TablaReporte desdeMapa(String col1, String col2, Map<String, BigDecimal> datos) {
        var cfg = ConfiguracionGlobal.getInstance();
        List<List<String>> filas = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (var e : datos.entrySet()) {
            filas.add(List.of(e.getKey(), cfg.formatear(e.getValue())));
            total = total.add(e.getValue());
        }
        filas.add(List.of("TOTAL", cfg.formatear(total)));
        return new TablaReporte(List.of(col1, col2), filas);
    }

    public String renderizar(int nivel) {
        String celda = "border:1px solid #bbb;padding:6px 10px;text-align:left";
        StringBuilder sb = new StringBuilder("<table style=\"border-collapse:collapse;width:100%;margin:8px 0\"><tr>");
        for (String c : cabecera) sb.append("<th style=\"").append(celda).append(";background:#eee\">").append(ComponenteReporte.esc(c)).append("</th>");
        sb.append("</tr>");
        for (List<String> f : filas) {
            sb.append("<tr>");
            for (String c : f) sb.append("<td style=\"").append(celda).append("\">").append(ComponenteReporte.esc(c)).append("</td>");
            sb.append("</tr>");
        }
        return sb.append("</table>").toString();
    }

    public ComponenteReporte copiar() { return new TablaReporte(cabecera, filas); }
}
