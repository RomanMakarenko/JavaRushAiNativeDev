package com.acme.shared;

// Спільний value-об’єкт грошей. Використовується в кількох модулях: замовлення, платежі, звіти.
public record Money(long amountCents, String currency) {

    public Money {
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("currency є обов'язковим");
        }
    }

    // Додає дві суми в одній валюті.
    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(this.amountCents + other.amountCents, this.currency);
    }

    // Віднімає суми в одній валюті.
    public Money minus(Money other) {
        requireSameCurrency(other);
        return new Money(this.amountCents - other.amountCents, this.currency);
    }

    private void requireSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("невідповідність валют");
        }
    }
}