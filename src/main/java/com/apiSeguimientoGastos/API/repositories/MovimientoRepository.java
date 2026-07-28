package com.apiSeguimientoGastos.API.repositories;

import com.apiSeguimientoGastos.API.entities.Movimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, UUID>, JpaSpecificationExecutor<Movimiento> {
    List<Movimiento> findByUsuarioId(UUID usuarioId);
    Page<Movimiento> findByUsuarioId(UUID usuarioId, Pageable pageable);
    List<Movimiento> findByUsuarioIdAndMetodoPagoId(UUID usuarioId, UUID metodoPagoId);
    Page<Movimiento> findByUsuarioIdAndMetodoPagoId(UUID usuarioId, UUID metodoPagoId, Pageable pageable);
    List<Movimiento> findByUsuarioIdAndFechaBetween(UUID usuarioId, LocalDate inicio, LocalDate fin);
}
