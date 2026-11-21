package cl.corpaliv.vinculacion.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "historial_solicitud")
@Getter
@Setter
@NoArgsConstructor
public class HistorialSolicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "solicitud_id", nullable = false)
    private Long solicitudId;
    @Column(name = "estado_anterior", length = 30)
    private String estadoAnterior;
    @Column(name = "estado_nuevo", nullable = false, length = 30)
    private String estadoNuevo;
    @Column(name = "observacion_interna", columnDefinition = "TEXT")
    private String observacionInterna;
    @Column(name = "usuario_gestor", length = 120)
    private String usuarioGestor;
    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @PrePersist
    void antesDeGuardar() { fechaCambio = LocalDateTime.now(); }
}
