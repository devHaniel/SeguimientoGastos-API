package com.apiSeguimientoGastos.API.repositories;

import com.apiSeguimientoGastos.API.entities.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, UUID> {
    List<MetodoPago> findByUsuarioId(UUID usuarioId);
}
