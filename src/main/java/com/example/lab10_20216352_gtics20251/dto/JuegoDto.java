package com.example.lab10_20216352_gtics20251.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JuegoDto {
    private CeldaDto[][] tablero;
    private int intentosRestantes;
    private boolean juegoTerminado;
    private boolean ganado;
    private String mensaje;
    private int dimX;
    private int dimY;
    
    public JuegoDto(int dimX, int dimY, int intentos) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.tablero = new CeldaDto[dimX][dimY];
        this.intentosRestantes = intentos;
        this.juegoTerminado = false;
        this.ganado = false;
        this.mensaje = "";
        
        // Inicializar tablero
        for (int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                tablero[i][j] = new CeldaDto(i, j);
            }
        }
    }
}
