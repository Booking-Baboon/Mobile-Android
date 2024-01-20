package com.example.bookingapptim4.ui.state_holders.adapters;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.reviews.AccommodationReviewUtils;
import com.example.bookingapptim4.data_layer.repositories.shared.ImageUtils;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationModification;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationModificationStatus;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationListAdapter extends ArrayAdapter<Accommodation> {
    private ArrayList<Accommodation> accommodations;

    public AccommodationListAdapter(Context context, ArrayList<Accommodation> accommodations) {
        super(context, R.layout.accommodation_card, accommodations);
        this.accommodations = accommodations;
    }

    @Override
    public int getCount() {
        if (accommodations != null) {
            return accommodations.size();
        } else {
            return 0;
        }
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
        ImageView accommodationImage = convertView.findViewById(R.id.accommodation_image);


        if(!accommodation.getImages().isEmpty()){
            loadImage(accommodation.getImages().get(0).getId(),accommodationImage);
        } else {
            accommodationImage.setImageResource(R.drawable.accommodation_image_example);
        }


        if (accommodation != null) {
            accommodationName.setText(accommodation.getName());
            accommodationDescription.setText(accommodation.getDescription());

            String locationText = String.format("%s, %s, %s",
                    accommodation.getLocation().getCountry(),
                    accommodation.getLocation().getCity(),
                    accommodation.getLocation().getAddress());
            accommodationLocation.setText(locationText);

            accommodationTotalPrice.setText(Integer.toString(100) + "e");
            accommodationPricePerNight.setText(Integer.toString(20) + "e/night");

            Call<Float> call = AccommodationReviewUtils.accommodationReviewService.getAverateRating(accommodation.getId());
            call.enqueue(new Callback<Float>() {
                @Override
                public void onResponse(Call<Float> call, Response<Float> response) {
                    if (response.body() > 0) {
                        accommodationRating.setText(response.body().toString());
                    }
                    else {
                        accommodationRating.setText("No reviews");
                    }
                }

                @Override
                public void onFailure(Call<Float> call, Throwable t) {

                }
            });

            accommodationCard.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedAccommodation", accommodation);

                Navigation.findNavController(v).navigate(R.id.nav_accommodation_details, bundle);
            });
        }

        return convertView;
    }

    private void loadImage(Long imageId, ImageView imageView){
        Call<ResponseBody> call = ImageUtils.imageService.getById(imageId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){

                    try {
                        Log.d("REZ","Meesage recieved");
                        byte[] imageContent = response.body().bytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageContent, 0, imageContent.length);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    Log.d("ImageUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ImageUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}
