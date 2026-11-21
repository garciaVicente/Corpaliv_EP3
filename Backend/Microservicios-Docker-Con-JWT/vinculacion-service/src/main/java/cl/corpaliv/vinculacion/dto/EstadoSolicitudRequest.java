package cl.corpaliv.vinculacion.dto;

import jakarta.validation.constraints.NotBlank;

public record EstadoSolicitudRequest(
    @NotBlank(message = "El estado es obligatorio") String estado,
    String observacion
) {}
