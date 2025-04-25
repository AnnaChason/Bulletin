package com.csci335.bulletin.AdminClasses;

import static com.csci335.bulletin.Events.Event.filterEvents;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csci335.bulletin.Events.Event;
import com.csci335.bulletin.Events.EventRecyclerViewAdapter;
import com.csci335.bulletin.Main.NavigationManager;
import com.csci335.bulletin.Main.Profile;
import com.csci335.bulletin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/*
where admin can approve and deny events
 */
public class AdminHomePage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
        Event display manager
         */
        ArrayList<Event> pendingEvents = new ArrayList<Event>();//all events to be displayed on feed
        RecyclerView RecyclerView = findViewById(R.id.myRecyclerView);
        A_RecyclerViewAdapter rvAdapter = new A_RecyclerViewAdapter(this, pendingEvents);
        RecyclerView.setAdapter(rvAdapter);
        RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setUpEvents(pendingEvents, rvAdapter);
        
         /*
        Bottom Navigation Bar Manager
         */
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBarA);
        btmNavBarMain.setSelectedItemId(R.id.home);
        new NavigationManager(btmNavBarMain, AdminHomePage.this);

    }

    private void setUpEvents(ArrayList<Event> events, A_RecyclerViewAdapter adapter) {
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
                                Event eventapp = document.toObject(Event.class);
                               events.add(eventapp);
                            }
                           filterEvents(events);
                           Collections.sort(events);
                           adapter.notifyDataSetChanged();
                        } else {
                            System.out.println("ERROR RETRIEVING EVENT FEED"); // fix later
                        }
                    }
                });

    }
}

