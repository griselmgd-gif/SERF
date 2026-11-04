package com.financorp.serf.web;

import com.financorp.serf.model.Requests.VentaRequest;
import com.financorp.serf.model.Venta;
import com.financorp.serf.service.VentaService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService service;
    public VentaController(VentaService service) { this.service = service; }

    @GetMapping public List<Venta> listar() { return service.listar(); }
    @PostMapping public Venta registrar(@RequestBody VentaRequest r) { return service.registrar(r); }
}
