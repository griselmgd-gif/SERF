package com.financorp.serf.reporte;

public class MarcaAguaDecorator extends DocumentoDecorator {
    private final String texto;

    public MarcaAguaDecorator(DocumentoReporte base, String texto) { super(base); this.texto = texto; }

    public String contenido() {
        return "<div style=\"position:relative;overflow:hidden\">" + base.contenido()
             + "<div style=\"position:absolute;inset:0;display:flex;align-items:center;justify-content:center;"
             + "pointer-events:none;font:bold 44px Arial,sans-serif;color:rgba(124,58,237,.13);"
             + "transform:rotate(-25deg);text-align:center\">" + ComponenteReporte.esc(texto) + "</div></div>";
    }
}
