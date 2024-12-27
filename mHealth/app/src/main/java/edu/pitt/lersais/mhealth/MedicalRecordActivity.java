package edu.pitt.lersais.mhealth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MedicalRecordActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private EditText editTextName, editTextDob, editTextOccupation, editTextContact;
    private RadioGroup radioGroupGender, radioGroupMaritalStatus;
    private CheckBox checkBoxAllergy, checkBoxHeartAttack, checkBoxRheumaticFever, checkBoxHeartMurmur;
    private EditText editTextFather, editTextMother, editTextSibling;
    private RadioButton radioAlcoholYes, radioAlcoholNo, radioCannabisYes, radioCannabisNo;
    private EditText editTextComments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_medical_record);


        databaseReference = FirebaseDatabase.getInstance().getReference("MedicalHistory");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        initializeUIElements();

        

        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(v -> saveMedicalRecord());
    }

    private void initializeUIElements() {
        editTextName = findViewById(R.id.editTextName);
        editTextDob = findViewById(R.id.editTextDob);
        editTextOccupation = findViewById(R.id.editTextOccupation);
        editTextContact = findViewById(R.id.editTextContact);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioGroupMaritalStatus = findViewById(R.id.radioGroupMaritalStatus);

        checkBoxAllergy = findViewById(R.id.checkBoxAllergy);
        checkBoxHeartAttack = findViewById(R.id.checkBoxHeartAttack);
        checkBoxRheumaticFever = findViewById(R.id.checkBoxRheumaticFever);
        checkBoxHeartMurmur = findViewById(R.id.checkBoxHeartMurmur);

        editTextFather = findViewById(R.id.editTextFather);
        editTextMother = findViewById(R.id.editTextMother);
        editTextSibling = findViewById(R.id.editTextSibling);

        radioAlcoholYes = findViewById(R.id.radioAlcoholYes);
        radioAlcoholNo = findViewById(R.id.radioAlcoholNo);
        radioCannabisYes = findViewById(R.id.radioCannabisYes);
        radioCannabisNo = findViewById(R.id.radioCannabisNo);

        editTextComments = findViewById(R.id.editTextComments);
    }

    private Map<String, Object> collectMedicalRecordData() {
        Map<String, Object> medicalRecord = new HashMap<>();

        // General Information
        medicalRecord.put("name", editTextName.getText().toString());
        medicalRecord.put("dob", editTextDob.getText().toString());
        medicalRecord.put("occupation", editTextOccupation.getText().toString());
        medicalRecord.put("contact", editTextContact.getText().toString());

        // Gender
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.radioMale) {
            medicalRecord.put("sex", "Male");
        } else if (selectedGenderId == R.id.radioFemale) {
            medicalRecord.put("sex", "Female");
        }

        // Marital Status
        int selectedMaritalStatusId = radioGroupMaritalStatus.getCheckedRadioButtonId();
        if (selectedMaritalStatusId == R.id.radioSingle) {
            medicalRecord.put("marital_status", "Single");
        } else if (selectedMaritalStatusId == R.id.radioMarried) {
            medicalRecord.put("marital_status", "Married");
        } else if (selectedMaritalStatusId == R.id.radioDivorced) {
            medicalRecord.put("marital_status", "Divorced");
        } else if (selectedMaritalStatusId == R.id.radioWidowed) {
            medicalRecord.put("marital_status", "Widowed");
        }

        // Past Medical History
        Map<String, Object> pastMedicalHistory = new HashMap<>();
        pastMedicalHistory.put("allergy", checkBoxAllergy.isChecked());
        pastMedicalHistory.put("heart_attack", checkBoxHeartAttack.isChecked());
        pastMedicalHistory.put("rheumatic_fever", checkBoxRheumaticFever.isChecked());
        pastMedicalHistory.put("heart_murmur", checkBoxHeartMurmur.isChecked());
        medicalRecord.put("diseases", pastMedicalHistory);

        // Family Diseases
        Map<String, Object> familyDiseases = new HashMap<>();
        familyDiseases.put("Father", editTextFather.getText().toString());
        familyDiseases.put("Mother", editTextMother.getText().toString());
        familyDiseases.put("Sibling", editTextSibling.getText().toString());
        medicalRecord.put("family_diseases", familyDiseases);

        // Habits
        Map<String, Object> habits = new HashMap<>();
        habits.put("Alcohol", radioAlcoholYes.isChecked() ? "Yes" : "No");
        habits.put("Cannabis", radioCannabisYes.isChecked() ? "Yes" : "No");
        medicalRecord.put("habits", habits);

        // Comments
        medicalRecord.put("comments", editTextComments.getText().toString());

        return medicalRecord;
    }


    private void saveMedicalRecord() {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            Map<String, Object> medicalRecordData = collectMedicalRecordData();

            databaseReference.child(uid).setValue(medicalRecordData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Record saved successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to save record", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
