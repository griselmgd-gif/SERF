# SERF – Sistema Empresarial de Gestión de Reportes Financieros (FinanCorp S.A.)

Aplicación web en **Java 17 + Spring Boot 3** (API REST + interfaz web estática). Datos en memoria.

## Requisitos
- JDK 17 o superior (`java -version`)
- Maven 3.9+ (`mvn -version`)

## Instalar, ejecutar y probar
```bash
mvn test                  # pruebas unitarias (JUnit 5)
mvn spring-boot:run       # inicia en http://localhost:8080
# o bien:  mvn package && java -jar target/serf-1.0.0.jar
```
Abrir http://localhost:8080 . Al iniciar se cargan datos demo (4 productos, 5 ventas en 5 filiales);
para desactivarlos: `serf.demo=false` en `src/main/resources/application.properties`.

## Uso
1. **Productos**: registra un lote importado; el costo en CNY (u otra moneda) se convierte a EUR.
2. **Ventas**: registra la venta de una filial en su moneda local; baja el stock y se convierte a EUR.
3. **Reportes**: elige tipo, año y mes; marca/desmarca marca de agua y firma digital; "Generar reporte".

Nueva filial sin cambiar código:
```bash
curl -X POST localhost:8080/api/filiales -H "Content-Type: application/json" \
  -d '{"nombre":"Chile","moneda":"CLP","tasaAEur":0.001}'
```

## Patrones de diseño
| Patrón | Clase(s) | Uso |
|---|---|---|
| Singleton | `config/ConfiguracionGlobal` | Moneda corporativa, tasas, filiales, formato, firmante |
| Prototype | `reporte/Reporte.copiar()`, `PlantillaRegistro` | Plantillas base clonadas (copia profunda) por tipo de reporte |
| Builder | `reporte/ReporteBuilder` | Arma el reporte paso a paso con API fluida |
| Composite | `ComponenteReporte`, `SeccionReporte`, `TablaReporte`, `GraficoReporte`, `TextoReporte` | Secciones y subsecciones |
| Decorator | `MarcaAguaDecorator`, `FirmaDigitalDecorator` | Funciones extra sin modificar `Reporte` |
| Facade | `reporte/ReporteFacade.generar(...)` | Un solo método para el reporte completo |

SOLID: SRP (`ConsolidadorDatos`, servicios, repositorios), OCP (nuevos decoradores/plantillas/filiales sin tocar lo existente),
LSP/ISP (`ComponenteReporte`, `DocumentoReporte`), DIP (servicios dependen de `ProductoRepositorio`/`VentaRepositorio`).

## Estructura
```
src/main/java/com/financorp/serf/{config,model,repository,service,reporte,web}
src/main/resources/static/index.html
src/test/java/com/financorp/serf
docs/   (colocar aquí UML de StarUML, enlace Figma y PDF de sustentación)
```
## API
`GET/POST /api/productos` · `GET/POST /api/ventas` · `GET /api/config` · `POST /api/filiales` ·
`GET /api/reportes?tipo=MENSUAL|TRIMESTRAL|ANUAL&anio=2026&mes=11&marca=true&firma=true`
