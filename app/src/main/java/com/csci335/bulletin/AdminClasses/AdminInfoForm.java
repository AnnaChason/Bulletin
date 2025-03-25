package com.csci335.bulletin.AdminClasses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.Organization;
import com.csci335.bulletin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminInfoForm extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_info_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button registerbtn = findViewById(R.id.adminRegBtn);
        EditText titleEntry = findViewById(R.id.editTextCode);

        registerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(titleEntry.getText().toString().equals(AdminUser.getAdminCode())){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    AdminUser newAdmin = new AdminUser("fakeemail@gmail.com",auth.getCurrentUser().getUid());
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("organizationInfo").document(newAdmin.getEmail()).set(newAdmin); // put object in database

                    Intent toHome = new Intent(getApplicationContext(), AdminHomePage.class);
                    startActivity(toHome);
                }

            }
        });
    }
}