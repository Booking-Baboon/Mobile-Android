package com.example.bookingapptim4.domain.models.accommodations;

import com.example.bookingapptim4.domain.models.users.HostReference;

import java.util.HashSet;
import java.util.Set;

public class AccommodationRequest {

        private String name;
        private String description;
        private HostReference host;
        private Location location;
        private Set<AmenityReference> amenities;
        private Set<AmenityReference> availablePeriods = new HashSet<>();
        private Integer minGuests;
        private Integer maxGuests;
        private Boolean isPricingPerPerson;
        private AccommodationType type;
        private boolean isAutomaticallyAccepted = false;

        public AccommodationRequest() {
        }

        public AccommodationRequest(String name, String description, HostReference host, Location location, Set<AmenityReference> amenities, Integer minGuests, Integer maxGuests, Boolean isPricingPerPerson, AccommodationType type) {
            this.name = name;
            this.description = description;
            this.host = host;
            this.location = location;
            this.amenities = amenities;
            this.minGuests = minGuests;
            this.maxGuests = maxGuests;
            this.isPricingPerPerson = isPricingPerPerson;
            this.type = type;
        }

}
