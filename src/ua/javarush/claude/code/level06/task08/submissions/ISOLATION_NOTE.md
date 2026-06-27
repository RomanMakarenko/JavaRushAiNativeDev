# Isolation Note

**Directory:** `../commerce-hotfix` (worktree)
**Branch:** `hotfix/refund-label`
**Changed files:**
- `src/main/resources/messages.properties` — `refund.amount.label=Refund amount` → `Returned amount`
- `src/test/java/com/example/commerce/refund/RefundMessagesTest.java` — очікуваний текст у тесті приведено до `"Returned amount"`