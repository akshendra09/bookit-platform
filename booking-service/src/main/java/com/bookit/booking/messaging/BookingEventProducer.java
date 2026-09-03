package com.bookit.booking.messaging;

import com.bookit.booking.event.BookingCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingEventProducer {

    public static final String TOPIC = "booking.created";

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public BookingEventProducer(KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(BookingCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.bookingId().toString(), event);
    }
}
