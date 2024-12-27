package edu.pitt.lersais.mhealth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.graphics.Bitmap;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.graphics.BitmapFactory;




import edu.pitt.lersais.mhealth.util.DownloadImageTask;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Profile_Activity";
    private static final int REQUEST_CODE_FOR_GALLERY = 520;

    private TextView uidTextView;
    private EditText nameEditText;
    private ImageView photoImageView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            nameEditText = findViewById(R.id.edit_text_display_name);
            photoImageView = findViewById(R.id.image_view_profile_photo);
            uidTextView = findViewById(R.id.text_view_uid);
            uidTextView.setText(currentUser.getUid());

            findViewById(R.id.button_update_profile).setOnClickListener(this);
            findViewById(R.id.button_chose_photo).setOnClickListener(this);

            displayNameAndPhoto(currentUser);
            loadProfileImage();
        }
    }

    private void displayNameAndPhoto(FirebaseUser currentUser) {
        // Display user's display name
        if (currentUser.getDisplayName() != null) {
            nameEditText.setText(currentUser.getDisplayName());
        }

        // Display user's profile photo (if photo uploading is re-enabled later)
        if (currentUser.getPhotoUrl() != null) {
            Uri photoUrl = currentUser.getPhotoUrl();
            new DownloadImageTask(photoImageView).execute(photoUrl.toString());
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void uploadImageToFirebase(String base64Image) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(user.getUid())
                    .child("profileImage");

            databaseReference.setValue(base64Image)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Profile image uploaded successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed to upload profile image", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "Error uploading profile image", task.getException());
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadProfileImage() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(user.getUid())
                    .child("profileImage");


            databaseReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String base64Image = task.getResult().getValue(String.class);
                    if (base64Image != null && !base64Image.isEmpty()) {
                        Bitmap bitmap = base64ToBitmap(base64Image);
                        if (bitmap != null) {
                            photoImageView.setImageBitmap(bitmap);
                        } else {
                            Log.e(TAG, "Bitmap decoding failed");
                            Toast.makeText(this, "Failed to decode profile image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e(TAG, "Base64 image string is null or empty");
                        Toast.makeText(this, "Profile image data is missing", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Failed to load profile image", task.getException());
                    Toast.makeText(this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private Bitmap base64ToBitmap(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }




    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_update_profile) {
            updateDisplayNameOnly(); // Update the display name only
        } else if (i == R.id.button_chose_photo) {
            choosePhotoFromGallery();
        }
    }

    private void choosePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_FOR_GALLERY);
    }

    private void updateDisplayNameOnly() {
        String displayName = nameEditText.getText().toString().trim();
        if (!displayName.isEmpty()) {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Reload user to get updated profile data
                                    user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> reloadTask) {
                                            if (reloadTask.isSuccessful()) {
                                                displayNameAndPhoto(user); // Refresh the UI with updated info
                                                Toast.makeText(ProfileActivity.this, "Yay! Your Profile Updated!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(ProfileActivity.this, "Error reloading user data", Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, "Error reloading user data", reloadTask.getException());
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Error updating display name", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Error updating display name", task.getException());
                                }
                            }
                        });
            }
        } else {
            Toast.makeText(ProfileActivity.this, "Display name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                photoImageView.setImageURI(selectedImageUri);

                try {
                    // Convert URI to Bitmap
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    // Convert Bitmap to Base64 String
                    String base64Image = bitmapToBase64(bitmap);

                    // Upload Base64 string to Firebase
                    uploadImageToFirebase(base64Image);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
