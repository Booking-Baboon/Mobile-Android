package com.example.bookingapptim4.domain.models.accommodations;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.example.bookingapptim4.domain.models.shared.Image;
import com.example.bookingapptim4.domain.models.users.Host;

public class AccommodationModification {
    private Long id;
    private Accommodation accommodation;
    private String name;
    private String description;
    private Host host;
    private Location location;
    private Set<Amenity> amenities;
    private List<AvailablePeriod> availablePeriods;
    private Integer minGuests;
    private Integer maxGuests;
    private Boolean pricingPerPerson;
    private AccommodationType type;
    private boolean isAutomaticallyAccepted;
    private List<Image> images;
    private Date requestDate;
    private AccommodationModificationStatus status = AccommodationModificationStatus.Pending;
    private AccommodationModificationType requestType;
    private Boolean isBeingEdited;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<Amenity> amenities) {
        this.amenities = amenities;
    }

    public List<AvailablePeriod> getAvailablePeriods() {
        return availablePeriods;
    }

    public void setAvailablePeriods(List<AvailablePeriod> availablePeriods) {
        this.availablePeriods = availablePeriods;
    }

    public Integer getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(Integer minGuests) {
        this.minGuests = minGuests;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public Boolean getPricingPerPerson() {
        return pricingPerPerson;
    }

    public void setPricingPerPerson(Boolean pricingPerPerson) {
        this.pricingPerPerson = pricingPerPerson;
    }

    public AccommodationType getType() {
        return type;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public boolean isAutomaticallyAccepted() {
        return isAutomaticallyAccepted;
    }

    public void setAutomaticallyAccepted(boolean automaticallyAccepted) {
        isAutomaticallyAccepted = automaticallyAccepted;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public AccommodationModificationStatus getStatus() {
        return status;
    }

    public void setStatus(AccommodationModificationStatus status) {
        this.status = status;
    }

    public AccommodationModificationType getRequestType() {
        return requestType;
    }

    public void setRequestType(AccommodationModificationType requestType) {
        this.requestType = requestType;
    }

    public Boolean getBeingEdited() {
        return isBeingEdited;
    }

    public void setBeingEdited(Boolean beingEdited) {
        isBeingEdited = beingEdited;
    }
}
