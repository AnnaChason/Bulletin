package com.csci335.bulletin.StudentClasses;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    // useful recycler view attributes
    private static RecyclerView searchRV;
    private static EventRecyclerViewAdapter rvAdapter;



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
        Handling the checkboxes
         */
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
        searchRV = findViewById(R.id.recyclerView);
        ArrayList<Event> events = Event.setUpEvents(searchRV);
        rvAdapter = new EventRecyclerViewAdapter(this, events, false);
        searchRV.setAdapter(rvAdapter);
        searchRV.setLayoutManager(new LinearLayoutManager(this));


        for (int i = 0; i < boxes.length; i++) {
            CheckBox box = boxes[i];
            box.setOnClickListener(
                    new CheckBoxListener(Event.categoryOptions()[i], events)
            );
        }

        /*
        Handling the search components
         */
        EditText searchET = findViewById(R.id.searchET);
        ImageButton searchBtn = findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keywordSearch(String.valueOf(searchET.getText()), events);
                    }
                }
        );

        /*
        Bottom Navigation Bar Manager
        */
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBarS);
        btmNavBarMain.setSelectedItemId(R.id.other);
        new NavigationManager(btmNavBarMain, Search.this);

    }

    /**
     * CheckBoxListener is an enhanced on click listener that obtains the category
     * of the clicked checkbox and stores it for easier use.
     */
    private static class CheckBoxListener implements View.OnClickListener {

        private String category;
        private ArrayList<Event> events;
        private static ArrayList<String> checkedCats = new ArrayList<String>();

        public CheckBoxListener(String category, ArrayList<Event> events) {
            this.category = category;
            this.events = events;
        }

        @Override
        public void onClick(View v) {
            CheckBox cb = (CheckBox) v;
            events.clear();
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
                rvAdapter.notifyDataSetChanged();
            }
        }
    }

    private void keywordSearch(String text, ArrayList<Event> events) {
        // clear the events
        events.clear();

        db.collection("approvedEvents").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event newEvt = document.toObject(Event.class);
                            // check if the string matches the event title
                            if (newEvt.getTitle().toLowerCase().contains(text.toLowerCase())) {
                                // adding matched item to the list
                                events.add(newEvt);
                            }
                        }
                        if(events.isEmpty())
                            Toast.makeText(getApplicationContext(), "No Results Found.", Toast.LENGTH_SHORT).show();
                        rvAdapter.notifyDataSetChanged();
                    }
                }
        );

    }
}