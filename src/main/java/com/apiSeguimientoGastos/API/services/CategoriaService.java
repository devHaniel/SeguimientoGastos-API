package com.apiSeguimientoGastos.API.services;

import com.apiSeguimientoGastos.API.dtos.CategoriaDTO;
import com.apiSeguimientoGastos.API.exceptions.NotArgumentValid;
import com.apiSeguimientoGastos.API.exceptions.NotFoundException;
import com.apiSeguimientoGastos.API.mapper.Mapper;
import com.apiSeguimientoGastos.API.repositories.CategoriaRepository;
import com.apiSeguimientoGastos.API.repositories.UsuarioRepository;
import com.apiSeguimientoGastos.API.services.interfaces.ICategoriaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService implements ICategoriaService {

    private CategoriaRepository categoriaRepository;
    private UsuarioRepository usuarioRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository)
    {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<CategoriaDTO> listar(UUID idActual)
    {
        verificarUsuarioActual(idActual);

        return categoriaRepository.findByUsuarioId(idActual).stream()
                .map(Mapper::toDTO)
                .toList();
    }

    @Override
    public CategoriaDTO obtenerPorId(UUID id, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        var categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria no encontrada."));

        if(!categoria.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes ver una categoria de otro usuario.");

        return Mapper.toDTO(categoria);
    }

    @Override
    public CategoriaDTO crear(CategoriaDTO dto, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(dto == null)
            throw new NotArgumentValid("Categoria no valida o vacia.");

        if(dto.nombre() == null || dto.nombre().isEmpty())
            throw new NotArgumentValid("El nombre de la categoria es obligatorio.");

        if(dto.tipo() == null || dto.tipo().isEmpty())
            throw new NotArgumentValid("El tipo de la categoria es obligatorio.");

        var usuario = usuarioRepository.findById(idActual)
                .orElseThrow(() -> new NotFoundException("Usuario actual no encontrado."));

        var categoria = Mapper.toEntitie(dto);
        categoria.setUsuario(usuario);
        var categoriaGuardada = categoriaRepository.save(categoria);

        return Mapper.toDTO(categoriaGuardada);
    }

    @Override
    public CategoriaDTO actualizar(UUID id, CategoriaDTO dto, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        if(dto == null)
            throw new NotArgumentValid("Categoria no valida o vacia.");

        var categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria no encontrada."));

        if(!categoria.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes modificar una categoria de otro usuario.");

        if(dto.nombre() != null && !dto.nombre().isEmpty())
            categoria.setNombre(dto.nombre());

        if(dto.tipo() != null && !dto.tipo().isEmpty())
            categoria.setTipo(dto.tipo());

        var categoriaActualizada = categoriaRepository.save(categoria);

        return Mapper.toDTO(categoriaActualizada);
    }

    @Override
    public void eliminar(UUID id, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        var categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria no encontrada para eliminar."));

        if(!categoria.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes eliminar una categoria de otro usuario.");

        categoriaRepository.deleteById(id);
    }

    private void verificarUsuarioActual(UUID idActual)
    {
        if(idActual == null)
            throw new NotArgumentValid("El id del usuario actual no puede ser nulo.");

        if(!usuarioRepository.existsById(idActual))
            throw new NotFoundException("Usuario actual no encontrado.");
    }
}
