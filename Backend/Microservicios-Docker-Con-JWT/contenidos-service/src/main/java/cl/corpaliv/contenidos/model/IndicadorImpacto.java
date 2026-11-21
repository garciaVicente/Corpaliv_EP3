package cl.corpaliv.contenidos.model;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
@Entity @Table(name="indicador_impacto") @Getter @Setter @NoArgsConstructor
public class IndicadorImpacto {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=120) private String nombre;
 @Column(columnDefinition="TEXT") private String descripcion;
 @Column(precision=12, scale=2) private BigDecimal valor;
 @Column(length=60) private String unidad;
 @Column(length=60) private String periodo;
 @Column(length=255) private String fuente;
 @Column(name="fecha_actualizacion") private LocalDateTime fechaActualizacion;
 @Column(nullable=false) private Boolean publicado=false;
}
