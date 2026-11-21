package cl.corpaliv.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import cl.corpaliv.auth.model.Usuario;
import cl.corpaliv.auth.repository.UsuarioRepository;

@Configuration
public class UsuarioInicialConfig {
    @Bean
    CommandLineRunner crearGestorInicial(
        UsuarioRepository repository,
        PasswordEncoder passwordEncoder,
        @Value("${app.gestor.email}") String email,
        @Value("${app.gestor.password}") String password
    ) {
        return args -> {
            if (repository.findByEmail(email).isEmpty()) {
                Usuario gestor = new Usuario();
                gestor.setEmail(email.toLowerCase());
                gestor.setPasswordHash(passwordEncoder.encode(password));
                gestor.setRol("GESTOR");
                gestor.setActivo(true);
                repository.save(gestor);
            }
        };
    }
}
