package cl.corpaliv.auth.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.corpaliv.auth.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
