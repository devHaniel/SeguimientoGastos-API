package com.apiSeguimientoGastos.API.entities;

import com.apiSeguimientoGastos.API.entities.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nombre;
    private String tipo;

    @ManyToOne
    private Usuario usuario;
}