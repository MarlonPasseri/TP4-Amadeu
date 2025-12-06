package com.example.tp4integrado.application.pedido;

import com.example.tp4integrado.application.BusinessException;
import com.example.tp4integrado.application.catalogo.CatalogoService;
import com.example.tp4integrado.domain.pedido.Pedido;
import com.example.tp4integrado.domain.pedido.PedidoRepository;
import com.example.tp4integrado.domain.produto.Produto;
import com.example.tp4integrado.domain.shared.StockQuantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceImplTest {

    private PedidoRepository pedidoRepository;
    private CatalogoService catalogoService;
    private PedidoServiceImpl service;

    @BeforeEach
    void setUp() {
        pedidoRepository = mock(PedidoRepository.class);
        catalogoService = mock(CatalogoService.class);
        service = new PedidoServiceImpl(pedidoRepository, catalogoService);
    }

    @Test
    void criaPedidoEAtualizaEstoque() {
        Produto produto = new Produto();
        produto.definirQuantidade(StockQuantity.of(10));

        when(catalogoService.buscarPorId(1L)).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            return p;
        });

        Pedido pedido = service.criarPedido(1L, 3);

        assertNotNull(pedido);
        ArgumentCaptor<Produto> produtoCaptor = ArgumentCaptor.forClass(Produto.class);
        verify(catalogoService).salvar(produtoCaptor.capture());
        assertEquals(7, produtoCaptor.getValue().getQuantidade().toInt());
    }

    @Test
    void naoPermiteQuantidadeZero() {
        assertThrows(BusinessException.class, () -> service.criarPedido(1L, 0));
    }

    @Test
    void falhaQuandoEstoqueInsuficiente() {
        Produto produto = new Produto();
        produto.definirQuantidade(StockQuantity.of(1));
        when(catalogoService.buscarPorId(1L)).thenReturn(Optional.of(produto));
        assertThrows(BusinessException.class, () -> service.criarPedido(1L, 5));
    }
}
