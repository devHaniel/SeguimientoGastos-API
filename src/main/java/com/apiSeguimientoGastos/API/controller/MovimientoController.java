package com.apiSeguimientoGastos.API.controller;

import com.apiSeguimientoGastos.API.dtos.MovimientoDTO;
import com.apiSeguimientoGastos.API.dtos.PaginadoDTO;
import com.apiSeguimientoGastos.API.security.SessionContext;
import com.apiSeguimientoGastos.API.services.interfaces.IMovimientoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public PaginadoDTO<MovimientoDTO> listar(
            @PageableDefault(size = 10, sort = "fecha,desc") Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) UUID categoriaId,
            @RequestParam(required = false) UUID metodoPagoId,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String q
    ) {
        return service.listar(SessionContext.getIdActual(), pageable,
                fechaDesde, fechaHasta, categoriaId, metodoPagoId, tipo, q);
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
