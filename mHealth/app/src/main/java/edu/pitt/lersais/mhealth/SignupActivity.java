package edu.pitt.lersais.mhealth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;


/**
 * The SignupActivity is used to handle registration, email-password registration.
 *
 * @author Haobing Huang and Runhua Xu.
 */

public class SignupActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SignupActivity";

    // BEGIN
    private FirebaseAuth mAuth;
    // END

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // BEGIN
        mAuth = FirebaseAuth.getInstance();
        // END

        mEmailEditText = findViewById(R.id.field_email);
        mPasswordEditText = findViewById(R.id.field_password);
        mConfirmPasswordEditText = findViewById(R.id.field_password_confirm);

        findViewById(R.id.email_create_account_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            registration();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void registration() {
        // 1. Get the email, password, and confirm password from user's input
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        // 2. Validate user input
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("All fields must be filled");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return;
        }

        // 3. Show a progress dialog
        showProgressDialog();

        // 4. Call Firebase's createUserWithEmailAndPassword method
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // 5. Hide the progress dialog
                    hideProgressDialog();

                    if (task.isSuccessful()) {
                        // Registration success, navigate to LoginActivity
                        showToast("Registration successful!");
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Registration failed, show error message
                        showToast("Registration failed: " + task.getException().getMessage());
                    }
                });
    }

}
