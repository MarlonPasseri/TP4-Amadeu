package com.example.tp4integrado.web;

import com.example.tp4integrado.application.catalogo.CatalogoService;
import com.example.tp4integrado.domain.produto.Produto;
import com.example.tp4integrado.domain.shared.Money;
import com.example.tp4integrado.domain.shared.StockQuantity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final CatalogoService catalogoService;

    public ProdutoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", catalogoService.listarTodos());
        return "produtos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Produto produto = new Produto();
        produto.alterarPreco(Money.of(0.0));
        produto.definirQuantidade(StockQuantity.of(0));
        model.addAttribute("produto", produto);
        return "produtos/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute("produto") Produto produto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "produtos/form";
        }

        if (produto.getPrecoRaw() == null) {
            produto.setPrecoRaw(BigDecimal.ZERO);
        }
        if (produto.getQuantidadeRaw() == null) {
            produto.setQuantidadeRaw(0);
        }

        catalogoService.salvar(produto);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto salvo com sucesso!");
        return "redirect:/produtos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Produto produto = catalogoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
        model.addAttribute("produto", produto);
        return "produtos/form";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("produto") Produto produto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "produtos/form";
        }
        produto.setId(id);
        catalogoService.salvar(produto);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto atualizado com sucesso!");
        return "redirect:/produtos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        catalogoService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto excluído com sucesso!");
        return "redirect:/produtos";
    }
}
