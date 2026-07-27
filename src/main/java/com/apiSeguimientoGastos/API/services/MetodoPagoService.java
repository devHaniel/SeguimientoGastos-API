package com.apiSeguimientoGastos.API.services;

import com.apiSeguimientoGastos.API.dtos.MetodoPagoDTO;
import com.apiSeguimientoGastos.API.entities.MetodoPago;
import com.apiSeguimientoGastos.API.exceptions.NotArgumentValid;
import com.apiSeguimientoGastos.API.exceptions.NotFoundException;
import com.apiSeguimientoGastos.API.repositories.MetodoPagoRepository;
import com.apiSeguimientoGastos.API.repositories.UsuarioRepository;
import com.apiSeguimientoGastos.API.services.interfaces.IMetodoPagoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MetodoPagoService implements IMetodoPagoService {

    private MetodoPagoRepository repository;
    private UsuarioRepository usuarioRepository;

    public MetodoPagoService(MetodoPagoRepository repository, UsuarioRepository usuarioRepository)
    {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<MetodoPagoDTO> listar(UUID idActual)
    {
        verificarUsuarioActual(idActual);

        return repository.findByUsuarioId(idActual).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public MetodoPagoDTO obtenerPorId(UUID id, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        var metodoPago = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Metodo de pago no encontrado."));

        if(!metodoPago.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes ver un metodo de pago de otro usuario.");

        return toDTO(metodoPago);
    }

    @Override
    public MetodoPagoDTO crear(MetodoPagoDTO dto, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(dto == null)
            throw new NotArgumentValid("Metodo de pago no valido o vacio.");

        if(dto.nombre() == null || dto.nombre().isEmpty())
            throw new NotArgumentValid("El nombre es obligatorio.");

        if(dto.tipo() == null || dto.tipo().isEmpty())
            throw new NotArgumentValid("El tipo es obligatorio.");

        var usuario = usuarioRepository.findById(idActual)
                .orElseThrow(() -> new NotFoundException("Usuario actual no encontrado."));

        var metodoPago = MetodoPago.builder()
                .nombre(dto.nombre())
                .tipo(dto.tipo())
                .usuario(usuario)
                .build();

        var guardado = repository.save(metodoPago);

        return toDTO(guardado);
    }

    @Override
    public MetodoPagoDTO actualizar(UUID id, MetodoPagoDTO dto, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        if(dto == null)
            throw new NotArgumentValid("Metodo de pago no valido o vacio.");

        var metodoPago = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Metodo de pago no encontrado."));

        if(!metodoPago.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes modificar un metodo de pago de otro usuario.");

        if(dto.nombre() != null && !dto.nombre().isEmpty())
            metodoPago.setNombre(dto.nombre());

        if(dto.tipo() != null && !dto.tipo().isEmpty())
            metodoPago.setTipo(dto.tipo());

        var actualizado = repository.save(metodoPago);

        return toDTO(actualizado);
    }

    @Override
    public void eliminar(UUID id, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        var metodoPago = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Metodo de pago no encontrado para eliminar."));

        if(!metodoPago.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes eliminar un metodo de pago de otro usuario.");

        repository.deleteById(id);
    }

    private MetodoPagoDTO toDTO(MetodoPago mp)
    {
        if(mp == null) return null;

        return new MetodoPagoDTO(
                mp.getId(),
                mp.getNombre(),
                mp.getTipo(),
                mp.getUsuario() != null ? mp.getUsuario().getId() : null
        );
    }

    private void verificarUsuarioActual(UUID idActual)
    {
        if(idActual == null)
            throw new NotArgumentValid("El id del usuario actual no puede ser nulo.");

        if(!usuarioRepository.existsById(idActual))
            throw new NotFoundException("Usuario actual no encontrado.");
    }
}
