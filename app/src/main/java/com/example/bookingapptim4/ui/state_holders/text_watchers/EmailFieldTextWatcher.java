package com.example.bookingapptim4.ui.state_holders.text_watchers;

import android.text.Editable;

import com.google.android.material.textfield.TextInputLayout;

public class EmailFieldTextWatcher extends RequiredFieldTextWatcher{
    public EmailFieldTextWatcher(TextInputLayout textInputLayout) {
        super(textInputLayout);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(textInputLayout.getEditText().length()==0) {
            textInputLayout.setError("Field is required");
            return;
        }
        textInputLayout.setError(textInputLayout.getEditText().getText().toString().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$") ? null :"Must a email address");
    }
}
