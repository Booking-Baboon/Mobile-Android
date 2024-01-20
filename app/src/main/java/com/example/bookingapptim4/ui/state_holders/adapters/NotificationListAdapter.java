package com.example.bookingapptim4.ui.state_holders.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.bookingapptim4.data_layer.repositories.notifications.NotificationUtils;
import com.example.bookingapptim4.data_layer.repositories.shared.ImageUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.notifications.Notification;
import com.example.bookingapptim4.domain.models.users.User;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListAdapter extends ArrayAdapter<Notification> {
    private ArrayList<Notification> notifications;

    public NotificationListAdapter(Context context, ArrayList<Notification> notifications) {
        super(context, R.layout.notification_card, notifications);
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        if (notifications != null) {
            return notifications.size();
        } else {
            return 0;
        }
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public Notification getItem(int position) {
        return notifications.get(position);
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

        Notification notification = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_card,
                    parent, false);
        }

        TextView title = convertView.findViewById(R.id.notification_title);
        TextView message = convertView.findViewById(R.id.notification_message);
        TextView dateCreated = convertView.findViewById(R.id.notification_time_created);
        Button markAsReadButton = convertView.findViewById(R.id.notification_mark_as_read_button);


        if (notification != null) {
            title.setText(notification.getType().toString());
            message.setText(notification.getMessage());
            dateCreated.setText(notification.getTimeCreated().toString());

            markAsReadButton.setOnClickListener(v -> {
                markNotificationAsRead(notification.getId(), markAsReadButton);
            });

            if(notification.getRead()){
                markAsReadButton.setVisibility(View.GONE);
            } else {
                markAsReadButton.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    private void markNotificationAsRead(Long id, Button markAsReadButton){
        User user = UserUtils.getCurrentUser();
        Call<Notification> call = NotificationUtils.notificationService.markAsRead(id, "Bearer " + user.getJwt());
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if (response.code() == 200){
                    Log.d("NotificationUtils","Meesage recieved");
                    markAsReadButton.setVisibility(View.GONE);
                }else{
                    Log.d("NotificationUtils","Meesage recieved: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Log.d("NotificationUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}
