package cl.corpaliv.vinculacion.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.corpaliv.vinculacion.model.HistorialSolicitud;

public interface HistorialSolicitudRepository extends JpaRepository<HistorialSolicitud, Long> {
    List<HistorialSolicitud> findBySolicitudIdOrderByFechaCambioDesc(Long solicitudId);
}
