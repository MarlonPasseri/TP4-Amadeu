package com.example.tp4integrado.domain.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object imutável que representa um valor monetário.
 * Substitui o uso direto de BigDecimal em regras de negócio.
 */
public final class Money {

    private final BigDecimal value;

    private Money(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Valor monetário não pode ser nulo.");
        }
        this.value = value.setScale(2, RoundingMode.HALF_UP);
        if (this.value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor monetário não pode ser negativo.");
        }
    }

    public static Money of(BigDecimal value) {
        return new Money(value);
    }

    public static Money of(double value) {
        return of(BigDecimal.valueOf(value));
    }

    public BigDecimal toBigDecimal() {
        return value;
    }

    public Money plus(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money minus(Money other) {
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Resultado monetário não pode ser negativo.");
        }
        return new Money(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
