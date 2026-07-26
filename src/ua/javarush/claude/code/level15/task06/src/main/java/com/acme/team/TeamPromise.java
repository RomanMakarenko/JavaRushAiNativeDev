package com.acme.team;

/**
 * Обіцянка, яку conceptual team нібито дає за підсумками investigation.
 * Використовується валідатором, щоб відсіяти занадто широкі гарантії.
 */
public enum TeamPromise {

    /** Безпечно: команда лише проводить read-only investigation. */
    READ_ONLY_INVESTIGATION,

    /** Небезпечно: обіцянка автоматично злити зміни без участі людини. */
    AUTO_MERGE,

    /** Небезпечно: обіцянка доставити зміни без review. */
    NO_REVIEW,

    /** Небезпечно: обіцянка автономної доставки прямо в production. */
    AUTONOMOUS_PRODUCTION
}