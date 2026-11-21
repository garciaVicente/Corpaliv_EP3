package cl.corpaliv.contenidos.model;
import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
@Entity @Table(name="producto_servicio") @Getter @Setter @NoArgsConstructor
public class ProductoServicio {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="categoria_id") private Long categoriaId;
 @Column(nullable=false,length=150) private String nombre;
 @Column(columnDefinition="TEXT") private String descripcion;
 @Column(name="imagen_url",length=500) private String imagenUrl;
 @Column(name="modalidad_consulta",length=100) private String modalidadConsulta;
 @Column(nullable=false) private Boolean disponible=true;
 @Column(nullable=false) private Boolean publicado=false;
}
