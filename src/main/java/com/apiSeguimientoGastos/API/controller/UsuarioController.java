package com.apiSeguimientoGastos.API.controller;

import com.apiSeguimientoGastos.API.dtos.UsuarioDTO;
import com.apiSeguimientoGastos.API.security.SessionContext;
import com.apiSeguimientoGastos.API.services.interfaces.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private IUsuarioService service;

    public UsuarioController(IUsuarioService service)
    {
        this.service = service;
    }

    @GetMapping()
    public UsuarioDTO obtener()
    {
        var id = SessionContext.getIdActual();
        return service.obtenerPorId(id);
    }

    @PutMapping
    public UsuarioDTO actualizar(@RequestBody UsuarioDTO dto)
    {
        var id = SessionContext.getIdActual();
        return service.actualizar(id, dto, id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar()
    {
        var id = SessionContext.getIdActual();
        service.eliminar(id, id);
    }
}
