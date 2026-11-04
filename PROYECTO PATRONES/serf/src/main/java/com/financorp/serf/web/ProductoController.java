package com.financorp.serf.web;

import com.financorp.serf.model.Producto;
import com.financorp.serf.model.Requests.ProductoRequest;
import com.financorp.serf.service.ProductoService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService service;
    public ProductoController(ProductoService service) { this.service = service; }

    @GetMapping public List<Producto> listar() { return service.listar(); }
    @PostMapping public Producto registrar(@RequestBody ProductoRequest r) { return service.registrar(r); }
}
