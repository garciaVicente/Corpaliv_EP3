package cl.corpaliv.contenidos.model;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
@Entity @Table(name="programa") @Getter @Setter @NoArgsConstructor
public class Programa {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=120) private String nombre;
 private String resumen;
 @Column(columnDefinition="TEXT") private String descripcion;
 @Column(length=100) private String categoria;
 @Column(name="imagen_url",length=500) private String imagenUrl;
 @Column(nullable=false) private Boolean publicado=true;
 @Column(name="fecha_creacion") private LocalDateTime fechaCreacion;
 @Column(name="fecha_actualizacion") private LocalDateTime fechaActualizacion;
 @PrePersist void crearFechas(){ fechaCreacion=LocalDateTime.now(); fechaActualizacion=fechaCreacion; }
 @PreUpdate void actualizarFecha(){ fechaActualizacion=LocalDateTime.now(); }
}
