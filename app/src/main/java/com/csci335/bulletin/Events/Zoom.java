package com.csci335.bulletin.Events;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.csci335.bulletin.AdminClasses.AdminHomePage;
import com.csci335.bulletin.Main.UserLoadingScreen;
import com.csci335.bulletin.Organizations.OrganizationProfilePage;
import com.csci335.bulletin.R;

public class Zoom extends AppCompatActivity {

    ImageView image;
    Button returnButton;

   int num = UserLoadingScreen.getCurrentUserType();
/*This code displays an image in a new activity using Glide.
The image URL must be sent via intent from another activity. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom); // Make sure this layout exists

        image = findViewById(R.id.zoomImage); // Match ID in your XML
        String imageUrl = getIntent().getStringExtra("imageUrl");

        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(image);
        }

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            if(num == 1) {
                finish();
            } else if (num == 2){
                finish();
            } else {
                finish();
            }
        });

    }
}