package com.example.bookingapptim4.UI.Elements.Adapters;

import androidx.fragment.app.Fragment;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.UI.Elements.Activities.GuestMainScreen;
import com.example.bookingapptim4.UI.Elements.Activities.LoginScreen;
import com.example.bookingapptim4.UI.Elements.Fragments.AccommodationDetailsScreen;
import com.example.bookingapptim4.UI.Elements.Fragments.FragmentAccommodationList;
import com.example.bookingapptim4.UI.Elements.Fragments.FragmentTransition;
import com.example.bookingapptim4.UI.Elements.Models.Accommodation;

import java.util.ArrayList;

public class AccommodationListAdapter extends ArrayAdapter<Accommodation> {
    private ArrayList<Accommodation> accommodations;
    public AccommodationListAdapter(Context context, ArrayList<Accommodation> accommodations) {
        super(context, R.layout.accommodation_card, accommodations);
        this.accommodations = accommodations;
    }

    @Override
    public int getCount() {
        return accommodations.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Accommodation getItem(int position) {
        return accommodations.get(position);
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

        Accommodation accommodation = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_card,
                    parent, false);
        }
        LinearLayout accommodationCard = convertView.findViewById(R.id.accommodation_card);
        TextView accommodationName = convertView.findViewById(R.id.accommodation_title);
        TextView accommodationDescription = convertView.findViewById(R.id.accommodation_description);
        TextView accommodationLocation = convertView.findViewById(R.id.accommodation_location);
        TextView accommodationTotalPrice = convertView.findViewById(R.id.accommodation_total_price);
        TextView accommodationPricePerNight = convertView.findViewById(R.id.accommodation_price_per_night);
        TextView accommodationRating = convertView.findViewById(R.id.accommodation_rating);


        if(accommodation != null){
            accommodationName.setText(accommodation.getName());
            accommodationDescription.setText(accommodation.getDescription());
            accommodationLocation.setText(accommodation.getLocation());
            accommodationTotalPrice.setText(Integer.toString(accommodation.getPrice())  + "e");
            accommodationPricePerNight.setText(Integer.toString(accommodation.getPrice()) + "e/night");
            accommodationRating.setText("4.5");


            accommodationCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedAccommodation", accommodation);

                Navigation.findNavController(v).navigate(R.id.nav_accommodation_details, bundle);
            });

        }

        return convertView;
    }
}
