package com.csci335.bulletin.StudentClasses;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.Main.NavigationManager;
import com.csci335.bulletin.Main.Profile;
import com.csci335.bulletin.R;
import com.google.android.material.navigation.NavigationBarView;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
         /*
        Bottom Navigation Bar Manager
         */
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBarS);
        btmNavBarMain.setSelectedItemId(R.id.other);
        new NavigationManager(btmNavBarMain, Search.this);

    }
}