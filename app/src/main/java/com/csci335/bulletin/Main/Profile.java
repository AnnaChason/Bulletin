package com.csci335.bulletin.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.Events.Event;
import com.csci335.bulletin.Organizations.Organization;
import com.csci335.bulletin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button buttonLogout,editBtn,deleteBtn,confirmBtn,cancelBtn;
    private TextView descriptionTv,warningTv, nameTv;
    private FirebaseUser user;
    private int userType;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        buttonLogout = findViewById(R.id.button_logout);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        confirmBtn = findViewById(R.id.confirmBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        descriptionTv = findViewById(R.id.descriptionTV);
        warningTv = findViewById(R.id.warningTv);
        nameTv = findViewById(R.id.user_name);
        userType = UserLoadingScreen.getCurrentUserType();
        String uid = mAuth.getCurrentUser().getUid();


        /*
        redirect user to login screen if they aren't logged in
         */
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            startActivity(intent);
            finish();
        }

        /*
        make sure screen looks correct based on user type
        and load user info.
         */
        if(userType == 1){//admin
            nameTv.setText(user.getEmail());
            descriptionTv.setVisibility(View.INVISIBLE);
        }
        else if(userType == 2){//organizaton
            db.collection("organizationInfo").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Organization org = task.getResult().toObject(Organization.class);
                    nameTv.setText(org.getName());
                    descriptionTv.setText(org.getDescription());
                }
            });
        }
        else{//student
            nameTv.setText(user.getEmail());
            descriptionTv.setVisibility(View.INVISIBLE);
        }
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningTv.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.VISIBLE);
                confirmBtn.setVisibility(View.VISIBLE);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningTv.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                confirmBtn.setVisibility(View.GONE);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningTv.setVisibility(View.GONE);
                cancelBtn.setVisibility(View.GONE);
                confirmBtn.setVisibility(View.GONE);

                switch(userType){
                    case 1:
                        db.collection("adminInfo").document(uid).delete();
                        mAuth.signOut();
                        user.delete();
                        break;
                    case 2:
                        db.collection("organizationInfo").document(uid).delete();
                        deleteOrgEvents(uid);
                        break;
                    case 3:
                        db.collection("studentInfo").document(uid).delete();
                        mAuth.signOut();
                        user.delete();
                        break;
                }
                //FirebaseAuth.getInstance(). need to delete account;
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });


        /*
        Bottom Navigation Bar Manager
         */
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBarP);
        if(UserLoadingScreen.getCurrentUserType() == 2)
            btmNavBarMain.setSelectedItemId(R.id.other);
        else
            btmNavBarMain.setSelectedItemId(R.id.profile);
        new NavigationManager(btmNavBarMain, Profile.this);


    }

    /*
    delete all events associated with an organization
    to do: also delete posters!
     */
    private void deleteOrgEvents(String uid){
        StorageReference sr = FirebaseStorage.getInstance().getReference();
        List<Task<Void>> deleteTasks = new ArrayList<>();
        String[] collections = {"eventApplications","approvedEvents","eventArchive"};

        for(String collection: collections){
            db.collection(collection).whereEqualTo("organizationID", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Event e = document.toObject(Event.class);
                        Task<Void> deleteTask = db.collection(collection).document(e.getTitle()).delete();
                        deleteTasks.add(deleteTask);
                    }
                    Tasks.whenAllComplete(deleteTasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Task<?>>> task) {
                            mAuth.signOut();
                            user.delete();
                        }
                    });
                }
            });
        }
        Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
        startActivity(intent);
    }
}