package com.cashflow.mrr;

import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.assertEquals;

/**
 * Один-єдиний тест на найпростіший метод рушія.
 * computeMrr (ключова грошова логіка) тестами не покрито.
 */
public class MrrEngineTest {

    @Test
    public void annualizeMultipliesByTwelve() {
        MrrEngine engine = new MrrEngine();
        assertEquals(new BigDecimal("1200"), engine.annualize(new BigDecimal("100")));
    }
}