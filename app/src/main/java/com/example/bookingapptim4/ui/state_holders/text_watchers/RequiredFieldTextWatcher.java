package com.example.bookingapptim4.ui.state_holders.text_watchers;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class RequiredFieldTextWatcher implements TextWatcher {
    protected TextInputLayout textInputLayout;

    public RequiredFieldTextWatcher(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        textInputLayout.setError(textInputLayout.getEditText().length()==0 ? "Field is required" : null);
    }

    public static boolean isValid(String value) {
        // Check if the value is not empty or null
        return value != null && !value.trim().isEmpty();
    }
}