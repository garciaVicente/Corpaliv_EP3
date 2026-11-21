package cl.corpaliv.vinculacion.service;

import java.util.List;
import java.util.Set;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import cl.corpaliv.vinculacion.dto.EstadoSolicitudRequest;
import cl.corpaliv.vinculacion.dto.EstadoPublicoResponse;
import cl.corpaliv.vinculacion.dto.SolicitudRequest;
import cl.corpaliv.vinculacion.model.HistorialSolicitud;
import cl.corpaliv.vinculacion.model.SolicitudColaboracion;
import cl.corpaliv.vinculacion.model.TipoColaboracion;
import cl.corpaliv.vinculacion.repository.HistorialSolicitudRepository;
import cl.corpaliv.vinculacion.repository.SolicitudRepository;
import cl.corpaliv.vinculacion.repository.TipoColaboracionRepository;

@Service
public class SolicitudService {
    private static final Set<String> ESTADOS = Set.of(
        "RECIBIDA", "EN_REVISION", "CONTACTADO", "CONCRETADO", "DESCARTADO");

    private final SolicitudRepository solicitudes;
    private final HistorialSolicitudRepository historial;
    private final TipoColaboracionRepository tipos;

    public SolicitudService(SolicitudRepository solicitudes, HistorialSolicitudRepository historial,
            TipoColaboracionRepository tipos) {
        this.solicitudes = solicitudes;
        this.historial = historial;
        this.tipos = tipos;
    }

    @Transactional
    public SolicitudColaboracion crear(SolicitudRequest request) {
        // El correo es obligatorio; el telefono es un medio de contacto complementario.
        boolean tieneTelefono = request.telefono() != null && !request.telefono().isBlank();
        if (Boolean.FALSE.equals(request.aceptaPrivacidad()) || request.aceptaPrivacidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe aceptar la politica de privacidad");
        }

        String codigoTipo = normalizarTipo(request.tipoColaboracion());
        TipoColaboracion tipo = tipos.findByCodigoAndActivoTrue(codigoTipo)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de colaboracion no permitido"));

        if (tieneTelefono) {
            long digitos = request.telefono().chars().filter(Character::isDigit).count();
            if (!request.telefono().matches("^[0-9+() -]{7,20}$") || digitos < 7 || digitos > 15) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El telefono no tiene un formato valido");
            }
        }

        SolicitudColaboracion solicitud = new SolicitudColaboracion();
        solicitud.setNombreContacto(request.nombreContacto().trim());
        solicitud.setOrganizacion(request.organizacion());
        solicitud.setEmail(request.email().trim());
        solicitud.setTelefono(tieneTelefono ? request.telefono().trim() : null);
        solicitud.setTipoColaboracionId(tipo.getId());
        solicitud.setProgramaId(request.programaId());
        solicitud.setProductoServicioId(request.productoServicioId());
        solicitud.setMensaje(request.mensaje());
        solicitud.setAceptaPrivacidad(true);
        solicitud.setEstado("RECIBIDA");
        SolicitudColaboracion guardada = solicitudes.save(solicitud);

        registrarHistorial(guardada.getId(), null, "RECIBIDA", "Solicitud creada desde la aplicacion", "SISTEMA");
        return guardada;
    }

    public List<SolicitudColaboracion> listar() { return solicitudes.findAll(); }

    public EstadoPublicoResponse consultarEstadoPublico(Long id) {
        SolicitudColaboracion solicitud = obtener(id);
        return new EstadoPublicoResponse(
            solicitud.getId(), solicitud.getEstado(),
            solicitud.getFechaCreacion(), solicitud.getFechaActualizacion());
    }

    public SolicitudColaboracion obtener(Long id) {
        return solicitudes.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));
    }

    @Transactional
    public SolicitudColaboracion cambiarEstado(Long id, EstadoSolicitudRequest request) {
        String nuevoEstado = request.estado() == null ? "" : request.estado().trim().toUpperCase(Locale.ROOT);
        if (!ESTADOS.contains(nuevoEstado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no permitido");
        }
        SolicitudColaboracion solicitud = obtener(id);
        String anterior = solicitud.getEstado();
        solicitud.setEstado(nuevoEstado);
        SolicitudColaboracion actualizada = solicitudes.save(solicitud);
        registrarHistorial(id, anterior, nuevoEstado, request.observacion(), "GESTOR");
        return actualizada;
    }

    public List<HistorialSolicitud> historial(Long id) {
        obtener(id);
        return historial.findBySolicitudIdOrderByFechaCambioDesc(id);
    }


    public List<TipoColaboracion> listarTiposActivos() {
        return tipos.findByActivoTrueOrderByNombre();
    }

    private String normalizarTipo(String tipo) {
        String valor = tipo.trim().toLowerCase();
        return switch (valor) {
            case "donaciones", "donacion" -> "DONACION";
            case "alianzas con empresas", "alianza_rse" -> "ALIANZA_RSE";
            case "voluntariado" -> "VOLUNTARIADO";
            case "productos y servicios", "compra" -> "COMPRA";
            case "capacitaciones", "capacitacion" -> "CAPACITACION";
            default -> tipo.trim().toUpperCase();
        };
    }

    private void registrarHistorial(Long id, String anterior, String nuevo, String observacion, String usuario) {
        HistorialSolicitud registro = new HistorialSolicitud();
        registro.setSolicitudId(id);
        registro.setEstadoAnterior(anterior);
        registro.setEstadoNuevo(nuevo);
        registro.setObservacionInterna(observacion);
        registro.setUsuarioGestor(usuario);
        historial.save(registro);
    }
}
