package com.example.lab10_20216352_gtics20251.repository;

import com.example.lab10_20216352_gtics20251.entity.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Integer> {
}
