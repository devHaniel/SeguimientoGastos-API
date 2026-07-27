package com.apiSeguimientoGastos.API.controller;

import com.apiSeguimientoGastos.API.dtos.MetodoPagoDTO;
import com.apiSeguimientoGastos.API.dtos.MovimientoDTO;
import com.apiSeguimientoGastos.API.security.SessionContext;
import com.apiSeguimientoGastos.API.services.interfaces.IMetodoPagoService;
import com.apiSeguimientoGastos.API.services.interfaces.IMovimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/metodo-pago")
public class MetodoPagoController {

    private IMetodoPagoService service;
    private IMovimientoService movimientoService;

    public MetodoPagoController(IMetodoPagoService service, IMovimientoService movimientoService)
    {
        this.service = service;
        this.movimientoService = movimientoService;
    }

    @GetMapping
    public List<MetodoPagoDTO> listar()
    {
        return service.listar(SessionContext.getIdActual());
    }

    @GetMapping("/{id}")
    public MetodoPagoDTO obtenerPorId(@PathVariable UUID id)
    {
        return service.obtenerPorId(id, SessionContext.getIdActual());
    }

    @GetMapping("/{id}/movimientos")
    public List<MovimientoDTO> listarMovimientos(@PathVariable UUID id)
    {
        return movimientoService.listarPorMetodoPago(SessionContext.getIdActual(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MetodoPagoDTO crear(@RequestBody MetodoPagoDTO dto)
    {
        return service.crear(dto, SessionContext.getIdActual());
    }

    @PutMapping("/{id}")
    public MetodoPagoDTO actualizar(@PathVariable UUID id, @RequestBody MetodoPagoDTO dto)
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
