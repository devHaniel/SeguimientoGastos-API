package com.apiSeguimientoGastos.API.dtos;

import java.util.UUID;

public record MetodoPagoDTO(
        UUID id,
        String nombre,
        String tipo,
        UUID usuarioId
) {
}
