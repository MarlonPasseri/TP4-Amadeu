package com.example.tp4integrado.application.pedido;

import com.example.tp4integrado.domain.pedido.Pedido;

import java.util.List;

public interface PedidoService {

    List<Pedido> listarTodos();

    Pedido criarPedido(Long produtoId, int quantidade);
}
