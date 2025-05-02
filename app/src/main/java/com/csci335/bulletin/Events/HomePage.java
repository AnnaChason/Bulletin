package com.csci335.bulletin.Events;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csci335.bulletin.Main.NavigationManager;
import com.csci335.bulletin.Main.Notifications;
import com.csci335.bulletin.Main.UserLoadingScreen;
import com.csci335.bulletin.Organizations.OrganizationProfilePage;
import com.csci335.bulletin.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;


public class HomePage extends AppCompatActivity {

    ArrayList<Event> events = new ArrayList<Event>();//all events to be displayed on feed
    EventRecyclerViewAdapter rvAdapter;

    int num = UserLoadingScreen.getCurrentUserType();

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
        ImageButton bellBtn = findViewById(R.id.bellBtn);
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBar);
        //highlight correct item
        if(UserLoadingScreen.getCurrentUserType() == 1) {
            btmNavBarMain.setSelectedItemId(R.id.other);
            bellBtn.setVisibility(View.GONE);
        }
        else {
            btmNavBarMain.setSelectedItemId(R.id.home);
            bellBtn.setVisibility(View.VISIBLE);
        }
        //set up on click listener
        new NavigationManager(btmNavBarMain, HomePage.this);


        /*
        Event display manager
         */
        RecyclerView eventFeedRV = findViewById(R.id.eventFeedRV);
        events = Event.setUpEvents(eventFeedRV);//retreives events from database

        if( num ==1 ) {
            rvAdapter = new EventRecyclerViewAdapter(this, events, false, true);
        } else {
            rvAdapter = new EventRecyclerViewAdapter(this, events, false, false);
        }
        eventFeedRV.setAdapter(rvAdapter);
        eventFeedRV.setLayoutManager(new LinearLayoutManager(this));

        /*
        Sort drop down menu
         */
        Spinner sortSpinner = findViewById(R.id.sortSpinner);
        sortSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, Event.sortTypes()));
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                events.sort(Event.sortMethods(position));
                rvAdapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        /*
        to notification page button
         */
        bellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toNotifs = new Intent(getApplicationContext(), Notifications.class);
                startActivity(toNotifs);
            }
        });

    }

}