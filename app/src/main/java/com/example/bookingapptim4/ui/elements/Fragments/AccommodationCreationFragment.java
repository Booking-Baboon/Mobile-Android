package com.example.bookingapptim4.ui.elements.Fragments;

import static android.app.Activity.RESULT_OK;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bookingapptim4.R;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AccommodationUtils;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AmenityUtils;
import com.example.bookingapptim4.data_layer.repositories.accommodations.AvailablePeriodUtils;
import com.example.bookingapptim4.data_layer.repositories.shared.ImageUtils;
import com.example.bookingapptim4.data_layer.repositories.users.UserUtils;
import com.example.bookingapptim4.databinding.FragmentAccommodationCreationBinding;
import com.example.bookingapptim4.domain.models.accommodations.Accommodation;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationRequest;
import com.example.bookingapptim4.domain.models.accommodations.AccommodationType;
import com.example.bookingapptim4.domain.models.accommodations.Amenity;
import com.example.bookingapptim4.domain.models.accommodations.AmenityReference;
import com.example.bookingapptim4.domain.models.accommodations.AvailablePeriod;
import com.example.bookingapptim4.domain.models.accommodations.Location;
import com.example.bookingapptim4.domain.models.shared.Image;
import com.example.bookingapptim4.domain.models.shared.TimeSlot;
import com.example.bookingapptim4.domain.models.users.HostReference;
import com.example.bookingapptim4.ui.state_holders.text_watchers.RequiredFieldTextWatcher;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationCreationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationCreationFragment extends Fragment {


    ImageCarousel imageCarousel;

    List<CarouselItem> images = new ArrayList<>();

    List<File> imageFiles = new ArrayList<>();

    private ArrayList<Amenity> allAmenities = new ArrayList<>();

    private FragmentAccommodationCreationBinding binding;

    private TextInputLayout nameInput, countryInput, cityInput, addressInput, descriptionInput, minGuestInput, maxGuestInput, priceInput;

    private ChipGroup chipGroup;

    private Accommodation createdAccommodation;

    private List<AvailablePeriod> availablePeriods = new ArrayList<>();

    private RadioGroup typeRadioGroup;




    public AccommodationCreationFragment() {

    }


    public static AccommodationCreationFragment newInstance() {
        AccommodationCreationFragment fragment = new AccommodationCreationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAccommodationCreationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadDateRangePicker(view);

        nameInput = view.findViewById(R.id.accommodation_name_creation);
        countryInput = view.findViewById(R.id.accommodation_country_creation);
        cityInput = view.findViewById(R.id.accommodation_city_creation);
        addressInput = view.findViewById(R.id.accommodation_address_creation);
        descriptionInput = view.findViewById(R.id.accommodation_description_creation);
        minGuestInput = view.findViewById(R.id.accommodation_min_guest_creation);
        maxGuestInput = view.findViewById(R.id.accommodation_max_guest_creation);
        chipGroup = view.findViewById(R.id.add_availability_chips);

         priceInput = view.findViewById(R.id.add_availability_price);

         typeRadioGroup= view.findViewById(R.id.accommodation_type_radio_creation);

        nameInput.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(nameInput));
        countryInput.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(countryInput));
        cityInput.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(cityInput));
        addressInput.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(addressInput));
        descriptionInput.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(descriptionInput));
        minGuestInput.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(minGuestInput));
        maxGuestInput.getEditText().addTextChangedListener(new RequiredFieldTextWatcher(maxGuestInput));


        loadAmenities();

        imageCarousel = view.findViewById(R.id.accommodationImagesCarousel);

         Button button = view.findViewById(R.id.upload_image_button);

         Button submit = view.findViewById(R.id.accommodation_create_button);

         fillAmenitiesCheckboxes(view);
         fillAccommodationTypeRadioGroup(view);

         button.setOnClickListener( new View.OnClickListener(){
             @Override
            public void onClick(View v) {
                 checkAndRequestPermissions();

             }
        });

        MaterialButton addAvailabilityButton = view.findViewById(R.id.add_availability_button);
        addAvailabilityButton.setOnClickListener(v -> {
            AvailablePeriod request = parseAvailabilityPeriodInfo(view);
            if(request != null){
                availablePeriods.add(request);
                System.out.println(request.getTimeSlot().getStartDate()+"-"+request.getTimeSlot().getEndDate());
                Chip chip = new Chip(new ContextThemeWrapper(getContext(), R.style.Theme_BookingAppTim4));
                chip.setText(request.getTimeSlot().getStartDate()+" to "+request.getTimeSlot().getEndDate()+"| Price per night: "+Float.toString(request.getPricePerNight())+"€");

                chip.setCloseIconVisible(true);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        availablePeriods.remove(request);
                        chipGroup.removeView(chip);
                    }
                });
                chipGroup.addView(chip);
                loadDateRangePicker(binding.getRoot());
            }
        });

         submit.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v) {
                 if (!areFieldsValid()){
                     showSnackbar(view,"Please fill all required fields!");
                     return;
                 }
                 String name = nameInput.getEditText().getText().toString();
                 String description = descriptionInput.getEditText().getText().toString();
                 Location location = new Location(countryInput.getEditText().getText().toString(), cityInput.getEditText().getText().toString(), addressInput.getEditText().getText().toString());
                 Set<AmenityReference> amenities = getSelectedAmenities(binding.getRoot(), R.id.accommodation_amenities_checkboxes_creation);
                 int minGuest = Integer.parseInt(minGuestInput.getEditText().getText().toString());
                 int maxGuest = Integer.parseInt(maxGuestInput.getEditText().getText().toString());
                 SwitchMaterial  pricePerPersonSwitch = binding.getRoot().findViewById(R.id.accommodation_price_per_person_creation);
                 boolean isPricingPerPerson =  pricePerPersonSwitch.isChecked();
                 RadioGroup radioGroup = binding.getRoot().findViewById(R.id.accommodation_type_radio_creation);
                 int checkedTypeId = radioGroup.getCheckedRadioButtonId();
                 RadioButton selectedRadioButton = binding.getRoot().findViewById(checkedTypeId);
                 String selectedValue = selectedRadioButton.getText().toString();
                 AccommodationType type = AccommodationType.valueOf(selectedValue);

                 AccommodationRequest request = new AccommodationRequest(name, description, new HostReference(UserUtils.getCurrentUser().getId()), location, amenities, minGuest, maxGuest, isPricingPerPerson, type);
                 createAccommodation(request);
                 showSnackbar(view, "Accommodation successfully created!");
                 Bundle bundle = new Bundle();
                 Navigation.findNavController(v).navigate(R.id.nav_accommodations, bundle);

             }
         });



        return view;
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    private AvailablePeriod parseAvailabilityPeriodInfo(View view){
        List<String> selectedDates = parseDates(view);
        String checkin = selectedDates.get(0);
        String checkout = selectedDates.get(1);
        String totalPriceText = priceInput.getEditText().getText().toString();

        if(checkin == null || checkin.isEmpty() || checkout == null || checkout.isEmpty() || totalPriceText == null || totalPriceText.isEmpty()){
            return null;
        }

        float totalPrice = Float.parseFloat(totalPriceText);

        return new AvailablePeriod(new TimeSlot(checkin,checkout), totalPrice);
    }

    private List<String> parseDates(View view) {
        String selectedDateRange = getTextFromTextView(view, R.id.add_availability_date_text_view);
        String startDate = null;
        String endDate = null;
        if(selectedDateRange != null){
            String[] dateParts = selectedDateRange.split(" to ");
            if (dateParts.length == 2) {
                startDate = dateParts[0];
                endDate = dateParts[1];
            }
        }

        return Arrays.asList(startDate, endDate);
    }

    private String getTextFromTextView(View dialogView, @IdRes int textViewId) {
        TextView textView = dialogView.findViewById(textViewId);
        return textView != null ? textView.getText().toString() : null;
    }

    private void pickPhoto(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto,  1);
    }



    public void createAccommodation(AccommodationRequest accommodation){

        Call<Accommodation> call = AccommodationUtils.accommodationService.create(accommodation, "Bearer "+UserUtils.getCurrentUser().getJwt());
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 201){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    createdAccommodation= response.body();
                    for (File image: imageFiles) {
                        uploadImage(image);
                    }
                    for (AvailablePeriod period: availablePeriods){
                        createPeriod(period);
                    }

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void createPeriod(AvailablePeriod period) {
        Call<AvailablePeriod> call = AvailablePeriodUtils.availablePeriodService.create(period);
        call.enqueue(new Callback<AvailablePeriod>() {
            @Override
            public void onResponse(Call<AvailablePeriod> call, Response<AvailablePeriod> response) {
                if (response.code() == 201){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    AvailablePeriod availablePeriod = response.body();
                    addPeriod(availablePeriod);



                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<AvailablePeriod> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void addPeriod(AvailablePeriod period) {
        Call<Accommodation> call = AccommodationUtils.accommodationService.addPeriod(createdAccommodation.getId(),period.getId(), "Bearer " + UserUtils.getCurrentUser().getJwt());
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 201){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());


                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }


    public void uploadImage(File image){

        RequestBody pathRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "accommodations/"+ createdAccommodation.getId());
        RequestBody fileNameRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image.getName());

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);

        MultipartBody.Part contentPart = MultipartBody.Part.createFormData("content", image.getName(), requestBody);

        Call<Image> call = ImageUtils.imageService.create(pathRequestBody, fileNameRequestBody, contentPart);
        call.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    Image uploadedImage = response.body();
                    if (uploadedImage != null) {
                        Log.d("REZ", "Uploaded image ID: " + uploadedImage.getId());
                        addImage(uploadedImage);
                    }

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    public void addImage(Image image){
        Call<Accommodation> call = AccommodationUtils.accommodationService.addImage(createdAccommodation.getId(),image.getId());
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                if (response.code() == 201){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());


                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Accommodation> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

                if(resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    images.add(new CarouselItem(String.valueOf(selectedImage)));
                    imageCarousel.setData(images);
                    imageFiles.add(new File(getPathFromUri(selectedImage, getContext().getApplicationContext())));
                }

    }

    private void loadAmenities(){
        Call<ArrayList<Amenity>> call = AmenityUtils.amenityService.getAll();
        call.enqueue(new Callback<ArrayList<Amenity>>() {
            @Override
            public void onResponse(Call<ArrayList<Amenity>> call, Response<ArrayList<Amenity>> response) {
                if (response.code() == 200){
                    Log.d("REZ","Meesage recieved");
                    System.out.println(response.body());
                    allAmenities = response.body();
                    fillAmenitiesCheckboxes(binding.getRoot());

                }else{
                    Log.d("REZ","Meesage recieved: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Amenity>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }

    private void fillAmenitiesCheckboxes(View view){
        LinearLayout amenitiesCheckboxesContainer = view.findViewById(R.id.accommodation_amenities_checkboxes_creation);

        for (Amenity amenity : allAmenities) {
            MaterialCheckBox checkBox = new MaterialCheckBox(requireContext());
            checkBox.setText(amenity.getName());
            checkBox.setId(Math.toIntExact(amenity.getId()));
            amenitiesCheckboxesContainer.addView(checkBox);
            int checkboxColor = Color.parseColor("#FEA31A");
            int checkboxColorStateList = ColorStateList.valueOf(checkboxColor).getDefaultColor();
            checkBox.setButtonTintList(ColorStateList.valueOf(checkboxColorStateList));
        }
    }


    private void fillAccommodationTypeRadioGroup(View view){

        RadioGroup radioGroup = view.findViewById(R.id.accommodation_type_radio_creation);
        
        AccommodationType[] values = AccommodationType.values();

        for (AccommodationType type : values) {
            MaterialRadioButton radioButton = new MaterialRadioButton(requireContext());
            radioButton.setText(type.toString());
            radioButton.setId(View.generateViewId());
            int radioColor = Color.parseColor("#FEA31A");
            int checkboxColorStateList = ColorStateList.valueOf(radioColor).getDefaultColor();
            radioButton.setButtonTintList(ColorStateList.valueOf(checkboxColorStateList));
            radioGroup.addView(radioButton);
        }
    }

    private Set<AmenityReference> getSelectedAmenities(View view, @IdRes int checkboxesContainerId) {
        Set<AmenityReference> selectedValues = new HashSet<>();
        LinearLayout checkboxContainer = view.findViewById(checkboxesContainerId);

        for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
            View childView = checkboxContainer.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;
                if (checkBox.isChecked()) {
                    selectedValues.add(new AmenityReference((long) checkBox.getId()) );
                }
            }
        }

        return selectedValues;
    }

    private String getPathFromUri(Uri uri, Context context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }

        return null;
    }


    private void checkAndRequestPermissions() {
            String[] permissions = {Manifest.permission.READ_MEDIA_IMAGES};

            // Check if the permissions are granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, 1);
                } else {
                    pickPhoto();
                }
            } else {
                pickPhoto();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickPhoto();
            } else {
                // Permission denied, handle accordingly (show a message, disable functionality, etc.)
            }
        }
    }

    private CalendarConstraints buildCalendarConstraints() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidator = new CalendarConstraints.DateValidator() {
            @Override
            public boolean isValid(long date) {
                for (AvailablePeriod availablePeriod : availablePeriods) {
                    long startMillis = convertDateStringToMillis(availablePeriod.getTimeSlot().getStartDate());
                    long endMillis = convertDateStringToMillis(availablePeriod.getTimeSlot().getEndDate());

                    if (date >= startMillis && date <= endMillis + 86400000) {
                        return false;
                    }
                }
                return date > new Date().getTime();
            }

            @Override
            public int describeContents() {
                return 0;
            }


            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }
        };

        constraintsBuilder.setValidator(dateValidator);

        return constraintsBuilder.build();
    }

    private long convertDateStringToMillis(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void loadDateRangePicker(View view) {
        MaterialButton dateRangePickerButton = view.findViewById(R.id.add_availability_date_range_button);
        TextView selectedDateRangeTextView = view.findViewById(R.id.add_availability_date_text_view);

        MaterialDatePicker<Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setCalendarConstraints(buildCalendarConstraints())
                .build();

        dateRangePickerButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getChildFragmentManager();
            datePicker.show(fragmentManager, datePicker.toString());
        });

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            long startDateMillis = selection.first;
            long endDateMillis = selection.second;

            String startDate = dateFormat.format(new Date(startDateMillis));
            String endDate = dateFormat.format(new Date(endDateMillis));

            String selectedDateRange = startDate + " to " + endDate;

            dateRangePickerButton.setText("Change Date Range");
            selectedDateRangeTextView.setText(selectedDateRange);
        });
    }

    private boolean areFieldsValid() {

        if (nameInput.getEditText().length() == 0) {
            nameInput.setError("This field is required");
            return false;
        }else{
            nameInput.setError(null);
        }

        if (countryInput.getEditText().length() == 0) {
            countryInput.setError("This field is required");
            return false;
        }


        if (cityInput.getEditText().length() == 0) {
            cityInput.setError("This field is required");
            return false;
        }

        if (addressInput.getEditText().length() == 0) {
            addressInput.setError("This field is required");
            return false;
        }

        if (descriptionInput.getEditText().length() == 0) {
            descriptionInput.setError("This field is required");
            return false;
        }

        if (minGuestInput.getEditText().length() == 0) {
            minGuestInput.setError("This field is required");
            return false;
        }

        if (maxGuestInput.getEditText().length() == 0) {
            maxGuestInput.setError("This field is required");
            return false;
        }

        if(typeRadioGroup.getCheckedRadioButtonId() == -1) return false;


        return true;
    }
}