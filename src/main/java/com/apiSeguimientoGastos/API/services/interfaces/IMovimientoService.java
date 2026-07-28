package com.apiSeguimientoGastos.API.services.interfaces;

import com.apiSeguimientoGastos.API.dtos.MovimientoDTO;
import com.apiSeguimientoGastos.API.dtos.PaginadoDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface IMovimientoService {
    PaginadoDTO<MovimientoDTO> listar(UUID idActual, Pageable pageable,
                                      LocalDate fechaDesde, LocalDate fechaHasta,
                                      UUID categoriaId, UUID metodoPagoId,
                                      String tipo, String q);
    PaginadoDTO<MovimientoDTO> listarPorMetodoPago(UUID idActual, UUID metodoPagoId, Pageable pageable);
    MovimientoDTO obtenerPorId(UUID id, UUID idActual);
    MovimientoDTO crear(MovimientoDTO dto, UUID idActual);
    MovimientoDTO actualizar(UUID id, MovimientoDTO dto, UUID idActual);
    void eliminar(UUID id, UUID idActual);
}
