package com.apiSeguimientoGastos.API.dtos;

import java.math.BigDecimal;
import java.util.List;

public record EstadisticaDTO(
        BigDecimal totalIngresos,
        BigDecimal totalGastos,
        List<EstadisticaPorMetodoPago> porMetodoPago
) {
}
