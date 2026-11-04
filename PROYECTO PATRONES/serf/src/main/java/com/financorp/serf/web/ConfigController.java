package com.financorp.serf.web;

import com.financorp.serf.config.ConfiguracionGlobal;
import com.financorp.serf.model.Categoria;
import com.financorp.serf.model.MetodoPago;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ConfigController {
    public record FilialRequest(String nombre, String moneda, BigDecimal tasaAEur) {}

    @GetMapping("/config")
    public Map<String, Object> config() {
        var c = ConfiguracionGlobal.getInstance();
        return Map.of("monedaCorporativa", c.getMonedaCorporativa(), "filiales", c.getFiliales(),
                "categorias", Categoria.values(), "metodosPago", MetodoPago.values());
    }

    /** Alta de una nueva filial (y, si hace falta, de su moneda) sin tocar el código. */
    @PostMapping("/filiales")
    public Map<String, String> nuevaFilial(@RequestBody FilialRequest r) {
        var c = ConfiguracionGlobal.getInstance();
        if (r.tasaAEur() != null) c.registrarTasa(r.moneda(), r.tasaAEur());
        c.registrarFilial(r.nombre(), r.moneda());
        return c.getFiliales();
    }
}
