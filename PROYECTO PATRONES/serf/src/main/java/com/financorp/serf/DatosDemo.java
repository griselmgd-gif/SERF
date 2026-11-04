package com.financorp.serf;

import com.financorp.serf.model.Requests.ProductoRequest;
import com.financorp.serf.model.Requests.VentaRequest;
import com.financorp.serf.service.ProductoService;
import com.financorp.serf.service.VentaService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** Carga datos de ejemplo (flujo del enunciado). Desactivar con serf.demo=false. */
@Component
@ConditionalOnProperty(name = "serf.demo", havingValue = "true", matchIfMissing = true)
public class DatosDemo implements CommandLineRunner {
    private final ProductoService productos;
    private final VentaService ventas;

    public DatosDemo(ProductoService productos, VentaService ventas) {
        this.productos = productos;
        this.ventas = ventas;
    }

    @Override
    public void run(String... args) {
        LocalDate hoy = LocalDate.now();
        prod("SMX-100", "Smartphone Modelo X", "SMARTPHONE", "1800", "1500", "Shenzhen Tech Co.", 500, hoy);
        prod("LAP-200", "Laptop Pro 14", "LAPTOP", "4200", "3800", "Dongguan Compute", 120, hoy);
        prod("ACC-300", "Auriculares Bluetooth", "ACCESORIO", "80", "150", "Shenzhen Tech Co.", 1000, hoy);
        prod("NET-400", "Router WiFi 6", "REDES", "220", "300", "Huawei Partner Ltd.", 300, hoy);
        vta("F-0001", "SMX-100", 100, "1500", "Perú", "Retail Lima SAC", "TARJETA", hoy);
        vta("F-0002", "LAP-200", 20, "19000", "México", "Corp MX", "TRANSFERENCIA", hoy);
        vta("F-0003", "LAP-200", 15, "1000", "España", "Madrid Tech SL", "TRANSFERENCIA", hoy);
        vta("F-0004", "ACC-300", 50, "150000", "Colombia", "Bogotá Store", "EFECTIVO", hoy);
        vta("F-0005", "NET-400", 30, "70", "Alemania", "Berlin Net GmbH", "TARJETA", hoy);
    }

    private void prod(String cod, String nom, String cat, String costo, String precio, String prov, int stock, LocalDate f) {
        productos.registrar(new ProductoRequest(cod, nom, "Importado de China", cat, new BigDecimal(costo), "CNY",
                new BigDecimal(precio), prov, f, stock));
    }

    private void vta(String fac, String cod, int cant, String precio, String filial, String cli, String pago, LocalDate f) {
        ventas.registrar(new VentaRequest(fac, f, cod, cant, new BigDecimal(precio), filial, cli, "Vendedor demo", pago));
    }
}
