package com.example.bookingapptim4.ui.state_holders.adapters;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.reviews.AccommodationReviewUtils;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationModification;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationModificationStatus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationModificationListAdapter extends ArrayAdapter<AccommodationModification> {
    private ArrayList<AccommodationModification> accommodationModifications;
    private OnApproveButtonClickListener approveButtonClickListener;
    private OnDenyButtonClickListener denyButtonClickListener;
    public AccommodationModificationListAdapter(Context context, ArrayList<AccommodationModification> accommodationModifications) {
        super(context, R.layout.accommodation_modification_card, accommodationModifications);
        this.accommodationModifications = accommodationModifications;
    }

    @Override
    public int getCount() {
        if (accommodationModifications != null) {
            return accommodationModifications.size();
        } else {
            return 0;
        }
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public AccommodationModification getItem(int position) {
        return accommodationModifications.get(position);
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

    public void setOnApproveButtonClickListener(OnApproveButtonClickListener listener) {
        this.approveButtonClickListener = listener;
    }
    public void setOnDenyButtonClickListener(OnDenyButtonClickListener listener) {
        this.denyButtonClickListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        AccommodationModification accommodationModification = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_modification_card,
                    parent, false);
        }
        LinearLayout accommodationModificationCard = convertView.findViewById(R.id.accommodation_modification_card);
        TextView accommodationModificationName = convertView.findViewById(R.id.accommodation_modification_title);
        TextView accommodationModificationLocation = convertView.findViewById(R.id.accommodation_modification_location);
        TextView accommodationModificationRating = convertView.findViewById(R.id.accommodation_modification_rating);
        TextView accommodationModificationStatus = convertView.findViewById(R.id.accommodation_modification_status);
        TextView accommodationModificationRequestType = convertView.findViewById(R.id.accommodation_modification_request_type);


        if (accommodationModification != null) {
            accommodationModificationName.setText(accommodationModification.getName());
            accommodationModificationStatus.setText(accommodationModification.getStatus().toString());
            accommodationModificationRequestType.setText(accommodationModification.getRequestType().toString());

            String locationText = String.format("%s, %s, %s",
                    accommodationModification.getLocation().getCountry(),
                    accommodationModification.getLocation().getCity(),
                    accommodationModification.getLocation().getAddress());
            accommodationModificationLocation.setText(locationText);

            loadAverageRating(accommodationModification.getAccommodation().getId(),accommodationModificationRating);

            accommodationModificationCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                /*Bundle bundle = new Bundle();
                bundle.putParcelable("selectedAccommodationModification", accommodationModification);

                Navigation.findNavController(v).navigate(R.id.nav_accommodationModification_details, bundle);*/
            });
        }

        Button approveButton = convertView.findViewById(R.id.accommodation_modification_approve_button);
        Button denyButton = convertView.findViewById(R.id.accommodation_modification_deny_button);

        if (accommodationModification != null && accommodationModification.getStatus().equals(AccommodationModificationStatus.Pending)) {
            approveButton.setEnabled(true);
            denyButton.setEnabled(true);
        } else {
            approveButton.setEnabled(false);
            denyButton.setEnabled(false);
        }

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (approveButtonClickListener != null) {
                    approveButtonClickListener.onApproveButtonClick(getItem(position));
                }
            }
        });



        denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (denyButtonClickListener != null) {
                    denyButtonClickListener.onDenyButtonClick(getItem(position));
                }
            }
        });


        return convertView;
    }

    public void updateModificationStatus(long modificationId, AccommodationModificationStatus status) {
        for (AccommodationModification modification : accommodationModifications) {
            if (modification.getId() == modificationId) {
                modification.setStatus(status);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public interface OnApproveButtonClickListener {
        void onApproveButtonClick(AccommodationModification modification);
    }
    public interface OnDenyButtonClickListener {
        void onDenyButtonClick(AccommodationModification modification);
    }
    private void loadAverageRating(Long accommodationId, TextView accommodationRating){
        Call<Float> call = AccommodationReviewUtils.accommodationReviewService.getAverateRating(accommodationId);
        call.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if (response.code() == 200){
                    Log.d("AccommodationReviewUtils","Meesage recieved");
                    if(response.body() == -1){
                        accommodationRating.setText("No reviews");
                    }else{
                        accommodationRating.setText(response.body().toString());
                    }

                }else{
                    Log.d("AccommodationReviewUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Float> call, Throwable t) {
                Log.d("AccommodationReviewUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}
