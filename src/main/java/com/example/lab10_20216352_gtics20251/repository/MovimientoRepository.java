package com.example.lab10_20216352_gtics20251.repository;

import com.example.lab10_20216352_gtics20251.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
    
    List<Movimiento> findByIdMinaOrderByNumeroMovimiento(Integer idMina);
    
    @Query("SELECT COALESCE(MAX(m.numeroMovimiento), 0) FROM Movimiento m WHERE m.idMina = :idMina")
    Integer findMaxNumeroMovimientoByIdMina(Integer idMina);
    
    void deleteByIdMina(Integer idMina);
}
