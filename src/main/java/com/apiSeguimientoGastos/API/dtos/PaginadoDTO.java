package com.apiSeguimientoGastos.API.dtos;

import java.util.List;

public record PaginadoDTO<T>(
        List<T> contenido,
        int pagina,
        int tamanio,
        long totalElementos,
        int totalPaginas,
        boolean ultima
) {
}
