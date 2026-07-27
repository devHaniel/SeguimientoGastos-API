package com.apiSeguimientoGastos.API.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UsuarioDTO(
        UUID id,
        String email,
        String nombre,
        String passwordHash
) {
}
