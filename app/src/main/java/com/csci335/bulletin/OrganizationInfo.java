package com.csci335.bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrganizationInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organization_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView orgNameTV = findViewById(R.id.orgName);
        TextView orgDescTV = findViewById(R.id.orgDesc);
        Organization newOrg = new Organization(orgNameTV.getText().toString(),orgDescTV.getText().toString(),"idk"/*figure out how to get user ID*/);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventApplications").document(newOrg.getName()).set(newOrg); // put object in database

        Button orgRegisterBtn = findViewById(R.id.orgRegisterButton);
        orgRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView orgNameTV = findViewById(R.id.orgName);
                TextView orgDescTV = findViewById(R.id.orgDesc);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                Organization newOrg = new Organization(orgNameTV.getText().toString(),orgDescTV.getText().toString(),auth.getCurrentUser().getUid());
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("organizationInfo").document(newOrg.getName()).set(newOrg); // put object in database

                Intent toLogin = new Intent(getApplicationContext(),LoginScreen.class);
                startActivity(toLogin);
            }
        });
    }
}