package com.acme.analytics;

import com.acme.support.api.TicketClient;

// Контролер аналітики: отримує кількість відкритих тикетів через support-api.
public class AnalyticsController {

    private final TicketClient ticketClient;

    public AnalyticsController(TicketClient ticketClient) {
        this.ticketClient = ticketClient;
    }

    // Повертає кількість відкритих звернень для дашборда.
    public int openTicketsCount() {
        return ticketClient.countOpenTickets();
    }
}