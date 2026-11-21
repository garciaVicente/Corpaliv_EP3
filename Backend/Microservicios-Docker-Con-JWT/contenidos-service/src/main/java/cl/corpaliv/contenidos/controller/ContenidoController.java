package cl.corpaliv.contenidos.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import cl.corpaliv.contenidos.model.*;
import cl.corpaliv.contenidos.service.ContenidoService;

@RestController
@RequestMapping("/contenidos")
public class ContenidoController {
    private final ContenidoService service;

    public ContenidoController(ContenidoService service) { this.service = service; }

    @GetMapping("/programas")
    public List<Programa> programas() { return service.listarProgramas(); }

    @GetMapping("/historias")
    public List<HistoriaImpacto> historias() { return service.listarHistorias(); }

    @GetMapping("/indicadores")
    public List<IndicadorImpacto> indicadores() { return service.listarIndicadores(); }

    @GetMapping("/categorias")
    public List<Categoria> categorias() { return service.listarCategorias(); }

    @GetMapping("/productos")
    public List<ProductoServicio> productos() { return service.listarProductos(); }
}
