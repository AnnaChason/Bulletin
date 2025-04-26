package com.csci335.bulletin.Main;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
import com.csci335.bulletin.Events.HomePage;
import com.csci335.bulletin.Organizations.EventApplicationForm;
import com.csci335.bulletin.Organizations.Organization;
import com.csci335.bulletin.Organizations.OrganizationProfilePage;
import com.csci335.bulletin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {
    ArrayList<Notif> displayedNots;
    ArrayList<Notif> allNots;
    NotificationRVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        allNots = new ArrayList<>();
        displayedNots = new ArrayList<>();

        /*
        Notification recycler view display manager
         */
        RecyclerView notifRV = findViewById(R.id.notifRV);
        rvAdapter = new NotificationRVAdapter(displayedNots, allNots, this);
        retreiveNotifs();
        notifRV.setAdapter(rvAdapter);
        notifRV.setLayoutManager(new LinearLayoutManager(this));

        /*
        swaps between read and unread messages
         */
        TabLayout typeTabs = findViewById(R.id.notTypes);
        typeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Called when a tab is selected
                switch (tab.getPosition()) {
                    case 0:
                        getAll();
                        break;
                    case 1:
                        getUnread();
                        break;
                    case 2:
                        getRead();
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        /*
        handles back button
         */
        ImageButton backBtn = findViewById(R.id.backbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back;
                if(UserLoadingScreen.getCurrentUserType() == 2) {
                    if (getIntent().hasExtra("OrgId")) {
                        back = new Intent(getApplicationContext(), OrganizationProfilePage.class);
                        back.putExtra("OrgId", getIntent().getExtras().getString("orgId"));
                    }
                    else{
                        back = new Intent(getApplicationContext(), EventApplicationForm.class);
                    }

                }
                else{
                    back = new Intent(getApplicationContext(), HomePage.class);
                }
                startActivity(back);

            }
        });
    }


    /*
    retrieves the user's notifications from the database
     */
    private void retreiveNotifs(){
        FirebaseAuth fauth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(UserLoadingScreen.getCurrentUserType() == 2){
            db.collection("organizationInfo").document(fauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   processNots(task);
                }
            });
        }
        else{
            db.collection("studentInfo").document(fauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    processNots(task);
                }
            });
        }
    }

    /*
     saves notifications from database query
     */
    private void processNots(Task<DocumentSnapshot> task){
        if(task.getResult().exists()){
            displayedNots.clear();
            ArrayList<Notif> not = task.getResult().toObject(Organization.class).getNotifications();
            for(Notif n: not){
                displayedNots.add(n);
                allNots.add(n);
            }
            rvAdapter.notifyDataSetChanged();
        }
    }

    /*
    updates displayed notification list to only unread messages
     */
    private void getUnread(){
        displayedNots.clear();
        for(int i = 0; i < allNots.size(); i++){
            if(allNots.get(i).getRead()){
                displayedNots.add(allNots.get(i));
            }
        }
        rvAdapter.notifyDataSetChanged();
    }

    /*
    updates displayed notification list to only read messages
     */
    private void getRead(){
        displayedNots.clear();
        for(int i = 0; i < allNots.size(); i++){
            if(!allNots.get(i).getRead()){
                displayedNots.add(allNots.get(i));
            }
        }
        rvAdapter.notifyDataSetChanged();
    }

    /*
    updates displayed notification list to show all messages
     */
    private void getAll(){
        displayedNots.clear();
        for(int i = 0; i < allNots.size(); i++){
            displayedNots.add(allNots.get(i));
        }
        rvAdapter.notifyDataSetChanged();
    }
}