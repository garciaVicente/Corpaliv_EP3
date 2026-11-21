package cl.corpaliv.contenidos.model;
import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
@Entity @Table(name="historia_impacto") @Getter @Setter @NoArgsConstructor
public class HistoriaImpacto {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=150) private String titulo;
 private String resumen;
 @Column(columnDefinition="TEXT") private String contenido;
 @Column(name="imagen_url",length=500) private String imagenUrl;
 @Column(nullable=false) private Boolean autorizada=false;
 @Column(nullable=false) private Boolean anonimizada=false;
 @Column(nullable=false) private Boolean publicada=false;
 @Column(name="fecha_publicacion") private LocalDate fechaPublicacion;
}
