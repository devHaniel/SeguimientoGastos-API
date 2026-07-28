package com.apiSeguimientoGastos.API.mapper;

import com.apiSeguimientoGastos.API.dtos.CategoriaDTO;
import com.apiSeguimientoGastos.API.dtos.MovimientoDTO;
import com.apiSeguimientoGastos.API.dtos.UsuarioDTO;
import com.apiSeguimientoGastos.API.entities.Categoria;
import com.apiSeguimientoGastos.API.entities.Movimiento;
import com.apiSeguimientoGastos.API.entities.Usuario;

public class Mapper {
    public static UsuarioDTO toDTO(Usuario u)
    {
        if(u == null) return null;

        return UsuarioDTO.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .email(u.getEmail().toLowerCase())
                .passwordHash(u.getPasswordHash())
                .moneda(u.getMoneda())
                .build();
    }

    public static Usuario toEntitie(UsuarioDTO u)
    {
        if(u == null) return null;

        return Usuario.builder()
                .id(u.id())
                .nombre(u.nombre())
                .email(u.email().toLowerCase())
                .passwordHash(u.passwordHash())
                .moneda(u.moneda())
                .build();
    }

    public static CategoriaDTO toDTO(Categoria c)
    {
        if(c == null) return null;

        return new CategoriaDTO(
                c.getId(),
                c.getNombre(),
                c.getTipo(),
                c.getUsuario() != null ? c.getUsuario().getId() : null
        );
    }

    public static Categoria toEntitie(CategoriaDTO c)
    {
        if(c == null) return null;

        return Categoria.builder()
                .id(c.id())
                .nombre(c.nombre())
                .tipo(c.tipo())
                .build();
    }

    public static MovimientoDTO toDTO(Movimiento m)
    {
        if(m == null) return null;

        return new MovimientoDTO(
                m.getId(),
                m.getMonto(),
                m.getFecha(),
                m.getDescripcion(),
                m.getCategoria() != null ? m.getCategoria().getId() : null,
                m.getMetodoPago() != null ? m.getMetodoPago().getId() : null,
                m.getUsuario() != null ? m.getUsuario().getId() : null
        );
    }

    public static Movimiento toEntitie(MovimientoDTO m)
    {
        if(m == null) return null;

        return Movimiento.builder()
                .id(m.id())
                .monto(m.monto())
                .fecha(m.fecha())
                .descripcion(m.descripcion())
                .build();
    }

}
