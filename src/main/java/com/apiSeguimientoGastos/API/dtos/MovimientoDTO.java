package com.apiSeguimientoGastos.API.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record MovimientoDTO(
        UUID id,
        BigDecimal monto,
        LocalDate fecha,
        String descripcion,
        UUID categoriaId,
        UUID metodoPagoId,
        UUID usuarioId
) {
}
