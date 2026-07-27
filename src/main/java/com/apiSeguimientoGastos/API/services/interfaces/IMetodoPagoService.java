package com.apiSeguimientoGastos.API.services.interfaces;

import com.apiSeguimientoGastos.API.dtos.MetodoPagoDTO;

import java.util.List;
import java.util.UUID;

public interface IMetodoPagoService {
    List<MetodoPagoDTO> listar(UUID idActual);
    MetodoPagoDTO obtenerPorId(UUID id, UUID idActual);
    MetodoPagoDTO crear(MetodoPagoDTO dto, UUID idActual);
    MetodoPagoDTO actualizar(UUID id, MetodoPagoDTO dto, UUID idActual);
    void eliminar(UUID id, UUID idActual);
}
