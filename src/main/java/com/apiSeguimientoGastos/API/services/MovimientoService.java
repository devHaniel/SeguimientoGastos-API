package com.apiSeguimientoGastos.API.services;

import com.apiSeguimientoGastos.API.dtos.MovimientoDTO;
import com.apiSeguimientoGastos.API.exceptions.NotArgumentValid;
import com.apiSeguimientoGastos.API.exceptions.NotFoundException;
import com.apiSeguimientoGastos.API.mapper.Mapper;
import com.apiSeguimientoGastos.API.repositories.CategoriaRepository;
import com.apiSeguimientoGastos.API.repositories.MetodoPagoRepository;
import com.apiSeguimientoGastos.API.repositories.MovimientoRepository;
import com.apiSeguimientoGastos.API.repositories.UsuarioRepository;
import com.apiSeguimientoGastos.API.services.interfaces.IMovimientoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovimientoService implements IMovimientoService {

    private MovimientoRepository movimientoRepository;
    private UsuarioRepository usuarioRepository;
    private CategoriaRepository categoriaRepository;
    private MetodoPagoRepository metodoPagoRepository;

    public MovimientoService(MovimientoRepository movimientoRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository, MetodoPagoRepository metodoPagoRepository)
    {
        this.movimientoRepository = movimientoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.metodoPagoRepository = metodoPagoRepository;
    }

    @Override
    public List<MovimientoDTO> listar(UUID idActual)
    {
        verificarUsuarioActual(idActual);

        return movimientoRepository.findByUsuarioId(idActual).stream()
                .map(Mapper::toDTO)
                .toList();
    }

    @Override
    public List<MovimientoDTO> listarPorMetodoPago(UUID idActual, UUID metodoPagoId)
    {
        verificarUsuarioActual(idActual);

        return movimientoRepository.findByUsuarioIdAndMetodoPagoId(idActual, metodoPagoId).stream()
                .map(Mapper::toDTO)
                .toList();
    }

    @Override
    public MovimientoDTO obtenerPorId(UUID id, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        var movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimiento no encontrado."));

        if(!movimiento.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes ver un movimiento de otro usuario.");

        return Mapper.toDTO(movimiento);
    }

    @Override
    public MovimientoDTO crear(MovimientoDTO dto, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(dto == null)
            throw new NotArgumentValid("Movimiento no valido o vacio.");

        if(dto.monto() == null)
            throw new NotArgumentValid("El monto es obligatorio.");

        if(dto.categoriaId() == null)
            throw new NotArgumentValid("La categoria es obligatoria.");

        if(dto.metodoPagoId() == null)
            throw new NotArgumentValid("El metodo de pago es obligatorio.");

        var usuario = usuarioRepository.findById(idActual)
                .orElseThrow(() -> new NotFoundException("Usuario actual no encontrado."));

        var categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria no encontrada."));

        if(!categoria.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes usar una categoria de otro usuario.");

        var metodoPago = metodoPagoRepository.findById(dto.metodoPagoId())
                .orElseThrow(() -> new NotFoundException("Metodo de pago no encontrado."));

        if(!metodoPago.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes usar un metodo de pago de otro usuario.");

        var movimiento = Mapper.toEntitie(dto);
        movimiento.setUsuario(usuario);
        movimiento.setCategoria(categoria);
        movimiento.setMetodoPago(metodoPago);

        if(dto.fecha() == null)
            movimiento.setFecha(java.time.LocalDate.now());

        var movimientoGuardado = movimientoRepository.save(movimiento);

        return Mapper.toDTO(movimientoGuardado);
    }

    @Override
    public MovimientoDTO actualizar(UUID id, MovimientoDTO dto, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        if(dto == null)
            throw new NotArgumentValid("Movimiento no valido o vacio.");

        var movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimiento no encontrado."));

        if(!movimiento.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes modificar un movimiento de otro usuario.");

        if(dto.monto() != null)
            movimiento.setMonto(dto.monto());

        if(dto.fecha() != null)
            movimiento.setFecha(dto.fecha());

        if(dto.descripcion() != null)
            movimiento.setDescripcion(dto.descripcion());

        if(dto.categoriaId() != null)
        {
            var categoria = categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new NotFoundException("Categoria no encontrada."));

            if(!categoria.getUsuario().getId().equals(idActual))
                throw new NotArgumentValid("No puedes usar una categoria de otro usuario.");

            movimiento.setCategoria(categoria);
        }

        if(dto.metodoPagoId() != null)
        {
            var metodoPago = metodoPagoRepository.findById(dto.metodoPagoId())
                    .orElseThrow(() -> new NotFoundException("Metodo de pago no encontrado."));

            if(!metodoPago.getUsuario().getId().equals(idActual))
                throw new NotArgumentValid("No puedes usar un metodo de pago de otro usuario.");

            movimiento.setMetodoPago(metodoPago);
        }

        var movimientoActualizado = movimientoRepository.save(movimiento);

        return Mapper.toDTO(movimientoActualizado);
    }

    @Override
    public void eliminar(UUID id, UUID idActual)
    {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        var movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimiento no encontrado para eliminar."));

        if(!movimiento.getUsuario().getId().equals(idActual))
            throw new NotArgumentValid("No puedes eliminar un movimiento de otro usuario.");

        movimientoRepository.deleteById(id);
    }

    private void verificarUsuarioActual(UUID idActual)
    {
        if(idActual == null)
            throw new NotArgumentValid("El id del usuario actual no puede ser nulo.");

        if(!usuarioRepository.existsById(idActual))
            throw new NotFoundException("Usuario actual no encontrado.");
    }
}
