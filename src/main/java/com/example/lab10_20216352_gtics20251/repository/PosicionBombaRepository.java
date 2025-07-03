package com.example.lab10_20216352_gtics20251.repository;

import com.example.lab10_20216352_gtics20251.entity.PosicionBomba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosicionBombaRepository extends JpaRepository<PosicionBomba, Integer> {
    List<PosicionBomba> findByIdMina(Integer idMina);
    boolean existsByCoordenadaXAndCoordenadaYAndIdMina(Integer x, Integer y, Integer idMina);
}
