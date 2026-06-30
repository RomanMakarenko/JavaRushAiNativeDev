package com.acme.orders;

import org.springframework.stereotype.Service;

// Оформлення замовлення: резерв товару, авторизація платежу, збереження.
@Service
public class OrderService {

    private final InventoryService inventoryService;
    private final PaymentGateway paymentGateway;
    private final OrderRepository orderRepository;

    public OrderService(InventoryService inventoryService,
                        PaymentGateway paymentGateway,
                        OrderRepository orderRepository) {
        this.inventoryService = inventoryService;
        this.paymentGateway = paymentGateway;
        this.orderRepository = orderRepository;
    }

    public OrderResult placeOrder(OrderRequest request) {
        // УВАГА: поточний порядок неправильний — платіж авторизується до резерву товару.
        paymentGateway.authorize(request.paymentToken(), request.quantity());
        inventoryService.reserve(request.sku(), request.quantity());
        OrderEntity saved = orderRepository.save(
                new OrderEntity(request.sku(), request.quantity(), "CONFIRMED"));
        return new OrderResult(saved.status());
    }
}