package com.financorp.serf.repository;

import com.financorp.serf.model.Producto;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class ProductoRepositorioMemoria implements ProductoRepositorio {
    private final Map<String, Producto> datos = new ConcurrentHashMap<>();
    public void guardar(Producto p) { datos.put(p.getCodigo(), p); }
    public Optional<Producto> porCodigo(String c) { return Optional.ofNullable(c == null ? null : datos.get(c.trim())); }
    public List<Producto> todos() {
        List<Producto> l = new ArrayList<>(datos.values());
        l.sort(Comparator.comparing(Producto::getCodigo));
        return l;
    }
}
