package io.shiftleft.controller;

import io.shiftleft.model.Order;
import io.shiftleft.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;


@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private static final String apiKey = "your_stripe_api_secret_key";

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder != null) {
            existingOrder.setOrderDetails(order.getOrderDetails());
            existingOrder.setPaymentDetails(order.getPaymentDetails());
            existingOrder.setDeliveryDetails(order.getDeliveryDetails());

            Order updatedOrderResult = orderRepository.save(existingOrder);
            log.info("Order updated: {}", updatedOrderResult.toString());
            return updatedOrderResult;
        } else {
            log.error("Order not found with ID: {}", id);
            throw new IllegalArgumentException("Order not found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        log.info("Order deleted with ID: {}", id);
    }

    @PostMapping("/{id}/pay")
    public Order payForOrder(@PathVariable Long id, @RequestBody PaymentRequest paymentRequest) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder != null) {
            try {
                ChargeCreateParams params = ChargeCreateParams.builder()
                        .setOrderId(paymentRequest.getOrderById())
                        .setPaymentDetails(paymentRequest.getPaymentMode())
                        .setAmount(paymentRequest.getAmount()) // Payment amount in cents
                        .setCurrency(paymentRequest.getCurrency())
                        .setDescription(paymentRequest.getDescription())
                        .setSource(paymentRequest.getStripeToken()) // Token obtained from Stripe.js or mobile SDK
                        .setCookies(paymentRequest.cookies)
                        .build();

                Charge charge = Charge.create(params);

                // Update order status or perform other necessary actions

                log.info("Payment successful. Charge ID: {}", charge.getId());
            } catch (StripeException e) {
                log.error("Payment failed: {}", e.getMessage());
                // Handle payment failure scenario
            }

            return existingOrder;
        } else {
            log.error("Order not found with ID: {}", id);
            throw new IllegalArgumentException("Order not found");
        }
    }

}
