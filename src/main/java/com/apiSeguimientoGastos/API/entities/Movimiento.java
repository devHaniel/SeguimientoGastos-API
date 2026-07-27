package com.apiSeguimientoGastos.API.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal monto;
    private LocalDate fecha;
    private String descripcion;

    @ManyToOne
    private Categoria categoria;

    @ManyToOne
    private Usuario usuario;
}
