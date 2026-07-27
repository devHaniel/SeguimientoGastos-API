package com.apiSeguimientoGastos.API.services.interfaces;

import com.apiSeguimientoGastos.API.dtos.UsuarioDTO;

import java.util.List;
import java.util.UUID;

public interface IUsuarioService {
    UsuarioDTO obtenerPorId(UUID id);
    UsuarioDTO obtenerPorEmail(String email);
    UsuarioDTO crear(UsuarioDTO dto, UUID idActual);
    UsuarioDTO actualizar(UUID id, UsuarioDTO dto, UUID idActual);
    void eliminar(UUID id, UUID idActual);
    UsuarioDTO registrar(UsuarioDTO dto);
    UsuarioDTO login(String email, String password);
}
