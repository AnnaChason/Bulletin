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
    private ArrayList<Event> events;
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
        Button mpBtn = findViewById(R.id.multiPurposeBtn);
        if(getIntent().hasExtra("OrgId")) {
            orgId = getIntent().getExtras().getString("OrgId");
        }
        if(orgId == null){//current user is the organization trying to view their own page
            orgId = FirebaseAuth.getInstance().getUid();
            mpBtn.setText("View pending events");
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
        boolean editVisible;
        if(UserLoadingScreen.getCurrentUserType() == 2){
            mpBtn.setOnClickListener(orgListener(mpBtn));
            editVisible = true;
        }
        else{
            mpBtn.setOnClickListener(followListener(mpBtn));
            editVisible = false;
        }
        /*
         Setting up recyclerview
         */
        RecyclerView orgEventsRV = findViewById(R.id.orgProfileRV);
        events = new ArrayList<>();
        rvAdapter = new EventRecyclerViewAdapter(this, events, editVisible);
        orgEventsRV.setAdapter(rvAdapter);
        orgEventsRV.setLayoutManager(new LinearLayoutManager(this));


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
    makes the button switch between approved events and pending events
     */
    private View.OnClickListener orgListener(Button mpBtn) {
        return new View.OnClickListener() {
            boolean clicked = false;
            ArrayList<Event> pending;

            @Override
            public void onClick(View v) {
                events.clear();
                rvAdapter.notifyDataSetChanged();
                if (clicked) {//going back to approved events
                    mpBtn.setText("View pending events");
                    for (Event e : approved) {
                        events.add(e);
                    }
                    rvAdapter.notifyDataSetChanged();
                } else {//going to pending events
                    mpBtn.setText("View approved events");
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
                clicked = !clicked;
            }
        };
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
}