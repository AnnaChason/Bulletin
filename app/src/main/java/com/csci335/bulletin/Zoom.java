package com.csci335.bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.csci335.bulletin.Events.HomePage;

public class Zoom extends AppCompatActivity {

    ImageView image;
    Button returnButton;
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
            Intent intent = new Intent(Zoom.this, HomePage.class);
            startActivity(intent);
        });

    }
}