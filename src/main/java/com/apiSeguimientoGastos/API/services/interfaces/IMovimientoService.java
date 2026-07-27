package com.apiSeguimientoGastos.API.services.interfaces;

import com.apiSeguimientoGastos.API.dtos.MovimientoDTO;

import java.util.List;
import java.util.UUID;

public interface IMovimientoService {
    List<MovimientoDTO> listar(UUID idActual);
    List<MovimientoDTO> listarPorMetodoPago(UUID idActual, UUID metodoPagoId);
    MovimientoDTO obtenerPorId(UUID id, UUID idActual);
    MovimientoDTO crear(MovimientoDTO dto, UUID idActual);
    MovimientoDTO actualizar(UUID id, MovimientoDTO dto, UUID idActual);
    void eliminar(UUID id, UUID idActual);
}
