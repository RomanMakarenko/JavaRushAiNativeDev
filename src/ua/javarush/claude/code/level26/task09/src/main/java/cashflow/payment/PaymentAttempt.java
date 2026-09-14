package cashflow.payment;

public class PaymentAttempt {
    private int succeedOnAttempt;
    private int currentAttempt = 0;
    private boolean resolved = false;

    public PaymentAttempt(int succeedOnAttempt) {
        this.succeedOnAttempt = succeedOnAttempt;
    }

    public boolean charge() {
        currentAttempt++;
        return currentAttempt >= succeedOnAttempt;
    }

    public void markResolved() { this.resolved = true; }
    public boolean isResolved() { return resolved; }
}