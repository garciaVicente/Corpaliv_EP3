package cl.corpaliv.vinculacion.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SolicitudRequest(
    @NotBlank(message = "El nombre es obligatorio") String nombreContacto,
    String organizacion,
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato valido") String email,
    String telefono,
    @NotBlank(message = "Debe indicar el tipo de colaboracion") String tipoColaboracion,
    Long programaId,
    Long productoServicioId,
    @NotBlank(message = "El mensaje es obligatorio") String mensaje,
    @AssertTrue(message = "Debe aceptar la politica de privacidad") Boolean aceptaPrivacidad
) {}
