package com.apiSeguimientoGastos.API.services;

import com.apiSeguimientoGastos.API.dtos.UsuarioDTO;
import com.apiSeguimientoGastos.API.exceptions.NotArgumentValid;
import com.apiSeguimientoGastos.API.exceptions.NotFoundException;
import com.apiSeguimientoGastos.API.mapper.Mapper;
import com.apiSeguimientoGastos.API.repositories.UsuarioRepository;
import com.apiSeguimientoGastos.API.services.interfaces.IUsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioService implements IUsuarioService {

    private UsuarioRepository repository;
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder)
    {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioDTO obtenerPorId(UUID id) {
        if(id == null)
            throw  new RuntimeException("Id nulo.");

        var usuarioEncontrado = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

        return Mapper.toDTO(usuarioEncontrado);
    }

    @Override
    public UsuarioDTO obtenerPorEmail(String email) {
        if(email == null || email.isEmpty())
            throw new NotArgumentValid("El email no es valido o está vacio.");

        var usuarioEncontrado = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

        return Mapper.toDTO(usuarioEncontrado);
    }

    @Override
    public UsuarioDTO crear(UsuarioDTO dto, UUID idActual) {
        verificarUsuarioActual(idActual);

        if(dto == null)
        {
            throw new NotArgumentValid("Usuario no es valido o está vacio.");
        }

        if(repository.findByEmail(dto.email().toLowerCase()).isPresent() )
            throw new RuntimeException("Ya existe este correo asosiado a otra cuenta.");

        var usuario = Mapper.toEntitie(dto);

        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        var usuarioGuardado = repository.save(usuario);

        return Mapper.toDTO(usuarioGuardado);
    }

    @Override
    public UsuarioDTO actualizar(UUID id, UsuarioDTO dto, UUID idActual) {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        if(!idActual.equals(id))
            throw new NotArgumentValid("No puedes modificar a otro usuario.");

        if(dto == null)
            throw new NotArgumentValid("Usuario no es valido o está vacio.");

        var usuario = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));

        if(dto.email() != null && !dto.email().isEmpty()) {
            var emailExistente = repository.findByEmail(dto.email().toLowerCase());
            if(emailExistente.isPresent() && !emailExistente.get().getId().equals(id))
                throw new RuntimeException("Ya existe este correo asociado a otra cuenta.");
            usuario.setEmail(dto.email());
        }

        if(dto.nombre() != null && !dto.nombre().isEmpty())
            usuario.setNombre(dto.nombre());

        if(dto.passwordHash() != null && !dto.passwordHash().isEmpty())
            usuario.setPasswordHash(dto.passwordHash());

        var usuarioActualizado = repository.save(usuario);

        return Mapper.toDTO(usuarioActualizado);
    }

    @Override
    public void eliminar(UUID id, UUID idActual) {
        verificarUsuarioActual(idActual);

        if(id == null)
            throw new NotArgumentValid("Id nulo.");

        if(!idActual.equals(id))
            throw new NotArgumentValid("No puedes eliminar a otro usuario.");

        if(!repository.existsById(id))
            throw new NotFoundException("Usuario no encontrado para eliminar");

        repository.deleteById(id);
    }

    @Override
    public UsuarioDTO registrar(UsuarioDTO dto)
    {
        if(dto == null)
            throw new NotArgumentValid("Usuario no es valido o está vacio.");

        if(dto.email() == null || dto.email().isEmpty())
            throw new NotArgumentValid("El email es obligatorio.");

        if(dto.passwordHash() == null || dto.passwordHash().isEmpty())
            throw new NotArgumentValid("La contraseña es obligatoria.");

        if(repository.findByEmail(dto.email()).isPresent())
            throw new RuntimeException("Ya existe un usuario con este email.");

        var usuario = Mapper.toEntitie(dto);
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        var usuarioGuardado = repository.save(usuario);

        return Mapper.toDTO(usuarioGuardado);
    }

    @Override
    public UsuarioDTO login(String email, String password)
    {
        if(email == null || email.isEmpty())
            throw new NotArgumentValid("El email es obligatorio.");

        if(password == null || password.isEmpty())
            throw new NotArgumentValid("La contraseña es obligatoria.");

        var usuario = repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email o contraseña incorrectos."));

        if(!passwordEncoder.matches(password, usuario.getPasswordHash()))
            throw new NotFoundException("Email o contraseña incorrectos.");

        return Mapper.toDTO(usuario);
    }

    private void verificarUsuarioActual(UUID idActual)
    {
        if(idActual == null)
            throw new NotArgumentValid("El id del usuario actual no puede ser nulo.");

        if(!repository.existsById(idActual))
            throw new NotFoundException("Usuario actual no encontrado.");
    }

}
