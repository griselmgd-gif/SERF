package com.financorp.serf.reporte;

/**
 * PATRÓN BUILDER (API fluida): arma el reporte paso a paso sobre una plantilla clonada.
 * Si la plantilla no define una sección, el paso se omite (la plantilla decide el contenido).
 */
public class ReporteBuilder {
    private final Reporte reporte;

    public ReporteBuilder(Reporte plantillaClonada) { this.reporte = plantillaClonada; }

    public ReporteBuilder periodo(String p) { reporte.setPeriodo(p); return this; }
    public ReporteBuilder moneda(String m) { reporte.setMoneda(m); return this; }
    public ReporteBuilder tabla(String seccion, TablaReporte t) { return agregar(seccion, t); }
    public ReporteBuilder grafico(String seccion, GraficoReporte g) { return agregar(seccion, g); }
    public ReporteBuilder texto(String seccion, TextoReporte t) { return agregar(seccion, t); }
    public ReporteBuilder conclusion(String c) { reporte.agregarConclusion(c); return this; }
    public Reporte build() { return reporte; }

    private ReporteBuilder agregar(String seccion, ComponenteReporte c) {
        reporte.getRaiz().buscar(seccion).ifPresent(s -> s.agregar(c));
        return this;
    }
}
