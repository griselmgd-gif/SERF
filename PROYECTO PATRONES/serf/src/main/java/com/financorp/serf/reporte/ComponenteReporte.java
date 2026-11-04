package com.financorp.serf.reporte;

/** PATRÓN COMPOSITE: hojas (texto, tabla, gráfico) y compuestos (sección) se tratan igual. */
public interface ComponenteReporte {
    String renderizar(int nivel);
    ComponenteReporte copiar();   // copia profunda (la usa Prototype)

    static String esc(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
