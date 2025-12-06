package com.example.tp4integrado.domain.pedido;

import com.example.tp4integrado.domain.produto.Produto;
import com.example.tp4integrado.domain.shared.StockQuantity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidade de Pedido/Reserva de produto.
 * Representa o segundo sistema que consome o catálogo e manipula estoque.
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Produto produto;

    private Integer quantidadeRaw;

    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidadeRaw() {
        return quantidadeRaw;
    }

    public void setQuantidadeRaw(Integer quantidadeRaw) {
        this.quantidadeRaw = quantidadeRaw;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    // API de domínio com Value Object
    public StockQuantity getQuantidade() {
        return quantidadeRaw == null ? StockQuantity.of(0) : StockQuantity.of(quantidadeRaw);
    }

    public void definirQuantidade(StockQuantity quantidade) {
        this.quantidadeRaw = quantidade.toInt();
    }
}
