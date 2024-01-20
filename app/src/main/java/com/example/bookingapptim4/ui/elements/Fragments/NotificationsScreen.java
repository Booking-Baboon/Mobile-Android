package com.example.bookingapptim4.ui.elements.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.notifications.NotificationUtils;
import com.example.bookingapptim4.data_layer.repositories.shared.ImageUtils;
import com.example.bookingapptim4.data_layer.repositories.users.GuestUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentFavoriteAccommodationsBinding;
import com.example.bookingapptim4.databinding.FragmentNotificationsScreenBinding;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.notifications.Notification;
import com.example.bookingapptim4.domain.models.users.User;
import com.example.bookingapptim4.ui.state_holders.adapters.AccommodationListAdapter;
import com.example.bookingapptim4.ui.state_holders.adapters.NotificationListAdapter;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsScreen extends Fragment {

    private ArrayList<Notification> notifications = new ArrayList<>();
    ListView notificationsListView;
    private FragmentNotificationsScreenBinding binding;

    public static NotificationsScreen newInstance() {
        return new NotificationsScreen();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notificationsListView = root.findViewById(R.id.notifications_list);

        loadNotifications();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadNotifications(){
        User user = UserUtils.getCurrentUser();
        Call<ArrayList<Notification>> call = NotificationUtils.notificationService.getByUserId(user.getId());
        call.enqueue(new Callback<ArrayList<Notification>>() {
            @Override
            public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                if (response.code() == 200){
                    Log.d("NotificationUtils","Meesage recieved");
                    notifications = response.body();

                    NotificationListAdapter notificationListAdapter = new NotificationListAdapter(getActivity(), notifications);
                    notificationsListView.setAdapter(notificationListAdapter);
                    notificationListAdapter.notifyDataSetChanged();

                }else{
                    Log.d("NotificationUtils","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                Log.d("NotificationUtils", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}