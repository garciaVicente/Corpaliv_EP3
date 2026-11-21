package cl.corpaliv.contenidos.model;
import jakarta.persistence.*;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter;
@Entity @Table(name="categoria") @Getter @Setter @NoArgsConstructor
public class Categoria {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=100) private String nombre;
 @Column(columnDefinition="TEXT") private String descripcion;
 @Column(length=40) private String tipo;
}
