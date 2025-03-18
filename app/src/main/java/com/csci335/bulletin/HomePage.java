package com.csci335.bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        btmNavBarMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Intent next = new Intent(getApplicationContext(),HomePage.class);
                if(R.id.profile == itemId){
                    next = new Intent(getApplicationContext(), Profile.class);
                }
                else if(R.id.home == itemId){
                    next = new Intent(getApplicationContext(),HomePage.class);
                }
                else if(R.id.new_event == itemId){
                    next = new Intent(getApplicationContext(),EventApplicationForm.class);
                }
                startActivity(next);
                return true;
            }
        });


        /*
        Event display manager
         */



        ArrayList<Event> events = new ArrayList<Event>();

        RecyclerView eventFeedRV = findViewById(R.id.eventFeedRV);

        seUpEvents(events, eventFeedRV);

        EventRecyclerViewAdapter rvAdapter = new EventRecyclerViewAdapter(this, events);
        eventFeedRV.setAdapter(rvAdapter);
        eventFeedRV.setLayoutManager(new LinearLayoutManager(this));


    }


    private void seUpEvents(ArrayList<Event> events, RecyclerView eventFeedRV){
        /*
        UPDATE this to actually get event info from database
         */
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("eventApplications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                EventApplication eventapp = document.toObject(EventApplication.class);
                                events.add(eventapp.toEvent());
                            }
                            eventFeedRV.getAdapter().notifyDataSetChanged();
                        } else {
                            System.out.println("ERROR RETREIVING EVENT FEED"); //fix later
                        }
                    }
                });

        //events.add(new Event("eventTest2", "3/4/2025","mysci","mysterious test event I need words to see formatting hello world come to my event, it's cool!"));
    }
}