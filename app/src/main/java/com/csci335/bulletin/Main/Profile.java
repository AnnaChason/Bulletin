package com.csci335.bulletin.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.Navigation;

import com.csci335.bulletin.AdminClasses.AdminInfoForm;
import com.csci335.bulletin.Organizations.EventApplicationForm;
import com.csci335.bulletin.R;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button buttonLogout;
    Button InfoFormbutton;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        buttonLogout = findViewById(R.id.button_logout);
        InfoFormbutton = findViewById(R.id.AdminInfoForm);
        textView = findViewById(R.id.user_details);
        user = mAuth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });

        InfoFormbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), AdminInfoForm.class);
                startActivity(intent);
                finish();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
        Bottom Navigation Bar Manager
         */
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBarP);
        if(UserLoadingScreen.getCurrentUserType() == 2)
            btmNavBarMain.setSelectedItemId(R.id.other);
        else
            btmNavBarMain.setSelectedItemId(R.id.profile);
        new NavigationManager(btmNavBarMain, Profile.this);

        TextView utype = findViewById(R.id.userTypeTV);
        utype.setText("User Type: " + UserLoadingScreen.getCurrentUserType());

    }
}