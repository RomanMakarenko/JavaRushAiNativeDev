package com.example.commerce.refunds;

import java.util.Objects;

/** Сервіс додавання коментарів до заявки на повернення. */
public class RefundCommentService {

    /**
     * Додає коментар до заявки.
     *
     * УВАГА: тут root cause багу — під час додавання коментаря статус
     * заявки помилково перезаписується значенням NEW. Коментар не повинен
     * впливати на статус.
     */
    public void addComment(RefundRequest request, String comment) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(comment, "comment");

        request.getComments().add(comment);
        // BUG: статус скидається, хоча додавання коментаря не змінює життєвий цикл заявки.
        request.setStatus(RefundStatus.NEW);
    }
}