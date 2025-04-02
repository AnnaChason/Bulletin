package com.csci335.bulletin.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.Mockups.AttendingMockup;
import com.csci335.bulletin.Mockups.FlyerApproval;
import com.csci335.bulletin.Mockups.OrganizationApproval;
import com.csci335.bulletin.R;
import com.csci335.bulletin.StudentClasses.HomePage;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button toOrganizationApprovalBtn = findViewById(R.id.toOrganizationApprovalBtn);
        toOrganizationApprovalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orgApprovalScreen = new Intent(getApplicationContext(), OrganizationApproval.class);
                startActivity(orgApprovalScreen);
            }
        });

        Button toApprovalBtn = findViewById(R.id.toApprovBtn);
        toApprovalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent approvalScreen = new Intent(getApplicationContext(), FlyerApproval.class);
                startActivity(approvalScreen);
            }
        });

        Button toFlyerViewBtn = findViewById(R.id.toFlyerBtn);
        toFlyerViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flyerAttendingScreen = new Intent(getApplicationContext(), AttendingMockup.class);
                startActivity(flyerAttendingScreen);
            }
    });

        Button toHomeBtn = findViewById(R.id.toHomeBtn);
        toHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(getApplicationContext(), HomePage.class);
                startActivity(toHome);
            }
        });


    }
}