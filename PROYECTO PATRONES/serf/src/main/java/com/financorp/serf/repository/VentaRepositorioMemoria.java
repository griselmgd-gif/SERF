package com.financorp.serf.repository;

import com.financorp.serf.model.Venta;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Repository;

@Repository
public class VentaRepositorioMemoria implements VentaRepositorio {
    private final List<Venta> datos = new CopyOnWriteArrayList<>();
    public void guardar(Venta v) { datos.add(v); }
    public List<Venta> todas() { return List.copyOf(datos); }
}
