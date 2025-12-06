package com.example.tp4integrado.domain.shared;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void naoPermiteValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(new BigDecimal("-1.00")));
    }

    @Test
    void somaValoresComPrecisao() {
        Money a = Money.of(10.00);
        Money b = Money.of(2.50);
        Money result = a.plus(b);
        assertEquals(new BigDecimal("12.50"), result.toBigDecimal());
    }

    @Test
    void subtraiSemFicarNegativo() {
        Money a = Money.of(10.00);
        Money b = Money.of(4.00);
        Money result = a.minus(b);
        assertEquals(new BigDecimal("6.00"), result.toBigDecimal());
        assertThrows(IllegalArgumentException.class, () -> b.minus(a));
    }
}
