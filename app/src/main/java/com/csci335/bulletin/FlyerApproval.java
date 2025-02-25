package com.csci335.bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FlyerApproval extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flyer_approval);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //gets elements from screen
        Button approveBtn = findViewById(R.id.approveBtn);
        Button rejectBtn = findViewById(R.id.rejectBtn);
        Intent flyerProcessed = new Intent(getApplicationContext(),flyerProcessed.class) ;

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to processed screen and send it approval status
                flyerProcessed.putExtra("com.csci335.approvalStatus","Flyer Approved!");
                startActivity(flyerProcessed);
            }
        });
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to processed screen and send it approval status
                flyerProcessed.putExtra("com.csci335.approvalStatus","Flyer Rejected");
                startActivity(flyerProcessed);
            }
        });

    }
}