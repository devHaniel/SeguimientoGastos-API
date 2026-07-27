package com.apiSeguimientoGastos.API.security;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SessionContext {

    public static UUID getIdActual()
    {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal))
            throw new IllegalStateException("No hay sesión activa.");
        return principal.id();
    }
}
