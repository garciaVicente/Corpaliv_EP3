package cl.corpaliv.vinculacion.dto;

import java.time.LocalDateTime;

/** Respuesta publica acotada: no expone nombre, correo, telefono ni mensaje. */
public record EstadoPublicoResponse(
    Long id,
    String estado,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) {}
