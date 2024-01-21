package com.example.bookingapptim4.domain.dtos.accommodations;

import com.example.bookingapptim4.domain.models.accommodations.AccommodationModificationType;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationType;
import com.example.bookingapptim4.domain.models.accommodations.AmenityReference;
import com.example.bookingapptim4.domain.models.accommodations.Location;
import com.example.bookingapptim4.domain.models.users.HostReference;

import java.util.HashSet;
import java.util.Set;

public class AccommodationModificationCreateRequest {
    private AmenityReference accommodation;
    private String name;
    private String description;
    private HostReference host;
    private Location location;
    private Set<AmenityReference> amenities;
    private Set<AmenityReference> availablePeriods;
    private Integer minGuests;
    private Integer maxGuests;
    private Boolean isPricingPerPerson;
    private AccommodationType type;
    private boolean isAutomaticallyAccepted;
    private AccommodationModificationType requestType;

    public AccommodationModificationCreateRequest() {
    }

    public AccommodationModificationCreateRequest(AmenityReference accommodation, String name, String description, HostReference host, Location location, Set<AmenityReference> amenities, Integer minGuests, Integer maxGuests, Boolean isPricingPerPerson, AccommodationType type, AccommodationModificationType modificationType) {
        this.accommodation = accommodation;
        this.name = name;
        this.description = description;
        this.host = host;
        this.location = location;
        this.amenities = amenities;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.isPricingPerPerson = isPricingPerPerson;
        this.type = type;
        this.requestType = modificationType;
    }
}
