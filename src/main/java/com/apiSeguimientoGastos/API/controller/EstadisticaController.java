package com.apiSeguimientoGastos.API.controller;

import com.apiSeguimientoGastos.API.dtos.EstadisticaDTO;
import com.apiSeguimientoGastos.API.security.SessionContext;
import com.apiSeguimientoGastos.API.services.EstadisticaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticaController {

    private final EstadisticaService service;

    public EstadisticaController(EstadisticaService service) {
        this.service = service;
    }

    @GetMapping("/resumen")
    public EstadisticaDTO resumen(
            @RequestParam(defaultValue = "MES") String periodo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        if (fechaInicio != null && fechaFin != null) {
            return service.resumenPorRango(SessionContext.getIdActual(), fechaInicio, fechaFin);
        }
        return service.resumen(SessionContext.getIdActual(), periodo);
    }
}
