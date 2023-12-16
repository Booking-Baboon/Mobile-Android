package com.example.bookingapptim4.ui.state_holders.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.Amenity;

import java.util.ArrayList;

public class AmenityListAdapter extends ArrayAdapter<Amenity> {
    private ArrayList<Amenity> amenities;
    public AmenityListAdapter(@NonNull Context context, ArrayList<Amenity> amenities) {
        super(context, R.layout.fragment_accommodation_details_screen, amenities);
        this.amenities = amenities;
    }
    @Override
    public int getCount() {
        return amenities.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Amenity getItem(int position) {
        return amenities.get(position);
    }

    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra. Naravno mozemo iskoristiti i
     * jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Ova metoda popunjava pojedinacan element ListView-a podacima.
     * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
     * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
     * uzece java objekat sa odredjene poziciuje (model) koji cuva podatke,
     * i layout koji treba da prikaze te podatke (view) npr R.layout.product_card.
     * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
     * popuniti view podacima i poslati listview da prikaze, i nastavice
     * sledecu iteraciju.
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Amenity amenity = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.amenity_item,
                    parent, false);
        }
        RelativeLayout amenityItem = convertView.findViewById(R.id.amenity_item);
        TextView amenityName = convertView.findViewById(R.id.amenity_name);

        if (amenity != null) {
            amenityName.setText(amenity.getName());
        }

        return convertView;
    }
}
