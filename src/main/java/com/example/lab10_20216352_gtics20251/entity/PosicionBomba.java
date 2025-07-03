package com.example.lab10_20216352_gtics20251.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "posicionbomba")
@Getter
@Setter
public class PosicionBomba {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idBomba;
    
    @Column(name = "coordenada_x")
    private Integer coordenadaX;
    
    @Column(name = "coordenada_y")
    private Integer coordenadaY;
    
    @Column(name = "id_mina")
    private Integer idMina;
}
