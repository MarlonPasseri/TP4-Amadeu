package com.example.tp4integrado.domain.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockQuantityTest {

    @Test
    void naoPermiteNegativo() {
        assertThrows(IllegalArgumentException.class, () -> StockQuantity.of(-1));
    }

    @Test
    void adicionaESubtraiComRegras() {
        StockQuantity q1 = StockQuantity.of(10);
        StockQuantity q2 = StockQuantity.of(3);

        assertEquals(13, q1.plus(q2).toInt());
        assertEquals(7, q1.minus(q2).toInt());
        assertThrows(IllegalArgumentException.class, () -> q2.minus(q1));
    }
}
