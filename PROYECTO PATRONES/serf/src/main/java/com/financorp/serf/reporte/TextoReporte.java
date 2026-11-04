package com.financorp.serf.reporte;

public class TextoReporte implements ComponenteReporte {
    private final String texto;
    public TextoReporte(String texto) { this.texto = texto; }
    public String renderizar(int nivel) { return "<p>" + ComponenteReporte.esc(texto) + "</p>"; }
    public ComponenteReporte copiar() { return new TextoReporte(texto); }
}
