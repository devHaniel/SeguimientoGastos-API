package com.apiSeguimientoGastos.API.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginResponse {
    private String email;
    private String nombre;
    private String token;
    @JsonProperty("refresh_token")
    private String refreshToken;

    public LoginResponse(String email, String nombre, String token, String refreshToken) {
        this.email = email;
        this.nombre = nombre;
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
