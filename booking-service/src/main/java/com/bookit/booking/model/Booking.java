package com.bookit.booking.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long venueId;
    private Long userId;
    private Instant slotStart;
    private Instant slotEnd;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING_PAYMENT;

    // Optimistic locking: prevents two concurrent requests from double-booking
    // the same slot. A second writer gets an OptimisticLockException and retries
    // or fails cleanly instead of silently overwriting the first booking.
    @Version
    private Long version;

    public Long getId() { return id; }
    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Instant getSlotStart() { return slotStart; }
    public void setSlotStart(Instant slotStart) { this.slotStart = slotStart; }
    public Instant getSlotEnd() { return slotEnd; }
    public void setSlotEnd(Instant slotEnd) { this.slotEnd = slotEnd; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}
