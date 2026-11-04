package com.financorp.serf.repository;

import com.financorp.serf.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepositorio {
    void guardar(Producto p);
    Optional<Producto> porCodigo(String codigo);
    List<Producto> todos();
}
