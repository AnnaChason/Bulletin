package com.csci335.bulletin.Organizations;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.csci335.bulletin.Main.UserLoadingScreen;
import com.csci335.bulletin.R;
import com.csci335.bulletin.Events.HomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;

public class OrganizationProfilePage extends AppCompatActivity {
    private String orgId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;

    //events to be displayed on screen
    private ArrayList<Event> events,archive,pending;
    private ArrayList<Event> approved = new ArrayList<>();;
    private EventRecyclerViewAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organization_profile_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
        Bottom Navigation Bar Manager
         */
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBarOP);
        if(UserLoadingScreen.getCurrentUserType() == 2)
            btmNavBarMain.setSelectedItemId(R.id.profile);
        new NavigationManager(btmNavBarMain, OrganizationProfilePage.this);

        /*
        setting up screen based on user type
         */
        boolean userIsOrg = false;
        Button mpBtn = findViewById(R.id.multiPurposeBtn);
        TextView archiveBtn = findViewById(R.id.archiveBtn);
        TabLayout typeTabs = findViewById(R.id.eventTabs);

        if(getIntent().hasExtra("OrgId")) {
            orgId = getIntent().getExtras().getString("OrgId");
            mpBtn.setOnClickListener(followListener(mpBtn));
            mpBtn.setVisibility(View.VISIBLE);
            typeTabs.setVisibility(View.GONE);
            archiveBtn.setVisibility(View.VISIBLE);
            archiveBtn.setOnClickListener(new View.OnClickListener() {
                boolean isClicked = false;
                @Override
                public void onClick(View v) {
                    if(!isClicked) {
                        archiveEvents();
                        archiveBtn.setText(R.string.to_current);
                    }
                    else {
                        currentEvents();
                        archiveBtn.setText(R.string.archive);
                    }
                    isClicked = !isClicked;
                }
            });
        }
        if(orgId == null){//current user is the organization trying to view their own page
            orgId = FirebaseAuth.getInstance().getUid();
            mpBtn.setText("View pending events");
            mpBtn.setVisibility(View.GONE);
            archiveBtn.setVisibility(View.GONE);
            typeTabs.setVisibility(View.VISIBLE);
            userIsOrg = true;
        }
        TextView orgNameTV = findViewById(R.id.orgNameTV);
        TextView orgDescTV = findViewById(R.id.orgDescTV);

        /*
        retrieve organization info
         */
        DocumentReference docRef = db.collection("organizationInfo").document(orgId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Organization org = documentSnapshot.toObject(Organization.class);
                    orgNameTV.setText(org.getName());
                    orgDescTV.setText(org.getDescription());
                }
                else {
                    orgNameTV.setText(orgId);
                    orgDescTV.setText("This organization does not exist");
                }
            }
        });

        /*
         Setting up recyclerview
         */
        RecyclerView orgEventsRV = findViewById(R.id.orgProfileRV);
        events = new ArrayList<>();
        rvAdapter = new EventRecyclerViewAdapter(this, events, false);
        orgEventsRV.setAdapter(rvAdapter);
        orgEventsRV.setLayoutManager(new LinearLayoutManager(this));

        typeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Called when a tab is selected
                switch (tab.getPosition()) {
                    case 0:
                        currentEvents();
                        break;
                    case 1:
                        pendingEvents();
                        break;
                    case 2:
                        archiveEvents();
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        /*
        set up events
         */
        db.collection("approvedEvents").whereEqualTo("organizationID", orgId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    approved.add(document.toObject(Event.class));
                    events.add(document.toObject(Event.class));
                }
                rvAdapter.notifyDataSetChanged();
            }
        });
    }

    /*
    makes button handle following/unfollowing organizations
     */
    private View.OnClickListener followListener (Button mpBtn){
        return new View.OnClickListener() {
            boolean clicked = false; //fix this to see if user is following the organization!
            @Override
            public void onClick(View v) {
                if(clicked){
                    mpBtn.setText("Follow");
                }
                else{
                    mpBtn.setText("Unfollow");
                }
                clicked = !clicked;
            }
        };
    }

    /*
    switches to pending events
    */
    private void pendingEvents() {
        events.clear();
        if (pending == null) {//haven't loaded pending events yet
            pending = new ArrayList<>();
            db.collection("eventApplications").whereEqualTo("organizationID", orgId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        pending.add(document.toObject(Event.class));
                        events.add(document.toObject(Event.class));
                    }
                    rvAdapter.notifyDataSetChanged();
                }
            });
        } else {
            for (Event e : pending) {
                events.add(e);
            }
            rvAdapter.notifyDataSetChanged();
        }
    }

    /*
      switches to archived events
     */
    private void archiveEvents (){
        events.clear();
        if(archive == null){
            archive = new ArrayList<>();
            db.collection("eventArchive").whereEqualTo("organizationID", orgId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        archive.add(document.toObject(Event.class));
                        events.add(document.toObject(Event.class));
                    }
                    rvAdapter.notifyDataSetChanged();
                }
            });
        }
        else{
            for(Event e: archive){
                events.add(e);
            }
            rvAdapter.notifyDataSetChanged();
        }
    }

    /*
    switches to current events
     */
    private void currentEvents (){
        events.clear();
        for(Event e: approved){
            events.add(e);
        }
        rvAdapter.notifyDataSetChanged();
    }

}