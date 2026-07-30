package com.apiSeguimientoGastos.API.controller;

import com.apiSeguimientoGastos.API.dtos.LoginRequest;
import com.apiSeguimientoGastos.API.dtos.LoginResponse;
import com.apiSeguimientoGastos.API.dtos.UsuarioDTO;
import com.apiSeguimientoGastos.API.exceptions.NotArgumentValid;
import com.apiSeguimientoGastos.API.exceptions.NotFoundException;
import com.apiSeguimientoGastos.API.repositories.UsuarioRepository;
import com.apiSeguimientoGastos.API.security.JwtService;
import io.jsonwebtoken.JwtException;
import com.apiSeguimientoGastos.API.services.RefreshTokenService;
import com.apiSeguimientoGastos.API.services.TokenBlacklistService;
import com.apiSeguimientoGastos.API.services.interfaces.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private IUsuarioService service;
    private JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(IUsuarioService service,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          TokenBlacklistService tokenBlacklistService,
                          RefreshTokenService refreshTokenService,
                          UsuarioRepository usuarioRepository)
    {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenBlacklistService = tokenBlacklistService;
        this.refreshTokenService = refreshTokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse register(@RequestBody UsuarioDTO dto)
    {
        var usuarioDTO = service.registrar(dto);
        var usuario = usuarioRepository.findById(usuarioDTO.id())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));
        var token = jwtService.generateToken(usuarioDTO);
        var refreshToken = refreshTokenService.createRefreshToken(usuario);
        return new LoginResponse(usuarioDTO.email(), usuarioDTO.nombre(), token, refreshToken.getToken());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var usuarioDTO = service.login(request.getEmail(), request.getPassword());
        var usuario = usuarioRepository.findById(usuarioDTO.id())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado."));
        var token = jwtService.generateToken(usuarioDTO);
        var refreshToken = refreshTokenService.createRefreshToken(usuario);
        return new LoginResponse(usuarioDTO.email(), usuarioDTO.nombre(), token, refreshToken.getToken());
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody Map<String, String> body)
    {
        String refreshTokenValue = body.get("refresh_token");
        if (refreshTokenValue == null || refreshTokenValue.isBlank()) {
            throw new NotArgumentValid("refresh_token es obligatorio.");
        }

        var novoRefreshToken = refreshTokenService.validateAndRotate(refreshTokenValue);
        var usuario = novoRefreshToken.getUsuario();
        var usuarioDTO = service.obtenerPorId(usuario.getId());
        var token = jwtService.generateToken(usuarioDTO);

        return new LoginResponse(usuarioDTO.email(), usuarioDTO.nombre(), token, novoRefreshToken.getToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authHeader,
                                                       @RequestBody(required = false) Map<String, String> body)
    {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token no proporcionado."));
        }
        String token = authHeader.substring(7);
        tokenBlacklistService.invalidate(token);

        if (body != null && body.containsKey("refresh_token")) {
            refreshTokenService.revoke(body.get("refresh_token"));
        }

        return ResponseEntity.ok(Map.of("mensaje", "Sesión cerrada correctamente."));
    }

    @PostMapping("/verify-token")
    public ResponseEntity<Map<String, Boolean>> verifyToken(@RequestBody Map<String, String> body)
    {
        String token = body.get("token");
        if (token == null || token.isBlank()) {
            throw new NotArgumentValid("token es obligatorio.");
        }
        try {
            boolean expirado = jwtService.isTokenExpired(token);
            return ResponseEntity.ok(Map.of("expirado", expirado));
        } catch (JwtException e) {
            throw new NotArgumentValid("Token inválido.");
        }
    }
}
