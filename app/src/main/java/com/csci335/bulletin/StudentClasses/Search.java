package com.csci335.bulletin.StudentClasses;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

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

public class Search extends AppCompatActivity {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        CheckBox sportCheckBox = findViewById(R.id.sportCheckBox);
        CheckBox performanceCheckBox = findViewById(R.id.performanceCheckBox);
        CheckBox ministryServiceCheckBox = findViewById(R.id.ministryServiceCheckBox);
        CheckBox speakerCheckBox = findViewById(R.id.speakerCheckBox);
        CheckBox faithCheckBox = findViewById(R.id.faithCheckBox);
        CheckBox movieGamesCheckBox = findViewById(R.id.movieGamesCheckBox);
        CheckBox informationalCheckBox = findViewById(R.id.informationalCheckBox);
        CheckBox artCheckBox = findViewById(R.id.artCheckBox);
        CheckBox foodCheckBox = findViewById(R.id.foodCheckBox);
        CheckBox academicsCheckBox = findViewById(R.id.academicsCheckBox);

        CheckBox[] boxes = {sportCheckBox, performanceCheckBox, ministryServiceCheckBox,
                speakerCheckBox, faithCheckBox, movieGamesCheckBox, informationalCheckBox,
                artCheckBox, foodCheckBox, academicsCheckBox};

        // recycler view
        RecyclerView searchRV = findViewById(R.id.recyclerView);
        ArrayList<Event> events = Event.setUpEvents(searchRV);
        EventRecyclerViewAdapter rvAdapter = new EventRecyclerViewAdapter(this, events, false);
        searchRV.setAdapter(rvAdapter);
        searchRV.setLayoutManager(new LinearLayoutManager(this));

        /*View.OnClickListener checkBoxListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int pos = cbToPos(cb, boxes);
                // add to the list of categories if checked
                if (cb.isChecked()) {
                    checkedCats.add(Event.categoryOptions()[pos]);
                }
                // remove from list if unchecked
                else { checkedCats.remove(Event.categoryOptions()[pos]); }

                // either way, update the recycler view
                if (!checkedCats.isEmpty()) {
                    db.collection("approvedEvents").whereIn("category", checkedCats).get().addOnCompleteListener(
                            new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        events.add(document.toObject(Event.class));
                                    }
                                    rvAdapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        };*/

        for (int i = 0; i < boxes.length; i++) {
            CheckBox box = boxes[i];
            box.setOnClickListener(
                    new CheckBoxListener(Event.categoryOptions()[i], searchRV, rvAdapter, events)
            );
        }
         /*
        Bottom Navigation Bar Manager
        */
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBarS);
        btmNavBarMain.setSelectedItemId(R.id.other);
        new NavigationManager(btmNavBarMain, Search.this);

    }

    private static class CheckBoxListener implements View.OnClickListener {

        private String category;
        private RecyclerView rv;
        private EventRecyclerViewAdapter rvAdapter;
        private ArrayList<Event> events;
        private ArrayList<String> checkedCats = new ArrayList<String>();

        public CheckBoxListener(String category, RecyclerView rv, EventRecyclerViewAdapter rvAdapter, ArrayList<Event> events) {
            this.category = category;
            this.rv = rv;
            this.rvAdapter = rvAdapter;
            this.events = events;
        }

        @Override
        public void onClick(View v) {
            CheckBox cb = (CheckBox) v;
            // add to the list of categories if checked
            if (cb.isChecked()) {
                checkedCats.add(category);
            }
            // remove from list if unchecked
            else { checkedCats.remove(category); }

            // either way, update the recycler view
            if (!checkedCats.isEmpty()) {
                db.collection("approvedEvents").whereIn("category", checkedCats).get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    events.add(document.toObject(Event.class));
                                }
                                rvAdapter.notifyDataSetChanged();
                            }
                        });
            }
            else {
                events = Event.setUpEvents(rv);
                rvAdapter.notifyDataSetChanged();
            }
        }
    }
}