package com.csci335.bulletin.Organizations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csci335.bulletin.Events.Event;
import com.csci335.bulletin.Events.EventRecyclerViewAdapter;
import com.csci335.bulletin.R;
import com.csci335.bulletin.StudentClasses.HomePage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OrganizationProfilePage extends AppCompatActivity {
    String orgId;
    FirebaseFirestore db;

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
        Setting up recyclerview and getting events
         */
        RecyclerView orgEventsRV = findViewById(R.id.orgProfileRV);
        ArrayList<Event> events = Event.setUpEvents(orgEventsRV);
        EventRecyclerViewAdapter rvAdapter = new EventRecyclerViewAdapter(this, events);
        orgEventsRV.setAdapter(rvAdapter);
        orgEventsRV.setLayoutManager(new LinearLayoutManager(this));

        /*
        figure out how to display the right info based on which organization it is.
        need to filter events and get the right name and description
         */
        //retrieving info passed in
        if(getIntent().hasExtra("OrgId")) {
            orgId = getIntent().getExtras().getString("OrgId");
        }
        if(orgId == null){//current user is the organization trying to view their own page
            orgId = FirebaseAuth.getInstance().getUid();
        }

        TextView orgNameTV = findViewById(R.id.orgNameTV);
        TextView orgDescTV = findViewById(R.id.orgDescTV);


        // retrieve organization info
        db = FirebaseFirestore.getInstance();
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

        //return to main feed
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });

    }
}