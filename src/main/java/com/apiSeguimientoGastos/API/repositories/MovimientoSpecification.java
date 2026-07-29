package com.apiSeguimientoGastos.API.repositories;

import com.apiSeguimientoGastos.API.entities.Movimiento;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovimientoSpecification {

    public static Specification<Movimiento> conFiltros(
            UUID usuarioId,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            UUID categoriaId,
            UUID metodoPagoId,
            String tipo,
            String q
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("usuario").get("id"), usuarioId));

            if (fechaDesde != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fecha"), fechaDesde));
            }

            if (fechaHasta != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fecha"), fechaHasta));
            }

            if (categoriaId != null) {
                predicates.add(cb.equal(root.get("categoria").get("id"), categoriaId));
            }

            if (metodoPagoId != null) {
                predicates.add(cb.equal(root.get("metodoPago").get("id"), metodoPagoId));
            }

            if (tipo != null && !tipo.isBlank()) {
                predicates.add(cb.equal(root.get("categoria").get("tipo"), tipo.toUpperCase()));
            }

            if (q != null && !q.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("descripcion")), "%" + q.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
