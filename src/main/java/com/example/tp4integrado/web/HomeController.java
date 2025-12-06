package com.example.tp4integrado.web;

import com.example.tp4integrado.application.catalogo.CatalogoService;
import com.example.tp4integrado.application.pedido.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Dashboard simples que integra informações dos dois sistemas.
 */
@Controller
public class HomeController {

    private final CatalogoService catalogoService;
    private final PedidoService pedidoService;

    public HomeController(CatalogoService catalogoService, PedidoService pedidoService) {
        this.catalogoService = catalogoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("produtos", catalogoService.listarTodos());
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "dashboard";
    }
}
