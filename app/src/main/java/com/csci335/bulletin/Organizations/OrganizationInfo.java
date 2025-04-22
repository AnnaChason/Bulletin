package com.csci335.bulletin.Organizations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.Main.LoginScreen;
import com.csci335.bulletin.Main.Profile;
import com.csci335.bulletin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrganizationInfo extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button orgRegisterBtn, cancelBtn;
    TextView orgNameTV, orgDescTV;
    Organization org;
    boolean created;

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
        orgNameTV = findViewById(R.id.orgName);
        orgDescTV = findViewById(R.id.orgDesc);
        orgRegisterBtn = findViewById(R.id.orgRegisterButton);
        cancelBtn = findViewById(R.id.cancelBtn);

        /*
        check if user is creating new account or editing already created one
         */
        db.collection("organizationInfo").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    created = true;
                    org = task.getResult().toObject(Organization.class);
                    orgNameTV.setText(org.getName());
                    orgDescTV.setText(org.getDescription());
                    orgRegisterBtn.setText("Save");
                    cancelBtn.setVisibility(View.VISIBLE);
                }
                else{
                    created = false;
                }
            }
        });


        orgRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!created) {
                    org = new Organization(orgNameTV.getText().toString(), orgDescTV.getText().toString(), auth.getCurrentUser().getUid());
                    db.collection("organizationInfo").document(org.getId()).set(org); // put object in database

                    Intent toLogin = new Intent(getApplicationContext(), LoginScreen.class);
                    startActivity(toLogin);
                    finish();
                }
                else{
                    db.collection("organizationInfo").document(org.getId()).update("name",orgNameTV.getText().toString(), "description",orgDescTV.getText().toString());

                    Intent toProfile = new Intent(getApplicationContext(), Profile.class);
                    startActivity(toProfile);
                    finish();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProfile = new Intent(getApplicationContext(), Profile.class);
                startActivity(toProfile);
                finish();
            }
        });
    }
}