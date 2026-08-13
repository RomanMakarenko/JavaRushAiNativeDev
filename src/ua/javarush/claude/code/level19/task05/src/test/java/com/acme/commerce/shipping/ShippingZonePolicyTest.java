package com.acme.commerce.shipping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

// Юніт-тести у стилі проєкту: без Spring context, на основі new ShippingZonePolicy().
class ShippingZonePolicyTest {

    private final ShippingZonePolicy policy = new ShippingZonePolicy();

    @Test
    void resolvesEuropeanZoneForSupportedCountry() {
        Optional<String> zone = policy.resolveZone("DE");

        assertThat(zone).contains("ZONE_EU");
    }

    @Test
    void returnsEmptyZoneForUnsupportedCountry() {
        Optional<String> zone = policy.resolveZone("GB");

        assertThat(zone).isEmpty();
    }
}