package com.csci335.bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


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
        ArrayList<Event> events = new ArrayList<Event>();//all events to be displayed on feed
        RecyclerView eventFeedRV = findViewById(R.id.eventFeedRV);
        seUpEvents(events, eventFeedRV);//retreives events from database
        EventRecyclerViewAdapter rvAdapter = new EventRecyclerViewAdapter(this, events);
        eventFeedRV.setAdapter(rvAdapter);
        eventFeedRV.setLayoutManager(new LinearLayoutManager(this));

        /*
        updates attendee number
         */

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
                            filterEvents(events);
                            Collections.sort(events);
                            eventFeedRV.getAdapter().notifyDataSetChanged();
                        } else {
                            System.out.println("ERROR RETREIVING EVENT FEED"); //fix later
                        }
                    }
                });
    }
    /*
    removes past events
     */
    private void filterEvents(ArrayList<Event> events){
        // Get today's date in YYMMDD format
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String formattedDate = currentDate.format(formatter);
        int[] currentDateArr = dateToNum(formattedDate);  // Convert today's date to YYMMDD
        int todayDateNum = (currentDateArr[2] * 10000) + (currentDateArr[0] * 100) + currentDateArr[1];  // Construct YYMMDD
        // Filter out events that have already happened
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.dateToNum() < todayDateNum) {
                iterator.remove();  // Remove event if it's before today's date
            }
        }
    }
    /*
    returns int array where index 0 has the month, 1 has the day, and 0 has the last 2 digits of the year
    should get rid of for cleaner code, but that's a later problem
     */
    private int[] dateToNum(String date){
        int[] dateNums = new int[3];
        try{
            int idx = date.indexOf("/");
            dateNums[0] = Integer.parseInt(date.substring(0, idx));
            dateNums[1] = Integer.parseInt(date.substring(idx+1, date.indexOf("/",idx+1)));
            idx = date.indexOf("/",idx+1);
            dateNums[2] = Integer.parseInt(date.substring(idx+1));
        } catch (Exception e) {
            dateNums = new int[]{-900,0,0};
        }
        return dateNums;
    }
}