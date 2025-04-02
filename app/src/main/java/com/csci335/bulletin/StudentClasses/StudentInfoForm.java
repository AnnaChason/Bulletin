package com.csci335.bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentInfoForm extends AppCompatActivity {

    EditText nameEditText;
    EditText ageEditText;
    RadioGroup maleFemaleRadioGroup;
    RadioGroup yearRadioGroup;
    CheckBox internationalCB;
    CheckBox missionaryCB;
    CheckBox thirdCultureCB;
    CheckBox pastorKidCB;


    private Student student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_info_form);

        // Components (text + radio buttons + check boxes)
        nameEditText = findViewById(R.id.nameET);
        ageEditText = findViewById(R.id.ageET);
        maleFemaleRadioGroup = findViewById(R.id.maleFemaleRG);

        maleFemaleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                if (String.valueOf(rb.getText()).equals("Male")) {
                    student.setGender(Gender.Male);
                } else {
                    student.setGender(Gender.Female);
                }

            }
        });
        yearRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                String selectedText = String.valueOf(rb.getText());
                switch(selectedText) {
                    case "Freshmen":
                        student.setYear(Year.Freshmen);
                        break;
                    case "Sophomore":
                        student.setYear(Year.Sophomore);
                        break;
                    case "Junior":
                        student.setYear(Year.Junior);
                        break;
                    case "Senior":
                        student.setYear(Year.Senior);
                        break;
                    case "Grad":
                        student.setYear(Year.Grad);
                        break;
                    // should add default case with exception raised
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button confirmationButton = findViewById(R.id.ConfirmationButton);
        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Put inputted info into student object and enter into database
                student.setName(String.valueOf(nameEditText.getText()));
                student.setAge(Integer.parseInt(String.valueOf(ageEditText.getText())));
                if (internationalCB.isChecked()) {
                    student.setSpecialStatus(SpecialStatus.International);
                } else if (missionaryCB.isChecked()) {
                    student.setSpecialStatus(SpecialStatus.MK);
                } else if (thirdCultureCB.isChecked()) {
                    student.setSpecialStatus(SpecialStatus.ThirdCulture);
                } else if (pastorKidCB.isChecked()) {
                    student.setSpecialStatus(SpecialStatus.PK);
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference studentInfo = db.collection("studentInfo");
                db.collection("studentInfo").document(student.getName()).set(student);

                Intent homePage = new Intent(getApplicationContext(), HomePage.class);
                startActivity(homePage);
            }
        });
    }
}