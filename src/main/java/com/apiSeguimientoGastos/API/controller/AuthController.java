package com.apiSeguimientoGastos.API.controller;

import com.apiSeguimientoGastos.API.dtos.LoginRequest;
import com.apiSeguimientoGastos.API.dtos.LoginResponse;
import com.apiSeguimientoGastos.API.dtos.UsuarioDTO;
import com.apiSeguimientoGastos.API.security.JwtService;
import com.apiSeguimientoGastos.API.services.interfaces.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private IUsuarioService service;
    private JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(IUsuarioService service,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager)
    {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse register(@RequestBody UsuarioDTO dto)
    {
        var usuario = service.registrar(dto);
        var token = jwtService.generateToken(usuario);
        return new LoginResponse(usuario.email(), usuario.nombre(), token);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var usuario = service.login(request.getEmail(), request.getPassword());
        var token = jwtService.generateToken(usuario);
        return new LoginResponse(usuario.email(), usuario.nombre(), token);
    }


}
