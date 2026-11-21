package cl.corpaliv.vinculacion.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solicitud_colaboracion")
@Getter
@Setter
@NoArgsConstructor
public class SolicitudColaboracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_contacto", nullable = false, length = 120)
    private String nombreContacto;
    @Column(length = 150)
    private String organizacion;
    @Column(length = 150)
    private String email;
    @Column(length = 40)
    private String telefono;
    @Column(name = "tipo_colaboracion_id", nullable = false)
    private Long tipoColaboracionId;
    @Column(name = "programa_id")
    private Long programaId;
    @Column(name = "producto_servicio_id")
    private Long productoServicioId;
    @Column(columnDefinition = "TEXT")
    private String mensaje;
    @Column(name = "acepta_privacidad", nullable = false)
    private Boolean aceptaPrivacidad;
    @Column(nullable = false, length = 30)
    private String estado;
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    void antesDeGuardar() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = fechaCreacion;
        if (estado == null || estado.isBlank()) estado = "RECIBIDA";
    }

    @PreUpdate
    void antesDeActualizar() { fechaActualizacion = LocalDateTime.now(); }
}
