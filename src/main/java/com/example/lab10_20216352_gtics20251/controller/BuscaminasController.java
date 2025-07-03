package com.example.lab10_20216352_gtics20251.controller;

import com.example.lab10_20216352_gtics20251.dto.JuegoDto;
import com.example.lab10_20216352_gtics20251.entity.Movimiento;
import com.example.lab10_20216352_gtics20251.service.BuscaminasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.List;

@Controller
public class BuscaminasController {
    
    @Autowired
    private BuscaminasService buscaminasService;
    
    @GetMapping("/")
    public String redirectToGame() {
        return "redirect:/buscaminas";
    }
    
    @GetMapping("/buscaminas")
    public String mostrarJuego(Model model, HttpSession session) {
        JuegoDto juego = (JuegoDto) session.getAttribute("juego");
        
        if (juego == null) {
            juego = buscaminasService.iniciarJuego();
            session.setAttribute("juego", juego);
        }
        
        model.addAttribute("juego", juego);
        return "buscaminas";
    }
    
    @PostMapping("/buscaminas/click")
    @ResponseBody
    public JuegoDto procesarClick(@RequestParam int x, @RequestParam int y, HttpSession session) {
        JuegoDto juego = (JuegoDto) session.getAttribute("juego");
        
        if (juego != null) {
            juego = buscaminasService.procesarClick(juego, x, y);
            session.setAttribute("juego", juego);
        }
        
        return juego;
    }
    
    @PostMapping("/buscaminas/reiniciar")
    public String reiniciarJuego(HttpSession session) {
        session.removeAttribute("juego");
        return "redirect:/buscaminas";
    }
    
    @GetMapping("/buscaminas/historial")
    @ResponseBody
    public List<Movimiento> obtenerHistorial() {
        return buscaminasService.obtenerHistorialMovimientos();
    }
}
