package com.example.bookingapptim4.domain.dtos.reservations;

import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.shared.TimeSlot;
import com.example.bookingapptim4.domain.models.users.Guest;

public class CreateReservationRequest {
    private Accommodation accommodation;
    private TimeSlot timeSlot;
    private Guest guest;
    private Float price;

    public CreateReservationRequest(Accommodation accommodation, TimeSlot timeSlot, Guest guest, Float price) {
        this.accommodation = accommodation;
        this.timeSlot = timeSlot;
        this.guest = guest;
        this.price = price;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
