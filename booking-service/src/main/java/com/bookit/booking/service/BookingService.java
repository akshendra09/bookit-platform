package com.bookit.booking.service;

import com.bookit.booking.event.BookingCreatedEvent;
import com.bookit.booking.messaging.BookingEventProducer;
import com.bookit.booking.model.Booking;
import com.bookit.booking.repository.BookingRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingEventProducer eventProducer;

    public BookingService(BookingRepository bookingRepository, BookingEventProducer eventProducer) {
        this.bookingRepository = bookingRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        List<Booking> conflicts = bookingRepository
                .findByVenueIdAndSlotStartLessThanAndSlotEndGreaterThan(
                        booking.getVenueId(), booking.getSlotEnd(), booking.getSlotStart());
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Slot already booked for this venue");
        }

        try {
            Booking saved = bookingRepository.save(booking);
            eventProducer.publish(new BookingCreatedEvent(
                    saved.getId(), saved.getUserId(), saved.getVenueId(),
                    saved.getSlotStart(), saved.getSlotEnd()));
            return saved;
        } catch (ObjectOptimisticLockingFailureException e) {
            // Another concurrent request won the race for this slot.
            throw new IllegalStateException("Slot was just booked by someone else, please retry", e);
        }
    }
}
