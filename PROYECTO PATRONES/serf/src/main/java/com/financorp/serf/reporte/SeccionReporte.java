package com.financorp.serf.reporte;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Nodo compuesto: contiene hojas u otras secciones (anidamiento ilimitado). */
public class SeccionReporte implements ComponenteReporte {
    private final String titulo;
    private final List<ComponenteReporte> hijos = new ArrayList<>();

    public SeccionReporte(String titulo) { this.titulo = titulo; }

    public SeccionReporte agregar(ComponenteReporte c) { hijos.add(c); return this; }

    public Optional<SeccionReporte> buscar(String t) {
        if (titulo.equals(t)) return Optional.of(this);
        for (ComponenteReporte h : hijos)
            if (h instanceof SeccionReporte s) {
                Optional<SeccionReporte> r = s.buscar(t);
                if (r.isPresent()) return r;
            }
        return Optional.empty();
    }

    public String renderizar(int nivel) {
        StringBuilder sb = new StringBuilder();
        boolean raiz = titulo.isEmpty();
        if (!raiz) sb.append("<h").append(Math.min(nivel, 6)).append(">").append(ComponenteReporte.esc(titulo))
                     .append("</h").append(Math.min(nivel, 6)).append(">");
        for (ComponenteReporte h : hijos) sb.append(h.renderizar(raiz ? nivel : nivel + 1));
        return sb.toString();
    }

    public ComponenteReporte copiar() {
        SeccionReporte s = new SeccionReporte(titulo);
        for (ComponenteReporte h : hijos) s.agregar(h.copiar());
        return s;
    }
}
