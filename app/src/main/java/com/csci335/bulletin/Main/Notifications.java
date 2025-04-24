package com.csci335.bulletin.Main;

import android.app.Notification;
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
import com.csci335.bulletin.Organizations.Organization;
import com.csci335.bulletin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {
    ArrayList<Notif> notifications;
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

        /*
        Notification display manager
         */
        notifications = new ArrayList<>();
        notifications.add(new Notif("HELLO", "Hello world!!!!!!"));
        RecyclerView notifRV = findViewById(R.id.notifRV);
        rvAdapter = new NotificationRVAdapter(notifications, this);
        retreiveNotifs();
        notifRV.setAdapter(rvAdapter);
        notifRV.setLayoutManager(new LinearLayoutManager(this));
    }

    private void retreiveNotifs(){
        FirebaseAuth fauth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(UserLoadingScreen.getCurrentUserType() ==2){
            db.collection("organizationInfo").document(fauth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.getResult().exists()){
                        notifications.clear();
                        ArrayList<Notif> not = task.getResult().toObject(Organization.class).getNotifications();
                        for(Notif n: not){
                            notifications.add(n);
                        }
                        rvAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
}