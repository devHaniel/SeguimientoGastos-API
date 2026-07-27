package com.apiSeguimientoGastos.API.services.interfaces;

import com.apiSeguimientoGastos.API.dtos.CategoriaDTO;

import java.util.List;
import java.util.UUID;

public interface ICategoriaService {
    List<CategoriaDTO> listar(UUID idActual);
    CategoriaDTO obtenerPorId(UUID id, UUID idActual);
    CategoriaDTO crear(CategoriaDTO dto, UUID idActual);
    CategoriaDTO actualizar(UUID id, CategoriaDTO dto, UUID idActual);
    void eliminar(UUID id, UUID idActual);
}
