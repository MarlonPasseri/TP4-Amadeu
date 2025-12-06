package com.example.tp4integrado.application.pedido;

import com.example.tp4integrado.application.BusinessException;
import com.example.tp4integrado.application.catalogo.CatalogoService;
import com.example.tp4integrado.domain.pedido.Pedido;
import com.example.tp4integrado.domain.pedido.PedidoRepository;
import com.example.tp4integrado.domain.produto.Produto;
import com.example.tp4integrado.domain.shared.StockQuantity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Sistema de Pedidos integrado ao sistema de Catálogo via interface CatalogoService.
 */
@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CatalogoService catalogoService;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, CatalogoService catalogoService) {
        this.pedidoRepository = pedidoRepository;
        this.catalogoService = catalogoService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    @Transactional
    public Pedido criarPedido(Long produtoId, int quantidadeInt) {
        StockQuantity quantidadeSolicitada = StockQuantity.of(quantidadeInt);

        Produto produto = catalogoService.buscarPorId(produtoId)
                .orElseThrow(() -> new BusinessException("Produto não encontrado para criação de pedido."));

        if (quantidadeSolicitada.isLessThan(StockQuantity.of(1))) {
            throw new BusinessException("Quantidade do pedido deve ser pelo menos 1.");
        }

        if (produto.getQuantidade().isLessThan(quantidadeSolicitada)) {
            throw new BusinessException("Estoque insuficiente para o produto selecionado.");
        }

        // atualiza estoque (integração entre sistemas)
        produto.removerEstoque(quantidadeSolicitada);
        catalogoService.salvar(produto);

        Pedido pedido = new Pedido();
        pedido.setProduto(produto);
        pedido.definirQuantidade(quantidadeSolicitada);

        return pedidoRepository.save(pedido);
    }
}
