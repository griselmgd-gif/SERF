package com.financorp.serf.reporte;

import java.util.ArrayList;
import java.util.List;

/** Producto del Builder, prototipo clonable (copia profunda) y componente concreto del Decorator. */
public class Reporte implements DocumentoReporte {
    private final String titulo;
    private final SeccionReporte raiz;
    private final List<String> conclusiones = new ArrayList<>();
    private String periodo = "";
    private String moneda = "";

    public Reporte(String titulo, SeccionReporte raiz) { this.titulo = titulo; this.raiz = raiz; }

    /** PATRÓN PROTOTYPE: copia independiente del original. */
    public Reporte copiar() {
        Reporte r = new Reporte(titulo, (SeccionReporte) raiz.copiar());
        r.periodo = periodo; r.moneda = moneda; r.conclusiones.addAll(conclusiones);
        return r;
    }

    public SeccionReporte getRaiz() { return raiz; }
    public void setPeriodo(String p) { this.periodo = p; }
    public void setMoneda(String m) { this.moneda = m; }
    public void agregarConclusion(String c) { conclusiones.add(c); }

    public String contenido() {
        StringBuilder sb = new StringBuilder("<div style=\"font-family:Arial,sans-serif;background:#fff;color:#111;padding:24px\">");
        sb.append("<h1>").append(ComponenteReporte.esc(titulo)).append("</h1>");
        sb.append("<p><b>Periodo:</b> ").append(ComponenteReporte.esc(periodo))
          .append(" &nbsp;|&nbsp; <b>Moneda corporativa:</b> ").append(ComponenteReporte.esc(moneda)).append("</p>");
        sb.append(raiz.renderizar(2));
        if (!conclusiones.isEmpty()) {
            sb.append("<h2>Conclusiones</h2><ul>");
            for (String c : conclusiones) sb.append("<li>").append(ComponenteReporte.esc(c)).append("</li>");
            sb.append("</ul>");
        }
        return sb.append("</div>").toString();
    }
}
