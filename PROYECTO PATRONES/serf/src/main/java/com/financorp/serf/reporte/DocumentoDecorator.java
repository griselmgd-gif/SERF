package com.financorp.serf.reporte;

/** PATRÓN DECORATOR: añade características sin modificar la clase Reporte. */
public abstract class DocumentoDecorator implements DocumentoReporte {
    protected final DocumentoReporte base;
    protected DocumentoDecorator(DocumentoReporte base) { this.base = base; }
}
