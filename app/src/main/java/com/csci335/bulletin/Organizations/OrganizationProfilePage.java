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
        Bottom Navigation Bar Manager
         */
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBarOP);
        if(UserLoadingScreen.getCurrentUserType() == 2)
            btmNavBarMain.setSelectedItemId(R.id.profile);
        new NavigationManager(btmNavBarMain, OrganizationProfilePage.this);


        //Figuring out which organization to display
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

        /*
        Setting up recyclerview and getting events
         */
        RecyclerView orgEventsRV = findViewById(R.id.orgProfileRV);
        ArrayList<Event> events = new ArrayList<>();
        EventRecyclerViewAdapter rvAdapter = new EventRecyclerViewAdapter(this, events);
        orgEventsRV.setAdapter(rvAdapter);
        orgEventsRV.setLayoutManager(new LinearLayoutManager(this));
        db.collection("approvedEvents").whereEqualTo("organizationID", orgId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                   events.add(document.toObject(Event.class));
                }
                rvAdapter.notifyDataSetChanged();
            }
        });


        //return to main feed
        //to do: fix this so it goes to the correct page depending on user! (or just get rid of it?
        Button backBtn = findViewById(R.id.backBtn);//make this a textView for aesthetics
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            }
        });

    }
}