package com.apiSeguimientoGastos.API.repositories;

import com.apiSeguimientoGastos.API.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    List<Categoria> findByUsuarioId(UUID usuarioId);
}
