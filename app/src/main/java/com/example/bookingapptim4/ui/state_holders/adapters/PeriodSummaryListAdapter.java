package com.example.bookingapptim4.ui.state_holders.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.summaries.AccommodationPeriodData;

import java.util.ArrayList;
import java.util.List;

public class PeriodSummaryListAdapter extends ArrayAdapter<AccommodationPeriodData> {
    private List<AccommodationPeriodData> accommodationPeriodDataList;

    public PeriodSummaryListAdapter(Context context, List<AccommodationPeriodData> accommodationPeriodDataList) {
        super(context, R.layout.period_summary_item, accommodationPeriodDataList);
        this.accommodationPeriodDataList = accommodationPeriodDataList;
    }

    @Override
    public int getCount() {
        if (accommodationPeriodDataList != null) {
            return accommodationPeriodDataList.size();
        } else {
            return 0;
        }
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public AccommodationPeriodData getItem(int position) {
        return accommodationPeriodDataList.get(position);
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
        AccommodationPeriodData accommodationPeriodData = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.period_summary_item, parent, false);
        }

        TextView accommodationName = convertView.findViewById(R.id.periodSummaryItemAccommodationName);
        TextView numberOfReservations = convertView.findViewById(R.id.periodSummaryItemNumberOfReservations);
        TextView totalProfit = convertView.findViewById(R.id.periodSummaryItemTotalProfit);

        if (accommodationPeriodData != null) {

            accommodationName.setText(accommodationPeriodData.getAccommodationName());

            String reservationsText = String.format("Reservations: %s", accommodationPeriodData.getReservationsCount());
            numberOfReservations.setText(reservationsText);

            String profitText = String.format("Profit: %s€", accommodationPeriodData.getTotalProfit());
            totalProfit.setText(profitText);

        }

        return convertView;
    }

}
