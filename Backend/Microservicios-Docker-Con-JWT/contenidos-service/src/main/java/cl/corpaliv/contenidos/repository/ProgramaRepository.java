package cl.corpaliv.contenidos.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.corpaliv.contenidos.model.Programa;
public interface ProgramaRepository extends JpaRepository<Programa, Long> {
    List<Programa> findByPublicadoTrue();
}
