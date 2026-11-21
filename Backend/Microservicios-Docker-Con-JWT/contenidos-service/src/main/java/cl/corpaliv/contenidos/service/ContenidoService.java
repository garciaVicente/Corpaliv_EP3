package cl.corpaliv.contenidos.service;

import java.util.List;
import org.springframework.stereotype.Service;
import cl.corpaliv.contenidos.model.*;
import cl.corpaliv.contenidos.repository.*;

@Service
public class ContenidoService {
    private final ProgramaRepository programas;
    private final HistoriaImpactoRepository historias;
    private final IndicadorImpactoRepository indicadores;
    private final CategoriaRepository categorias;
    private final ProductoServicioRepository productos;

    public ContenidoService(ProgramaRepository programas, HistoriaImpactoRepository historias,
            IndicadorImpactoRepository indicadores, CategoriaRepository categorias,
            ProductoServicioRepository productos) {
        this.programas = programas;
        this.historias = historias;
        this.indicadores = indicadores;
        this.categorias = categorias;
        this.productos = productos;
    }

    public List<Programa> listarProgramas() { return programas.findByPublicadoTrue(); }
    public List<HistoriaImpacto> listarHistorias() { return historias.findByAutorizadaTrueAndAnonimizadaTrueAndPublicadaTrue(); }
    public List<IndicadorImpacto> listarIndicadores() { return indicadores.findByPublicadoTrue(); }
    public List<Categoria> listarCategorias() { return categorias.findAll(); }
    public List<ProductoServicio> listarProductos() { return productos.findByDisponibleTrueAndPublicadoTrue(); }
}
