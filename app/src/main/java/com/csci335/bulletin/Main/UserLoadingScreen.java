package com.csci335.bulletin.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.AdminClasses.AdminHomePage;
import com.csci335.bulletin.Events.HomePage;
import com.csci335.bulletin.Organizations.EventApplicationForm;
import com.csci335.bulletin.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/*
this event will just be a loading page
where we process which type of user the current user is
then redirect them to the correct page.
 */
public class UserLoadingScreen extends AppCompatActivity {
   /*
   1 is admin
   2 is organization
   3 is student
    */
    private static int currentUserType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_loading_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button logOutBtn = findViewById(R.id.logoutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        FirebaseAuth fauth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        /*
        determine type of user and redirect to correct page
         */
        String currentUID = fauth.getCurrentUser().getUid();
        //check for admin
        DocumentReference adminRef = firestore.collection("adminInfo").document(currentUID);
        adminRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    currentUserType = 1;
                    Intent toHome = new Intent(getApplicationContext(), AdminHomePage.class);
                    startActivity(toHome);
                    finish();
                }
            }
        });
        //check for organization
        DocumentReference orgRef = firestore.collection("organizationInfo").document(currentUID);
        orgRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    currentUserType = 2;
                    Intent toHome = new Intent(getApplicationContext(), EventApplicationForm.class);
                    startActivity(toHome);
                    finish();
                }
            }
        });
        //check for student
        DocumentReference stuRef = firestore.collection("studentInfo").document(currentUID);
        stuRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //temporary until students are finished.
                if(documentSnapshot.exists()) {
                    currentUserType = 3;
                    Intent toHome = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(toHome);
                    finish();
                }
            }
        });
        //Intent toHome = new Intent(getApplicationContext(), HomePage.class);
        //startActivity(toHome);

    }

    public static int getCurrentUserType(){
        return currentUserType;
    }
}