package com.example.tp4integrado.application.catalogo;

import com.example.tp4integrado.domain.produto.Produto;

import java.util.List;
import java.util.Optional;

/**
 * Porta de entrada para o sistema de Catálogo.
 * Expõe apenas as operações necessárias para outros módulos/sistemas.
 */
public interface CatalogoService {

    List<Produto> listarTodos();

    Optional<Produto> buscarPorId(Long id);

    Produto salvar(Produto produto);

    void excluir(Long id);
}
