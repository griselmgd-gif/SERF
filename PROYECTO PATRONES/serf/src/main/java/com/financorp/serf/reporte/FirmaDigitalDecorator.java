package com.financorp.serf.reporte;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;

/** Firma = huella SHA-256 del contenido + firmante + fecha (verificable ante alteraciones). */
public class FirmaDigitalDecorator extends DocumentoDecorator {
    private final String firmante;

    public FirmaDigitalDecorator(DocumentoReporte base, String firmante) { super(base); this.firmante = firmante; }

    public String contenido() {
        String cuerpo = base.contenido();
        return cuerpo + "<div style=\"font-family:monospace;font-size:12px;border-top:1px solid #999;margin-top:16px;"
             + "padding:8px 24px;color:#333;background:#fff\">Firmado digitalmente por: "
             + ComponenteReporte.esc(firmante) + " | " + LocalDateTime.now().withNano(0)
             + "<br>SHA-256: " + sha256(cuerpo) + "</div>";
    }

    private static String sha256(String s) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
