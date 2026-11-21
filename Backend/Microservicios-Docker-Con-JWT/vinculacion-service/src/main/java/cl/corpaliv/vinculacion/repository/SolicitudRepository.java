package cl.corpaliv.vinculacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.corpaliv.vinculacion.model.SolicitudColaboracion;

public interface SolicitudRepository extends JpaRepository<SolicitudColaboracion, Long> {}
