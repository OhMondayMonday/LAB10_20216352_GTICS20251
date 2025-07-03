package com.example.lab10_20216352_gtics20251.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CeldaDto {
    private int x;
    private int y;
    private boolean revelada;
    private boolean bomba;
    private int numeroVecinas;
    private boolean exploto;
    
    public CeldaDto(int x, int y) {
        this.x = x;
        this.y = y;
        this.revelada = false;
        this.bomba = false;
        this.numeroVecinas = 0;
        this.exploto = false;
    }
}
