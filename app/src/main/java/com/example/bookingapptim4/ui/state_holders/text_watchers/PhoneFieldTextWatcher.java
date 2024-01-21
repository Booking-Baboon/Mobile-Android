package com.example.bookingapptim4.ui.state_holders.text_watchers;

import android.text.Editable;

import com.google.android.material.textfield.TextInputLayout;

public class PhoneFieldTextWatcher extends RequiredFieldTextWatcher{
    public PhoneFieldTextWatcher(TextInputLayout textInputLayout) {
        super(textInputLayout);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(textInputLayout.getEditText().length()==0) {
            textInputLayout.setError("Field is required");
            return;
        }
        textInputLayout.setError(textInputLayout.getEditText().getText().toString().matches("^\\+(?:[0-9]●?){6,14}[0-9]$") ? null :"Must a valid phone number");
    }
}
