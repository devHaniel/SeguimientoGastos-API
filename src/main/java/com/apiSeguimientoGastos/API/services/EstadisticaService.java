package com.apiSeguimientoGastos.API.services;

import com.apiSeguimientoGastos.API.dtos.EstadisticaDTO;
import com.apiSeguimientoGastos.API.dtos.EstadisticaPorMetodoPago;
import com.apiSeguimientoGastos.API.entities.MetodoPago;
import com.apiSeguimientoGastos.API.entities.Movimiento;
import com.apiSeguimientoGastos.API.repositories.MetodoPagoRepository;
import com.apiSeguimientoGastos.API.repositories.MovimientoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class EstadisticaService {

    private final MovimientoRepository movimientoRepository;
    private final MetodoPagoRepository metodoPagoRepository;

    public EstadisticaService(MovimientoRepository movimientoRepository, MetodoPagoRepository metodoPagoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.metodoPagoRepository = metodoPagoRepository;
    }

    public EstadisticaDTO resumen(UUID usuarioId, String periodo) {
        LocalDate hoy = LocalDate.now();
        LocalDate inicio = switch (periodo.toUpperCase()) {
            case "HOY" -> hoy;
            case "SEMANA" -> hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            case "MES" -> hoy.withDayOfMonth(1);
            case "ANIO", "AÑO" -> hoy.withDayOfYear(1);
            default -> throw new IllegalArgumentException("Periodo invalido: " + periodo);
        };
        LocalDate fin = hoy;

        return calcular(usuarioId, inicio, fin);
    }

    public EstadisticaDTO resumenPorRango(UUID usuarioId, LocalDate inicio, LocalDate fin) {
        return calcular(usuarioId, inicio, fin);
    }

    private EstadisticaDTO calcular(UUID usuarioId, LocalDate inicio, LocalDate fin) {
        List<Movimiento> movimientos = movimientoRepository
                .findByUsuarioIdAndFechaBetween(usuarioId, inicio, fin);

        Map<UUID, List<Movimiento>> agrupados = new HashMap<>();
        for (var m : movimientos) {
            UUID mpId = m.getMetodoPago() != null ? m.getMetodoPago().getId() : null;
            agrupados.computeIfAbsent(mpId, k -> new ArrayList<>()).add(m);
        }

        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalGastos = BigDecimal.ZERO;
        List<EstadisticaPorMetodoPago> porMetodoPago = new ArrayList<>();

        for (var entry : agrupados.entrySet()) {
            UUID mpId = entry.getKey();
            List<Movimiento> lista = entry.getValue();

            BigDecimal ingresos = BigDecimal.ZERO;
            BigDecimal gastos = BigDecimal.ZERO;

            for (var m : lista) {
                String tipo = m.getCategoria() != null ? m.getCategoria().getTipo() : "";
                if ("INGRESO".equalsIgnoreCase(tipo)) {
                    ingresos = ingresos.add(m.getMonto());
                } else {
                    gastos = gastos.add(m.getMonto());
                }
            }

            totalIngresos = totalIngresos.add(ingresos);
            totalGastos = totalGastos.add(gastos);

            String nombre = "";
            String tipo = "";
            if (mpId != null) {
                Optional<MetodoPago> mp = metodoPagoRepository.findById(mpId);
                if (mp.isPresent()) {
                    nombre = mp.get().getNombre();
                    tipo = mp.get().getTipo();
                }
            }

            porMetodoPago.add(new EstadisticaPorMetodoPago(mpId, nombre, tipo, ingresos, gastos));
        }

        return new EstadisticaDTO(totalIngresos, totalGastos, porMetodoPago);
    }
}
