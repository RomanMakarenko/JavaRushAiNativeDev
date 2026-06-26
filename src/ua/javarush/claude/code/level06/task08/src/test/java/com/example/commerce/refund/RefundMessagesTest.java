package com.example.commerce.refund;

import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Перевіряє тексти інтерфейсу повернення (refund messages).
class RefundMessagesTest {

    private Properties loadMessages() throws Exception {
        Properties props = new Properties();
        try (InputStream in = getClass().getResourceAsStream("/messages.properties")) {
            props.load(in);
        }
        return props;
    }

    @Test
    void refundAmountLabelMatchesExpectedText() throws Exception {
        Properties props = loadMessages();
        assertEquals("Returned amount", props.getProperty("refund.amount.label"));
    }
}