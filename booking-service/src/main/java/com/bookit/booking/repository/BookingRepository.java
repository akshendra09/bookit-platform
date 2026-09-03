package com.bookit.booking.repository;

import com.bookit.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Used to check slot conflicts before inserting a new booking.
    List<Booking> findByVenueIdAndSlotStartLessThanAndSlotEndGreaterThan(
            Long venueId, Instant slotEnd, Instant slotStart);
}
