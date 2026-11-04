package com.financorp.serf.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/** PATRÓN SINGLETON (holder idiom: perezoso, thread-safe, constructor privado). */
public final class ConfiguracionGlobal {

    private static final class Holder {
        private static final ConfiguracionGlobal INSTANCIA = new ConfiguracionGlobal();
    }

    public static ConfiguracionGlobal getInstance() {
        return Holder.INSTANCIA;
    }

    private final String monedaCorporativa = "EUR";
    private final Map<String, BigDecimal> tasasAEur = new ConcurrentHashMap<>();
    private final Map<String, String> filiales = new ConcurrentHashMap<>();
    private volatile String marcaAgua = "CONFIDENCIAL - FINANCORP";
    private volatile String firmante = "Dirección Financiera";

    private ConfiguracionGlobal() {
        // Tasas de ejemplo (editables): 1 unidad de moneda = X EUR
        registrarTasa("EUR", new BigDecimal("1"));
        registrarTasa("CNY", new BigDecimal("0.13"));
        registrarTasa("PEN", new BigDecimal("0.25"));
        registrarTasa("USD", new BigDecimal("0.92"));
        registrarTasa("MXN", new BigDecimal("0.05"));
        registrarTasa("COP", new BigDecimal("0.00022"));
        registrarFilial("Perú", "PEN");
        registrarFilial("México", "MXN");
        registrarFilial("Colombia", "COP");
        registrarFilial("España", "EUR");
        registrarFilial("Alemania", "EUR");
    }

    public void registrarTasa(String moneda, BigDecimal tasa) {
        if (moneda == null || moneda.isBlank() || tasa == null || tasa.signum() <= 0)
            throw new IllegalArgumentException("Moneda y tasa positiva son obligatorias");
        tasasAEur.put(moneda.trim().toUpperCase(), tasa);
    }

    /** Escalabilidad: agregar una filial nueva no requiere tocar el resto del sistema. */
    public void registrarFilial(String nombre, String moneda) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre de filial obligatorio");
        if (moneda == null || !tasasAEur.containsKey(moneda.trim().toUpperCase()))
            throw new IllegalArgumentException("Registre primero la tasa de la moneda " + moneda);
        filiales.put(nombre.trim(), moneda.trim().toUpperCase());
    }

    public String monedaDeFilial(String filial) {
        String m = filial == null ? null : filiales.get(filial.trim());
        if (m == null) throw new IllegalArgumentException("Filial no registrada: " + filial);
        return m;
    }

    public BigDecimal convertir(BigDecimal monto, String moneda) {
        if (monto == null || moneda == null) throw new IllegalArgumentException("Monto y moneda son obligatorios");
        BigDecimal tasa = tasasAEur.get(moneda.trim().toUpperCase());
        if (tasa == null) throw new IllegalArgumentException("Moneda sin tasa configurada: " + moneda);
        return monto.multiply(tasa).setScale(2, RoundingMode.HALF_UP);
    }

    public String formatear(BigDecimal monto) {
        return String.format(Locale.US, "%,.2f %s", monto, monedaCorporativa);
    }

    public String getMonedaCorporativa() { return monedaCorporativa; }
    public Map<String, String> getFiliales() { return new TreeMap<>(filiales); }
    public String getMarcaAgua() { return marcaAgua; }
    public String getFirmante() { return firmante; }
}
