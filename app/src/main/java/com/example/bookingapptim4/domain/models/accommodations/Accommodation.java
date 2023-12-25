package com.example.bookingapptim4.domain.models.accommodations;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.bookingapptim4.domain.models.shared.Image;
import com.example.bookingapptim4.domain.models.users.Host;

import java.util.ArrayList;
import java.util.List;

public class Accommodation implements Parcelable {

    private Long id;
    private String name;
    private String description;
    private Host host;
    private Location location;
    private List<Amenity> amenities;
    private List<AvailablePeriod> availablePeriods;
    private Integer minGuests;
    private Integer maxGuests;
    private Boolean pricingPerPerson;
    private AccommodationType type;
    private boolean isAutomaticallyAccepted;
    private boolean isBeingEdited;
    private List<Image> images;

    public Accommodation(Long id, String name, String description, Host host, Location location, List<Amenity> amenities, List<AvailablePeriod> availablePeriods, Integer minGuests, Integer maxGuests, Boolean pricingPerPerson, AccommodationType type, boolean isAutomaticallyAccepted, boolean isBeingEdited, List<Image> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.host = host;
        this.location = location;
        this.amenities = amenities;
        this.availablePeriods = availablePeriods;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.pricingPerPerson = pricingPerPerson;
        this.type = type;
        this.isAutomaticallyAccepted = isAutomaticallyAccepted;
        this.isBeingEdited = isBeingEdited;
        this.images = images;
    }

    public Accommodation( String name, String description, Host host, Location location, List<Amenity> amenities, Integer minGuests, Integer maxGuests, Boolean pricingPerPerson, AccommodationType type) {
        this.name = name;
        this.description = description;
        this.host = host;
        this.location = location;
        this.amenities = amenities;
        this.availablePeriods = new ArrayList<>();
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.pricingPerPerson = pricingPerPerson;
        this.type = type;
        this.isAutomaticallyAccepted = false;
        this.isBeingEdited = false;
        this.images = new ArrayList<>();
    }

    protected Accommodation(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        host = in.readParcelable(Host.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());

        // Read ArrayList instead of List
        amenities = new ArrayList<>();
        in.readList(amenities, Amenity.class.getClassLoader());

        // Read ArrayList instead of List
        availablePeriods = new ArrayList<>();
        in.readList(availablePeriods, AvailablePeriod.class.getClassLoader());

        minGuests = in.readInt();
        maxGuests = in.readInt();
        pricingPerPerson = in.readByte() != 0;
        type = (AccommodationType) in.readSerializable();
        isAutomaticallyAccepted = in.readByte() != 0;
        isBeingEdited = in.readByte() != 0;

        // Read ArrayList instead of List
        images = new ArrayList<>();
        in.readList(images, Image.class.getClassLoader());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Host getHost() {
        return host;
    }

    public Location getLocation() {
        return location;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public List<AvailablePeriod> getAvailablePeriods() {
        return availablePeriods;
    }

    public Integer getMinGuests() {
        return minGuests;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public Boolean getPricingPerPerson() {
        return pricingPerPerson;
    }

    public AccommodationType getType() {
        return type;
    }

    public boolean isAutomaticallyAccepted() {
        return isAutomaticallyAccepted;
    }

    public boolean isBeingEdited() {
        return isBeingEdited;
    }

    public void setBeingEdited(boolean beingEdited) {
        isBeingEdited = beingEdited;
    }

    public List<Image> getImages() {
        return images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeParcelable(host, flags);
        dest.writeParcelable(location, flags);

        // Convert List to ArrayList before writing
        dest.writeList(new ArrayList<>(amenities));

        // Convert List to ArrayList before writing
        dest.writeList(new ArrayList<>(availablePeriods));

        dest.writeInt(minGuests);
        dest.writeInt(maxGuests);
        /*dest.writeByte((byte) (pricingPerPerson ? 1 : 0));*/
        dest.writeSerializable(type);
        dest.writeByte((byte) (isAutomaticallyAccepted ? 1 : 0));
        dest.writeByte((byte) (isBeingEdited ? 1 : 0));

        // Convert List to ArrayList before writing
        dest.writeList(new ArrayList<>(images));
    }

    public static final Creator<Accommodation> CREATOR = new Creator<Accommodation>() {
        @Override
        public Accommodation createFromParcel(Parcel in) {
            return new Accommodation(in);
        }

        @Override
        public Accommodation[] newArray(int size) {
            return new Accommodation[size];
        }
    };
}
