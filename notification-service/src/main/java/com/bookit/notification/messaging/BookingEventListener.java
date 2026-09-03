package com.bookit.notification.messaging;

import com.bookit.notification.event.BookingCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BookingEventListener {

    private static final Logger log = LoggerFactory.getLogger(BookingEventListener.class);

    @KafkaListener(topics = "booking.created", groupId = "notification-service")
    public void onBookingCreated(BookingCreatedEvent event) {
        // TODO: replace with a real email/SMS provider call.
        log.info("Sending booking confirmation email for booking {} to user {}",
                event.bookingId(), event.userId());
    }
}
