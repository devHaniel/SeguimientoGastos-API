package com.apiSeguimientoGastos.API.repositories;

import com.apiSeguimientoGastos.API.entities.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, UUID> {
    List<Movimiento> findByUsuarioId(UUID usuarioId);
}
