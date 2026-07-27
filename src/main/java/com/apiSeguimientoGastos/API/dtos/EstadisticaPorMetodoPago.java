package com.apiSeguimientoGastos.API.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record EstadisticaPorMetodoPago(
        UUID metodoPagoId,
        String metodoPagoNombre,
        String metodoPagoTipo,
        BigDecimal ingresos,
        BigDecimal gastos
) {
}
