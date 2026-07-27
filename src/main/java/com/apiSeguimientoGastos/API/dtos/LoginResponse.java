package com.apiSeguimientoGastos.API.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class LoginResponse {
    private String email;
    private String nombre;
    private String token;
}
