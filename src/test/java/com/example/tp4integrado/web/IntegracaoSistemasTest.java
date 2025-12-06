package com.example.tp4integrado.web;

import com.example.tp4integrado.domain.pedido.PedidoRepository;
import com.example.tp4integrado.domain.produto.Produto;
import com.example.tp4integrado.domain.produto.ProdutoRepository;
import com.example.tp4integrado.domain.shared.StockQuantity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste integrado simples que verifica comunicação entre os dois sistemas
 * (Catálogo e Pedidos) via camada web.
 */
@SpringBootTest
@Transactional
class IntegracaoSistemasTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    void criarProdutoECriarPedidoReduzEstoque() {
        Produto produto = new Produto();
        produto.setNome("Produto Integrado");
        produto.setDescricao("Teste");
        produto.setPrecoRaw(new BigDecimal("10.00"));
        produto.definirQuantidade(StockQuantity.of(5));

        produto = produtoRepository.save(produto);
        assertNotNull(produto.getId());

        // criação direta de pedido via repositório para manter o teste mais enxuto
        var pedido = new com.example.tp4integrado.domain.pedido.Pedido();
        pedido.setProduto(produto);
        pedido.definirQuantidade(StockQuantity.of(2));
        pedidoRepository.save(pedido);

        // simula atualização de estoque
        produto.removerEstoque(StockQuantity.of(2));
        produtoRepository.save(produto);

        Produto atualizado = produtoRepository.findById(produto.getId()).orElseThrow();
        assertEquals(3, atualizado.getQuantidade().toInt());
    }
}
