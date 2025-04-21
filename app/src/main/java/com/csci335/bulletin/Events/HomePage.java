package com.csci335.bulletin.Events;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csci335.bulletin.Main.NavigationManager;
import com.csci335.bulletin.Main.UserLoadingScreen;
import com.csci335.bulletin.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;


public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
        Bottom Navigation Bar Manager
         */
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBar);
        //highlight correct item
        if(UserLoadingScreen.getCurrentUserType() == 1)
            btmNavBarMain.setSelectedItemId(R.id.other);
        else
            btmNavBarMain.setSelectedItemId(R.id.home);
        //set up on click listener
        new NavigationManager(btmNavBarMain, HomePage.this);


        /*
        Event display manager
         */
        ArrayList<Event> events = new ArrayList<Event>();//all events to be displayed on feed
        RecyclerView eventFeedRV = findViewById(R.id.eventFeedRV);
        events = Event.setUpEvents(eventFeedRV);//retreives events from database
        EventRecyclerViewAdapter rvAdapter = new EventRecyclerViewAdapter(this, events);
        eventFeedRV.setAdapter(rvAdapter);
        eventFeedRV.setLayoutManager(new LinearLayoutManager(this));

    }

}