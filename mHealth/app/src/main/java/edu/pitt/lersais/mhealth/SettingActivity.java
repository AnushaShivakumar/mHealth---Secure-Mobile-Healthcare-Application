package edu.pitt.lersais.mhealth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;


/**
 * The SettingActivity that is used to handle setting features such as email verification and
 * password reset.
 *
 * @author Haobing Huang and Runhua Xu.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Setting_Activity";
    private static final int REQUEST_CODE_FOR_GALLERY = 5201314;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private EditText editTextCurrentPwd;
    private EditText editTextNewPwd;
    private EditText editTextConfirmPwd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();
        editTextCurrentPwd = findViewById(R.id.setting_edit_current_pwd); // For current password
        editTextNewPwd = findViewById(R.id.setting_edit_new_pwd);         // For new password
        editTextConfirmPwd = findViewById(R.id.setting_edit_confirm_pwd); // For confirm password

        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } else {
            TextView textViewUID = findViewById(R.id.text_view_uid);
            textViewUID.setText(currentUser.getUid());
            TextView textViewEmail = findViewById(R.id.setting_email);
            textViewEmail.setText(currentUser.getEmail());
            TextView textViewEmailStatus = findViewById(R.id.setting_email_status);

            // Checking email verification status
            currentUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (currentUser.isEmailVerified()) {
                        textViewEmailStatus.setText("EMAIL VERIFIED");
                        findViewById(R.id.setting_button_verify_email).setEnabled(false);
                        findViewById(R.id.setting_button_reset_password_email).setEnabled(true);
                    } else {
                        textViewEmailStatus.setText("EMAIL NOT VERIFIED");
                        findViewById(R.id.setting_button_verify_email).setOnClickListener(SettingActivity.this);
                        findViewById(R.id.setting_button_reset_password_email).setEnabled(false);
                    }
                }
            });

            // Setting up button listeners
            findViewById(R.id.setting_button_reset_password).setOnClickListener(this);
            findViewById(R.id.setting_button_reset_password_email).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.setting_button_verify_email) {
            currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingActivity.this, "Verification email is sent.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (i == R.id.setting_button_reset_password) {

            resetPasswordByInput();
        } else if (i == R.id.setting_button_reset_password_email) {

            resetPasswordByEmail();
        }
    }

    private void resetPasswordByInput() {
        String currentPassword = editTextCurrentPwd.getText().toString().trim();
        String newPassword = editTextNewPwd.getText().toString().trim();
        String confirmPassword = editTextConfirmPwd.getText().toString().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty() || !newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match or fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);
        currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    currentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SettingActivity.this, "Password update failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SettingActivity.this, "Re-authentication failed. Check current password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void resetPasswordByEmail() {
        String emailAddress = currentUser.getEmail();
        if (emailAddress != null) {
            mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "No email associated with this account", Toast.LENGTH_SHORT).show();
        }
    }

}
