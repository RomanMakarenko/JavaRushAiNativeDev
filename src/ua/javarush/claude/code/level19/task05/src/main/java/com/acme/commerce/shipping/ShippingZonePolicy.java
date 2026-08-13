package com.acme.commerce.shipping;

import java.util.Optional;
import java.util.Set;

/**
 * Чиста бізнес-логіка вибору зони доставки.
 * Без БД, HTTP і Spring-залежностей — ідеальний кандидат для unit-тесту.
 */
public class ShippingZonePolicy {

    private static final Set<String> SUPPORTED_COUNTRIES = Set.of("DE", "FR", "PL", "US");

    /**
     * Повертає зону доставки для країни замовлення.
     * Якщо країна не підтримується — зона відсутня.
     */
    public Optional<String> resolveZone(String countryCode) {
        if (countryCode == null || !isSupported(countryCode)) {
            return Optional.empty();
        }
        return Optional.of(toZone(countryCode));
    }

    // Внутрішній helper — деталь реалізації, тести на нього опиратися не повинні.
    private boolean isSupported(String countryCode) {
        return SUPPORTED_COUNTRIES.contains(countryCode);
    }

    private String toZone(String countryCode) {
        return switch (countryCode) {
            case "US" -> "ZONE_NA";
            default -> "ZONE_EU";
        };
    }
}