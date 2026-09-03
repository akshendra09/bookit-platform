package com.bookit.notification.event;

import java.time.Instant;

public record BookingCreatedEvent(
        Long bookingId,
        Long userId,
        Long venueId,
        Instant slotStart,
        Instant slotEnd
) {}
