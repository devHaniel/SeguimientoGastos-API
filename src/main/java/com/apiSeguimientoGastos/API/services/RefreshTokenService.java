package com.apiSeguimientoGastos.API.services;

import com.apiSeguimientoGastos.API.entities.RefreshToken;
import com.apiSeguimientoGastos.API.entities.Usuario;
import com.apiSeguimientoGastos.API.exceptions.NotArgumentValid;
import com.apiSeguimientoGastos.API.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final long refreshExpiration;

    public RefreshTokenService(RefreshTokenRepository repository,
                               @Value("${jwt.refresh-expiration}") long refreshExpiration) {
        this.repository = repository;
        this.refreshExpiration = refreshExpiration;
    }

    public RefreshToken createRefreshToken(Usuario usuario) {
        var refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .usuario(usuario)
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .revoked(false)
                .build();

        return repository.save(refreshToken);
    }

    public RefreshToken validateAndRotate(String token) {
        var refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new NotArgumentValid("Refresh token inválido."));

        if (refreshToken.isRevoked()) {
            throw new NotArgumentValid("Refresh token ya fue revocado.");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new NotArgumentValid("Refresh token expirado.");
        }

        refreshToken.setRevoked(true);
        repository.save(refreshToken);

        return createRefreshToken(refreshToken.getUsuario());
    }

    public void revoke(String token) {
        repository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            repository.save(rt);
        });
    }

    @Transactional
    public void revokeAllByUsuario(UUID usuarioId) {
        repository.deleteByUsuarioId(usuarioId);
    }
}
