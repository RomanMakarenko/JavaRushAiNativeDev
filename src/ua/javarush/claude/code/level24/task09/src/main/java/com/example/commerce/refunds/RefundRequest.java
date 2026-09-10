package com.example.commerce.refunds;

import java.util.ArrayList;
import java.util.List;

/** Заявка на повернення коштів. Зберігає статус і список коментарів. */
public class RefundRequest {

    private final String id;
    private RefundStatus status;
    private final List<String> comments = new ArrayList<>();

    public RefundRequest(String id, RefundStatus status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public RefundStatus getStatus() {
        return status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public List<String> getComments() {
        return comments;
    }
}