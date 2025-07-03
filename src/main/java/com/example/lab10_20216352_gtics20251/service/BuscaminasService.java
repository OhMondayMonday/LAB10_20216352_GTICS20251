package com.example.lab10_20216352_gtics20251.service;

import com.example.lab10_20216352_gtics20251.dto.CeldaDto;
import com.example.lab10_20216352_gtics20251.dto.JuegoDto;
import com.example.lab10_20216352_gtics20251.entity.Configuracion;
import com.example.lab10_20216352_gtics20251.entity.Movimiento;
import com.example.lab10_20216352_gtics20251.entity.PosicionBomba;
import com.example.lab10_20216352_gtics20251.repository.ConfiguracionRepository;
import com.example.lab10_20216352_gtics20251.repository.MovimientoRepository;
import com.example.lab10_20216352_gtics20251.repository.PosicionBombaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class BuscaminasService {
    
    @Autowired
    private ConfiguracionRepository configuracionRepository;
    
    @Autowired
    private PosicionBombaRepository posicionBombaRepository;
    
    @Autowired
    private MovimientoRepository movimientoRepository;
    
    @Transactional
    public JuegoDto iniciarJuego() {
        // Obtener configuración de la base de datos (primer registro)
        Configuracion config = configuracionRepository.findAll().get(0);
        
        // Limpiar movimientos previos de esta mina
        movimientoRepository.deleteByIdMina(config.getIdMina());
        
        // Crear nuevo juego
        JuegoDto juego = new JuegoDto(config.getDimMinaX(), config.getDimMinaY(), config.getCantIntentos());
        
        // Obtener posiciones de bombas
        List<PosicionBomba> bombas = posicionBombaRepository.findByIdMina(config.getIdMina());
        
        // Colocar bombas en el tablero
        for (PosicionBomba bomba : bombas) {
            juego.getTablero()[bomba.getCoordenadaX() - 1][bomba.getCoordenadaY() - 1].setBomba(true);
        }
        
        // Calcular números de bombas vecinas
        calcularNumerosBombas(juego);
        
        return juego;
    }
    
    private void calcularNumerosBombas(JuegoDto juego) {
        for (int x = 0; x < juego.getDimX(); x++) {
            for (int y = 0; y < juego.getDimY(); y++) {
                if (!juego.getTablero()[x][y].isBomba()) {
                    int count = 0;
                    // Verificar las 8 direcciones
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int nx = x + dx;
                            int ny = y + dy;
                            if (nx >= 0 && nx < juego.getDimX() && ny >= 0 && ny < juego.getDimY()) {
                                if (juego.getTablero()[nx][ny].isBomba()) {
                                    count++;
                                }
                            }
                        }
                    }
                    juego.getTablero()[x][y].setNumeroVecinas(count);
                }
            }
        }
    }
    
    @Transactional
    public JuegoDto procesarClick(JuegoDto juego, int x, int y) {
        if (juego.isJuegoTerminado() || juego.getTablero()[x][y].isRevelada()) {
            return juego;
        }
        
        // Obtener configuración para el ID de mina
        Configuracion config = configuracionRepository.findAll().get(0);
        
        CeldaDto celda = juego.getTablero()[x][y];
        celda.setRevelada(true);
        
        // Registrar el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setIdMina(config.getIdMina());
        movimiento.setCoordenadaX(x + 1); // Convertir a coordenadas 1-based
        movimiento.setCoordenadaY(y + 1); // Convertir a coordenadas 1-based
        movimiento.setEsBomba(celda.isBomba());
        movimiento.setNumeroVecinas(celda.getNumeroVecinas());
        movimiento.setFechaMovimiento(LocalDateTime.now());
        
        // Obtener el siguiente número de movimiento
        Integer maxMovimiento = movimientoRepository.findMaxNumeroMovimientoByIdMina(config.getIdMina());
        movimiento.setNumeroMovimiento(maxMovimiento + 1);
        
        String resultado;
        
        if (celda.isBomba()) {
            // Encontró una bomba
            celda.setExploto(true);
            juego.setIntentosRestantes(juego.getIntentosRestantes() - 1);
            
            if (juego.getIntentosRestantes() <= 0) {
                // Se acabaron los intentos
                juego.setJuegoTerminado(true);
                juego.setMensaje("¡Usted ha perdido el juego!.");
                revelarTodasLasBombas(juego);
                resultado = "DERROTA";
            } else {
                juego.setMensaje("¡Encontró una bomba, le quedan " + juego.getIntentosRestantes() + " intentos!");
                resultado = "BOMBA";
            }
        } else if (celda.getNumeroVecinas() == 0) {
            // No hay bombas alrededor, expandir usando BFS (sin recursión)
            expandirSinRecursion(juego, x, y);
            resultado = "SEGURO";
        } else {
            resultado = "SEGURO";
        }
        
        // Verificar si ganó
        if (!juego.isJuegoTerminado() && verificarVictoria(juego)) {
            juego.setJuegoTerminado(true);
            juego.setGanado(true);
            juego.setMensaje("¡Usted ha ganado el juego!.");
            resultado = "VICTORIA";
        }
        
        movimiento.setResultado(resultado);
        movimientoRepository.save(movimiento);
        
        return juego;
    }
    
    private void expandirSinRecursion(JuegoDto juego, int startX, int startY) {
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visitado = new boolean[juego.getDimX()][juego.getDimY()];
        
        queue.offer(new int[]{startX, startY});
        visitado[startX][startY] = true;
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            
            // Verificar las 8 direcciones
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int nx = x + dx;
                    int ny = y + dy;
                    
                    if (nx >= 0 && nx < juego.getDimX() && ny >= 0 && ny < juego.getDimY() 
                        && !visitado[nx][ny] && !juego.getTablero()[nx][ny].isBomba()) {
                        
                        visitado[nx][ny] = true;
                        juego.getTablero()[nx][ny].setRevelada(true);
                        
                        // Si esta celda también tiene 0 bombas vecinas, añadirla a la cola
                        if (juego.getTablero()[nx][ny].getNumeroVecinas() == 0) {
                            queue.offer(new int[]{nx, ny});
                        }
                    }
                }
            }
        }
    }
    
    private boolean verificarVictoria(JuegoDto juego) {
        for (int x = 0; x < juego.getDimX(); x++) {
            for (int y = 0; y < juego.getDimY(); y++) {
                CeldaDto celda = juego.getTablero()[x][y];
                if (!celda.isBomba() && !celda.isRevelada()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void revelarTodasLasBombas(JuegoDto juego) {
        for (int x = 0; x < juego.getDimX(); x++) {
            for (int y = 0; y < juego.getDimY(); y++) {
                if (juego.getTablero()[x][y].isBomba()) {
                    juego.getTablero()[x][y].setRevelada(true);
                }
            }
        }
    }

    public List<Movimiento> obtenerHistorialMovimientos() {
        Configuracion config = configuracionRepository.findAll().get(0);
        return movimientoRepository.findByIdMinaOrderByNumeroMovimiento(config.getIdMina());
    }
}
