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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;

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
        ArrayList<String> checkedCats = new ArrayList<String>();

        // recycler view
        RecyclerView searchRV = findViewById(R.id.recyclerView);
        ArrayList<Event> events = Event.setUpEvents(searchRV);
        EventRecyclerViewAdapter rvAdapter = new EventRecyclerViewAdapter(this, events, false);
        searchRV.setAdapter(rvAdapter);
        searchRV.setLayoutManager(new LinearLayoutManager(this));

        View.OnClickListener checkBoxListener = new View.OnClickListener() {
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
        };

        for (CheckBox box : boxes) {
            box.setOnClickListener(checkBoxListener);
        }
         /*
        Bottom Navigation Bar Manager
        */
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBarS);
        btmNavBarMain.setSelectedItemId(R.id.other);
        new NavigationManager(btmNavBarMain, Search.this);

    }

    /**
     * cbToPos takes a given checkbox object and finds its integer position in a given array of boxes
     * @param box the checkbox to search for
     * @param boxes list of checkboxes that it is in
     * @return integer representing the cb's position
     */
    public static int cbToPos(CheckBox box, CheckBox[] boxes) {
        int counter = 0;
        while (!(box.equals(boxes[counter]))) {
            counter++;
        }
        return counter;
    }
}