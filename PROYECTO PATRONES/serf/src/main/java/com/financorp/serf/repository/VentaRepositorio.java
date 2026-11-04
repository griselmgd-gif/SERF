package com.financorp.serf.repository;

import com.financorp.serf.model.Venta;
import java.util.List;

public interface VentaRepositorio {
    void guardar(Venta v);
    List<Venta> todas();
}
