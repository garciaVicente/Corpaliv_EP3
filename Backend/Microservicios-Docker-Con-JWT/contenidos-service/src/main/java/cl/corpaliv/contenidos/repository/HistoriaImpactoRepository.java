package cl.corpaliv.contenidos.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.corpaliv.contenidos.model.HistoriaImpacto;
public interface HistoriaImpactoRepository extends JpaRepository<HistoriaImpacto, Long> {
    List<HistoriaImpacto> findByAutorizadaTrueAndAnonimizadaTrueAndPublicadaTrue();
}
