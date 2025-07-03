package com.example.lab10_20216352_gtics20251.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "configuracion")
@Getter
@Setter
public class Configuracion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMina;
    
    @Column(name = "dim_mina_x")
    private Integer dimMinaX;
    
    @Column(name = "dim_mina_y")
    private Integer dimMinaY;
    
    @Column(name = "cant_bombas")
    private Integer cantBombas;
    
    @Column(name = "cant_intentos")
    private Integer cantIntentos;
    
    @Column(name = "cant_intentos_actual")
    private Integer cantIntentosActual;
}
