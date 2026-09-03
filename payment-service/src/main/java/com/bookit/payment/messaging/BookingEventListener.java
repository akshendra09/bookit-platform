package com.bookit.payment.messaging;

import com.bookit.payment.event.BookingCreatedEvent;
import com.bookit.payment.model.Payment;
import com.bookit.payment.model.PaymentStatus;
import com.bookit.payment.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// Saga participant: reacts to booking.created, attempts payment, and would
// publish payment.succeeded / payment.failed for the booking service (and
// notification service) to react to in turn. Kept intentionally simple here
// so it's easy to swap in a real payment gateway call.
@Component
public class BookingEventListener {

    private final PaymentRepository paymentRepository;

    public BookingEventListener(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @KafkaListener(topics = "booking.created", groupId = "payment-service")
    public void onBookingCreated(BookingCreatedEvent event) {
        Payment payment = new Payment();
        payment.setBookingId(event.bookingId());
        payment.setUserId(event.userId());

        // TODO: call a real payment gateway (Stripe/Razorpay) here.
        boolean paymentSucceeded = true;
        payment.setStatus(paymentSucceeded ? PaymentStatus.SUCCEEDED : PaymentStatus.FAILED);

        paymentRepository.save(payment);
        // TODO: publish payment.succeeded / payment.failed to Kafka for the
        // booking service to update status and the notification service to alert the user.
    }
}
