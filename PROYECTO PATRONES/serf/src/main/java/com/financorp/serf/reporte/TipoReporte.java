package com.financorp.serf.reporte;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public enum TipoReporte {
    MENSUAL("Reporte Mensual Consolidado de Ingresos"),
    TRIMESTRAL("Reporte Trimestral de Gastos e Ingresos"),
    ANUAL("Reporte Anual Consolidado");

    private final String titulo;
    TipoReporte(String titulo) { this.titulo = titulo; }
    public String getTitulo() { return titulo; }

    /** Rango [desde, hasta] del periodo; "mes" (1-12) define el mes o el trimestre que lo contiene. */
    public LocalDate[] rango(int anio, int mes) {
        return switch (this) {
            case MENSUAL -> {
                LocalDate d = LocalDate.of(anio, mes, 1);
                yield new LocalDate[]{d, d.with(TemporalAdjusters.lastDayOfMonth())};
            }
            case TRIMESTRAL -> {
                LocalDate d = LocalDate.of(anio, ((mes - 1) / 3) * 3 + 1, 1);
                yield new LocalDate[]{d, d.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth())};
            }
            case ANUAL -> new LocalDate[]{LocalDate.of(anio, 1, 1), LocalDate.of(anio, 12, 31)};
        };
    }
}
