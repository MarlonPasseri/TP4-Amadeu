package com.example.tp4integrado.domain.shared;

import java.util.Objects;

/**
 * Value Object imutável que representa uma quantidade em estoque.
 * Encapsula um inteiro e garante regras de domínio.
 */
public final class StockQuantity {

    private final int value;

    private StockQuantity(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }
        this.value = value;
    }

    public static StockQuantity of(int value) {
        return new StockQuantity(value);
    }

    public int toInt() {
        return value;
    }

    public StockQuantity plus(StockQuantity other) {
        return new StockQuantity(Math.addExact(this.value, other.value));
    }

    public StockQuantity minus(StockQuantity other) {
        int result = this.value - other.value;
        if (result < 0) {
            throw new IllegalArgumentException("Resultado de quantidade não pode ser negativo.");
        }
        return new StockQuantity(result);
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isLessThan(StockQuantity other) {
        return this.value < other.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockQuantity)) return false;
        StockQuantity that = (StockQuantity) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
