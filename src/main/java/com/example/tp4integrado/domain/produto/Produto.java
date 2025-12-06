package com.example.tp4integrado.domain.produto;

import com.example.tp4integrado.domain.shared.Money;
import com.example.tp4integrado.domain.shared.StockQuantity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade de Produto pertencente ao sistema de Catálogo.
 * Utiliza objetos de valor (Money, StockQuantity) na lógica de domínio,
 * mantendo os atributos primitivos no modelo de persistência.
 */
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório.")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres.")
    private String nome;

    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres.")
    private String descricao;

    // campos primitivos que são encapsulados por Value Objects
    private BigDecimal precoRaw;

    private Integer quantidadeRaw;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = this.criadoEm;
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    // getters/setters de persistência
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPrecoRaw() { return precoRaw; }

    public void setPrecoRaw(BigDecimal precoRaw) { this.precoRaw = precoRaw; }

    public Integer getQuantidadeRaw() { return quantidadeRaw; }

    public void setQuantidadeRaw(Integer quantidadeRaw) { this.quantidadeRaw = quantidadeRaw; }

    public LocalDateTime getCriadoEm() { return criadoEm; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    // Métodos de domínio usando Value Objects

    public Money getPreco() {
        return precoRaw == null ? Money.of(0.0) : Money.of(precoRaw);
    }

    public void alterarPreco(Money novoPreco) {
        this.precoRaw = novoPreco.toBigDecimal();
    }

    public StockQuantity getQuantidade() {
        return quantidadeRaw == null ? StockQuantity.of(0) : StockQuantity.of(quantidadeRaw);
    }

    public void definirQuantidade(StockQuantity quantidade) {
        this.quantidadeRaw = quantidade.toInt();
    }

    public void adicionarEstoque(StockQuantity incremento) {
        definirQuantidade(getQuantidade().plus(incremento));
    }

    public void removerEstoque(StockQuantity decremento) {
        definirQuantidade(getQuantidade().minus(decremento));
    }
}
