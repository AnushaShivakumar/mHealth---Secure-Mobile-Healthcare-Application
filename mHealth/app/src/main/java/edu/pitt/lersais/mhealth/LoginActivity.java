package edu.pitt.lersais.mhealth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;


/**
 * The LoginActivity is used to handle login authentication, email password authentication.
 *
 * @author Haobing Huang and Runhua Xu.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Login_Password_Activity";
    private static final int REQUEST_SIGNUP = 123;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    // BEGIN
    private FirebaseAuth mAuth;
    // END

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // BEGIN
        mAuth = FirebaseAuth.getInstance();
        // END

        mEmailEditText = findViewById(R.id.field_email);
        mPasswordEditText = findViewById(R.id.field_password);

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.link_signup).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_sign_in_button) {
            login();
        }
        else if (i == R.id.link_signup) {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    private void login() {
        // 1. Get the email and password from user's input
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        // 2. Validate user input
        if (email.isEmpty() || password.isEmpty()) {
            showToast("Email and password must not be empty");
            return;
        }

        // 3. Show a progress dialog
        showProgressDialog();

        // 4. Call Firebase's signInWithEmailAndPassword method
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    // 5. Hide the progress dialog
                    hideProgressDialog();

                    if (task.isSuccessful()) {
                        // Login success, navigate to MainActivity
                        showToast("Login successful!");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Login failed, show error message
                        showToast("Login failed: " + task.getException().getMessage());
                    }
                });
    }


}
