package com.csci335.bulletin.StudentClasses;

import java.util.*;

import android.content.Intent;
import android.os.Bundle;

import com.csci335.bulletin.Main.Profile;
import com.csci335.bulletin.Main.UserLoadingScreen;
import com.csci335.bulletin.R;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

public class StudentInfoForm extends AppCompatActivity {

    EditText nameEditText;
    EditText ageEditText;
    RadioGroup maleFemaleRadioGroup;
    RadioGroup yearRadioGroup;
    CheckBox internationalCB,pastorKidCB,missionaryCB,thirdCultureCB;
    Button cancelbtn;
    private Student student;
    private boolean created;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_info_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Components (text + radio buttons + check boxes)
        nameEditText = findViewById(R.id.searchET);
        ageEditText = findViewById(R.id.ageET);
        maleFemaleRadioGroup = findViewById(R.id.maleFemaleRG);
        yearRadioGroup = findViewById(R.id.yearRG);
        cancelbtn = findViewById(R.id.cancelBtn);

        internationalCB = findViewById(R.id.InternationalCB);
        missionaryCB = findViewById(R.id.missionaryKidCB);
        thirdCultureCB = findViewById(R.id.thirdCultureCB);
        pastorKidCB = findViewById(R.id.pastorKidCB);

        /*
        check if student is editing already created account or is a new user
         */
        db.collection("studentInfo").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    student = task.getResult().toObject(Student.class);
                    created = true;
                    cancelbtn.setVisibility(View.VISIBLE);
                    nameEditText.setText(student.getName());
                    ageEditText.setText(""+student.getAge());

                    if(student.getGender().equals(Gender.Male))
                        maleFemaleRadioGroup.check(R.id.maleRB);
                    else
                        maleFemaleRadioGroup.check(R.id.femaleRB);

                    if(student.getYear().equals(Year.Freshmen))
                        yearRadioGroup.check(R.id.freshmenRB);
                    else if(student.getYear().equals(Year.Sophomore))
                        yearRadioGroup.check(R.id.sophomoreRB);
                    else if(student.getYear().equals(Year.Junior))
                        yearRadioGroup.check(R.id.juniorRB);
                    else if(student.getYear().equals(Year.Senior))
                        yearRadioGroup.check(R.id.seniorRB);
                    else if(student.getYear().equals(Year.Grad))
                        yearRadioGroup.check(R.id.gradRB);

                    if(student.getSpecialStatus().contains(SpecialStatus.International))
                        internationalCB.setChecked(true);
                    if(student.getSpecialStatus().contains(SpecialStatus.MK))
                        missionaryCB.setChecked(true);
                    if(student.getSpecialStatus().contains(SpecialStatus.ThirdCulture))
                        thirdCultureCB.setChecked(true);
                    if(student.getSpecialStatus().contains(SpecialStatus.PK))
                        pastorKidCB.setChecked(true);
                }
                else {
                    student = new Student(auth.getCurrentUser().getUid(), new ArrayList<SpecialStatus>());
                    created = false;
                }
            }
        });


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

        Button confirmationButton = findViewById(R.id.ConfirmationButton);
        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(created){
                    db.collection("studentInfo").document(student.getID()).delete();
                }
                // Put inputted info into student object and enter into database
                student.setName(String.valueOf(nameEditText.getText()));
                student.setAge(Integer.parseInt(String.valueOf(ageEditText.getText())));


                ArrayList<SpecialStatus> ss = new ArrayList<>();
                if (internationalCB.isChecked()) {
                    ss.add(SpecialStatus.International);
                }
                if (missionaryCB.isChecked()) {
                    ss.add(SpecialStatus.MK);
                }
                if (thirdCultureCB.isChecked()) {
                    ss.add(SpecialStatus.ThirdCulture);
                }
                if (pastorKidCB.isChecked()) {
                    ss.add(SpecialStatus.PK);
                }
                student.setSpecialStatus(ss);

                CollectionReference studentInfo = db.collection("studentInfo");
                db.collection("studentInfo").document(student.getID()).set(student);

                Intent next;
                if(!created) {
                    next = new Intent(getApplicationContext(), UserLoadingScreen.class);
                }
                else {
                    next = new Intent(getApplicationContext(), Profile.class);
                }
                startActivity(next);
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfile = new Intent(getApplicationContext(), Profile.class);
                startActivity(toProfile);
                finish();
            }
        });
    }
}