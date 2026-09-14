package cashflow.subscription;

import java.time.LocalDate;

/**
 * Перемикання тарифу підписки. Прикордонний випадок — switch у день білінгу:
 * порядок застосування нової ціни та списання платежу тут неочевидний і не покритий тестами.
 */
public class PlanSwitchService {

    // Зміна тарифу. Якщо switch припадає на billing day, нова ціна
    // застосовується негайно, але перевірки на подвійне списання немає.
    public void switchPlan(Subscription sub, long newPriceCents, LocalDate billingDay, LocalDate switchDay) {
        boolean onBillingDay = switchDay.equals(billingDay);
        sub.setMonthlyPriceCents(newPriceCents);
        // edge case: при onBillingDay поведінка proration не визначена явно
    }
}