package com.apiSeguimientoGastos.API.controller;

import com.apiSeguimientoGastos.API.dtos.CategoriaDTO;
import com.apiSeguimientoGastos.API.security.SessionContext;
import com.apiSeguimientoGastos.API.services.interfaces.ICategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    private ICategoriaService service;

    public CategoriaController(ICategoriaService service)
    {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaDTO> listar()
    {
        return service.listar(SessionContext.getIdActual());
    }

    @GetMapping("/{id}")
    public CategoriaDTO obtenerPorId(@PathVariable UUID id)
    {
        return service.obtenerPorId(id, SessionContext.getIdActual());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDTO crear(@RequestBody CategoriaDTO dto)
    {
        return service.crear(dto, SessionContext.getIdActual());
    }

    @PutMapping("/{id}")
    public CategoriaDTO actualizar(@PathVariable UUID id, @RequestBody CategoriaDTO dto)
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
