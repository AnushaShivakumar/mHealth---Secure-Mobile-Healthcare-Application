package edu.pitt.lersais.mhealth;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayMedicalHistoryActivity extends AppCompatActivity {

    private TextView nameTextView, dobTextView, occupationTextView, contactTextView, sexTextView,
            maritalStatusTextView, commentsTextView, fatherDiseaseTextView, motherDiseaseTextView,
            siblingDiseaseTextView, alcoholTextView, cannabisTextView;
    private CheckBox allergyCheckbox, heartAttackCheckbox, rheumaticFeverCheckbox, heartMurmurCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_medical_history);

        // Initialize TextViews
        nameTextView = findViewById(R.id.nameTextView);
        dobTextView = findViewById(R.id.dobTextView);
        occupationTextView = findViewById(R.id.occupationTextView);
        contactTextView = findViewById(R.id.contactTextView);
        sexTextView = findViewById(R.id.sexTextView);
        maritalStatusTextView = findViewById(R.id.maritalStatusTextView);
        commentsTextView = findViewById(R.id.commentsTextView);
        fatherDiseaseTextView = findViewById(R.id.fatherTextView);
        motherDiseaseTextView = findViewById(R.id.motherTextView);
        siblingDiseaseTextView = findViewById(R.id.siblingTextView);
        alcoholTextView = findViewById(R.id.alcoholYesRadioButton);
        cannabisTextView = findViewById(R.id.cannabisYesRadioButton);

        // Initialize CheckBoxes
        allergyCheckbox = findViewById(R.id.allergyCheckbox);
        heartAttackCheckbox = findViewById(R.id.heartAttackCheckbox);
        rheumaticFeverCheckbox = findViewById(R.id.rheumaticFeverCheckbox);
        heartMurmurCheckbox = findViewById(R.id.heartMurmurCheckbox);

        // Display hardcoded data
        displayMedicalHistoryInfo();
    }

    private void displayMedicalHistoryInfo() {
        nameTextView.setText("Name: Zari");
        dobTextView.setText("Date of Birth: 1998-07-21");
        occupationTextView.setText("Occupation: Pharmacist");
        contactTextView.setText("Contact: +1 123-456-7890");
        sexTextView.setText("Gender: Female");
        maritalStatusTextView.setText("Marital Status: Single");
        commentsTextView.setText("Comments: No Known Chronic conditions, generally healthy.");

        // Family Diseases
        fatherDiseaseTextView.setText("Father: Hypertension");
        motherDiseaseTextView.setText("Mother: Diabetes");
        siblingDiseaseTextView.setText("Sibling: None");

        // Habits
        alcoholTextView.setText("Alcohol: No");
        cannabisTextView.setText("Cannabis: No");

        // Past Medical History Checkboxes
        allergyCheckbox.setChecked(true);
        heartAttackCheckbox.setChecked(false);
        rheumaticFeverCheckbox.setChecked(true);
        heartMurmurCheckbox.setChecked(true);
    }


}
