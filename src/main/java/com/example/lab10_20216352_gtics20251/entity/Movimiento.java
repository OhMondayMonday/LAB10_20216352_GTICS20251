package com.example.lab10_20216352_gtics20251.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")
@Getter
@Setter
public class Movimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMovimiento;
    
    @Column(name = "id_mina")
    private Integer idMina;
    
    @Column(name = "coordenada_x")
    private Integer coordenadaX;
    
    @Column(name = "coordenada_y")
    private Integer coordenadaY;
    
    @Column(name = "es_bomba")
    private Boolean esBomba;
    
    @Column(name = "numero_vecinas")
    private Integer numeroVecinas;
    
    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento;
    
    @Column(name = "numero_movimiento")
    private Integer numeroMovimiento;
    
    @Column(name = "resultado")
    private String resultado; // "SEGURO", "BOMBA", "VICTORIA", "DERROTA"
}
