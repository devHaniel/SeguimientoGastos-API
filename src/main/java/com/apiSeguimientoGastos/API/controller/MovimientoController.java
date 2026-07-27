package com.apiSeguimientoGastos.API.controller;

import com.apiSeguimientoGastos.API.dtos.MovimientoDTO;
import com.apiSeguimientoGastos.API.security.SessionContext;
import com.apiSeguimientoGastos.API.services.interfaces.IMovimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/movimiento")
public class MovimientoController {

    private IMovimientoService service;

    public MovimientoController(IMovimientoService service)
    {
        this.service = service;
    }

    @GetMapping
    public List<MovimientoDTO> listar()
    {
        return service.listar(SessionContext.getIdActual());
    }

    @GetMapping("/{id}")
    public MovimientoDTO obtenerPorId(@PathVariable UUID id)
    {
        return service.obtenerPorId(id, SessionContext.getIdActual());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoDTO crear(@RequestBody MovimientoDTO dto)
    {
        return service.crear(dto, SessionContext.getIdActual());
    }

    @PutMapping("/{id}")
    public MovimientoDTO actualizar(@PathVariable UUID id, @RequestBody MovimientoDTO dto)
    {
        return service.actualizar(id, dto, SessionContext.getIdActual());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable UUID id)
    {
        service.eliminar(id, SessionContext.getIdActual());
    }
}
