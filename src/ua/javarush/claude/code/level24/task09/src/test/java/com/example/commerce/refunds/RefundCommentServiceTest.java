package com.example.commerce.refunds;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RefundCommentServiceTest {

    private final RefundCommentService service = new RefundCommentService();

    @Test
    void addCommentStoresComment() {
        RefundRequest request = new RefundRequest("R-1", RefundStatus.IN_REVIEW);

        service.addComment(request, "Прохання уточнити суму");

        assertTrue(request.getComments().contains("Прохання уточнити суму"));
    }
}