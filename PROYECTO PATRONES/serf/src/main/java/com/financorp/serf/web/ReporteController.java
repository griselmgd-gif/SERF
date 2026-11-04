package com.financorp.serf.web;

import com.financorp.serf.reporte.ReporteFacade;
import com.financorp.serf.reporte.TipoReporte;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReporteController {
    private final ReporteFacade facade;
    public ReporteController(ReporteFacade facade) { this.facade = facade; }

    @GetMapping("/api/reportes")
    public ResponseEntity<String> generar(@RequestParam TipoReporte tipo, @RequestParam int anio, @RequestParam int mes,
                                          @RequestParam(defaultValue = "true") boolean marca,
                                          @RequestParam(defaultValue = "true") boolean firma) {
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(facade.generar(tipo, anio, mes, marca, firma));
    }
}
