package cl.corpaliv.vinculacion.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.corpaliv.vinculacion.dto.EstadoSolicitudRequest;
import cl.corpaliv.vinculacion.dto.EstadoPublicoResponse;
import cl.corpaliv.vinculacion.dto.SolicitudRequest;
import cl.corpaliv.vinculacion.model.HistorialSolicitud;
import cl.corpaliv.vinculacion.model.SolicitudColaboracion;
import cl.corpaliv.vinculacion.model.TipoColaboracion;
import cl.corpaliv.vinculacion.service.SolicitudService;

@RestController
@RequestMapping("/vinculacion/solicitudes")
public class SolicitudController {
    private final SolicitudService service;

    public SolicitudController(SolicitudService service) { this.service = service; }

    @GetMapping("/tipos-colaboracion")
    public List<TipoColaboracion> tiposColaboracion() { return service.listarTiposActivos(); }

    @PostMapping
    public ResponseEntity<SolicitudColaboracion> crear(@Valid @RequestBody SolicitudRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping
    public List<SolicitudColaboracion> listar() { return service.listar(); }

    @GetMapping("/{id}/estado")
    public EstadoPublicoResponse consultarEstado(@PathVariable Long id) {
        return service.consultarEstadoPublico(id);
    }

    @GetMapping("/{id}")
    public SolicitudColaboracion obtener(@PathVariable Long id) { return service.obtener(id); }

    @PatchMapping("/{id}/estado")
    public SolicitudColaboracion cambiarEstado(@PathVariable Long id,
            @Valid @RequestBody EstadoSolicitudRequest request) {
        return service.cambiarEstado(id, request);
    }

    @GetMapping("/{id}/historial")
    public List<HistorialSolicitud> historial(@PathVariable Long id) { return service.historial(id); }
}
