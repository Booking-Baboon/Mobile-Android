package com.example.bookingapptim4.ui.state_holders.text_watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PasswordFieldTextWatcher implements TextWatcher {

    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;

    public PasswordFieldTextWatcher(EditText newPasswordEditText, EditText confirmPasswordEditText) {
        this.newPasswordEditText = newPasswordEditText;
        this.confirmPasswordEditText = confirmPasswordEditText;
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not needed in this example
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Not needed in this example
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Check if the new password and confirm password match
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (!confirmPassword.equals("")) {
            confirmPasswordEditText.setError(null);
        }

        if (!newPassword.equals(confirmPassword)) {
            // Display an error or update UI to indicate password mismatch
            confirmPasswordEditText.setError("Passwords do not match");
        } else {
            // Clear any previous error
            confirmPasswordEditText.setError(null);
        }
    }

    public static boolean isValid(String newPassword, String confirmPassword) {
        // Check if the new password and confirm password match
        return newPassword.equals(confirmPassword);
    }
}
