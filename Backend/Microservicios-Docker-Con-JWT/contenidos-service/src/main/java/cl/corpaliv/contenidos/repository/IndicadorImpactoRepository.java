package cl.corpaliv.contenidos.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.corpaliv.contenidos.model.IndicadorImpacto;
public interface IndicadorImpactoRepository extends JpaRepository<IndicadorImpacto, Long> {
    List<IndicadorImpacto> findByPublicadoTrue();
}
