package com.example.commerce.refunds;

/** Контролер заявок на повернення. */
public class RefundController {

    private final RefundCommentService commentService;

    public RefundController(RefundCommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Додає коментар і повертає актуальний статус заявки.
     */
    public RefundStatus addComment(RefundRequest request, String comment) {
        // SYMPTOM PATCH: запам'ятовуємо статус до виклику сервісу і підміняємо його
        // у відповіді, щоб користувач не бачив помилковий скидання в NEW.
        // Це маскує баг у RefundCommentService, а не виправляє його.
        RefundStatus statusBefore = request.getStatus();
        commentService.addComment(request, comment);
        return statusBefore;
    }
}