package cl.corpaliv.auth.service;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cl.corpaliv.auth.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final SecretKey key;
    private final long duracionMs;

    public JwtService(
        @Value("${app.jwt.secret}") String secret,
        @Value("${app.jwt.duracion-ms:3600000}") long duracionMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.duracionMs = duracionMs;
    }

    public String generarToken(Usuario usuario) {
        Date ahora = new Date();
        return Jwts.builder()
            .subject(usuario.getEmail())
            .claim("rol", usuario.getRol())
            .issuedAt(ahora)
            .expiration(new Date(ahora.getTime() + duracionMs))
            .signWith(key, Jwts.SIG.HS256)
            .compact();
    }
}
