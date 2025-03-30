package com.csci335.bulletin.Organizations;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.csci335.bulletin.Events.Event;
import com.csci335.bulletin.Events.EventRecyclerViewAdapter;
import com.csci335.bulletin.R;

import java.util.ArrayList;

public class OrganizationProfilePage extends AppCompatActivity {
    String orgName;
    String orgId;
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

        /*
        figure out how to display the right info based on which organization it is.
        need to filter events and get the right name and description
         */
        orgName = "";
        orgId = "";
        //retrieving info passed in
        if(getIntent().hasExtra("OrgName")) {
            orgName = getIntent().getExtras().getString("OrgAppStatus");
        }
        if(getIntent().hasExtra("OrgId")) {
            orgId = getIntent().getExtras().getString("OrgAppStatus");
        }
        if(orgName.equals("") && orgId.equals("")){//current user is the organization trying to view their own page
            /*
            get data on current user
            maybe put the data retreiving method in event class
             */
        }
    }
}