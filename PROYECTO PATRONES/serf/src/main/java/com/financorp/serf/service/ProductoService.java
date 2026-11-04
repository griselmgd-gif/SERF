package com.financorp.serf.service;

import com.financorp.serf.config.ConfiguracionGlobal;
import com.financorp.serf.model.*;
import com.financorp.serf.model.Requests.ProductoRequest;
import com.financorp.serf.repository.ProductoRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
    private final ProductoRepositorio repo;

    public ProductoService(ProductoRepositorio repo) { this.repo = repo; }

    public synchronized Producto registrar(ProductoRequest r) {
        if (r.codigo() == null || r.codigo().isBlank()) throw new IllegalArgumentException("El código es obligatorio");
        if (repo.porCodigo(r.codigo()).isPresent())
            throw new IllegalArgumentException("Ya existe un producto con código " + r.codigo());
        if (r.categoria() == null) throw new IllegalArgumentException("La categoría es obligatoria");
        Categoria cat = Categoria.valueOf(r.categoria().trim().toUpperCase());
        String moneda = (r.monedaOrigen() == null || r.monedaOrigen().isBlank()) ? "CNY" : r.monedaOrigen().trim().toUpperCase();
        var costoEur = ConfiguracionGlobal.getInstance().convertir(r.costoImportacion(), moneda);
        Producto p = new Producto(r.codigo(), r.nombre(), r.descripcionTecnica(), cat, r.costoImportacion(),
                moneda, costoEur, r.precioSugerido(), r.proveedor(), r.fechaImportacion(), r.stockInicial());
        repo.guardar(p);
        return p;
    }

    public List<Producto> listar() { return repo.todos(); }
}
