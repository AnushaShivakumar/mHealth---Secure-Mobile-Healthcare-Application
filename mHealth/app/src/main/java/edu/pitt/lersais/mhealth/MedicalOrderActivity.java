package edu.pitt.lersais.mhealth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MedicalOrderActivity extends AppCompatActivity {

    private EditText etPrescriptionName, etDosage, etFrequency, etDuration, etComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_order);

        // Initialize the EditText fields
        etPrescriptionName = findViewById(R.id.et_prescription_name);
        etDosage = findViewById(R.id.et_dosage);
        etFrequency = findViewById(R.id.et_frequency);
        etDuration = findViewById(R.id.et_duration);
        etComments = findViewById(R.id.et_comments);

        // Set up the save button
        Button btnSaveOrder = findViewById(R.id.btn_save_order);
        btnSaveOrder.setOnClickListener(v -> saveMedicalOrder());
    }

    private void saveMedicalOrder() {
        // Check if user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "User is not logged in. Cannot save order.", Toast.LENGTH_SHORT).show();
            return;  // Exit the method if the user is not logged in
        }

        // Proceed if user is logged in
        String prescriptionName = etPrescriptionName.getText().toString().trim();
        String dosage = etDosage.getText().toString().trim();
        String frequency = etFrequency.getText().toString().trim();
        String duration = etDuration.getText().toString().trim();
        String comments = etComments.getText().toString().trim();

        HashMap<String, Object> medicalOrder = new HashMap<>();
        medicalOrder.put("prescriptionName", prescriptionName);
        medicalOrder.put("dosage", dosage);
        medicalOrder.put("frequency", frequency);
        medicalOrder.put("duration", duration);
        medicalOrder.put("comments", comments);

        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference("MedicalOrders")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push();

        orderReference.setValue(medicalOrder)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Order saved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to save order", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
