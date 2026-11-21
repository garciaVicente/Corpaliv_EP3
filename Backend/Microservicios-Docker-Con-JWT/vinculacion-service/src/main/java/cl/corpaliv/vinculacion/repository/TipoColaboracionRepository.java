package cl.corpaliv.vinculacion.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.corpaliv.vinculacion.model.TipoColaboracion;

public interface TipoColaboracionRepository extends JpaRepository<TipoColaboracion, Long> {
    Optional<TipoColaboracion> findByCodigoAndActivoTrue(String codigo);
    List<TipoColaboracion> findByActivoTrueOrderByNombre();
}
