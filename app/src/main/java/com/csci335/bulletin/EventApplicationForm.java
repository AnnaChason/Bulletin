package com.csci335.bulletin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.Mockups.FlyerApproval;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class EventApplicationForm extends AppCompatActivity {

    FirebaseFirestore db;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_application_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /**Bottom Navigation Bar Manager
        NavigationBarView btmNavBarMain = findViewById(R.id.btmNavBar);
        btmNavBarMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Intent next = new Intent();
                if(R.id.profile == itemId){
                    next = new Intent(getApplicationContext(),HomePage.class);
                    //Fix This: next = new Intent(/*Put file name of profile screen here);
                }
                else if(R.id.home == itemId){
                    next = new Intent(getApplicationContext(),HomePage.class);
                }
                else if(R.id.new_event == itemId){
                    next = new Intent(getApplicationContext(),EventApplicationForm.class);
                }
                startActivity(next);
                return true;
            }
        }); **/

        // form submission handling


        // submit button
        Button evtAppSubmit = findViewById(R.id.evtAppBtn);
        EditText titleEntry = findViewById(R.id.editTextTitle);
        EditText dateEntry = findViewById(R.id.editTextDate);
        EditText descEntry = findViewById(R.id.editTextDesc);
        EditText locEntry = findViewById(R.id.editTextLocation);

        // Handling category drop down menu
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,Event.categoryOptions()));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = Event.categoryOptions()[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        evtAppSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                    // get the database
                    db = FirebaseFirestore.getInstance();

                    // get values to build the event application object
                    String title = titleEntry.getEditableText().toString();
                    String date = dateEntry.getEditableText().toString();
                    String desc = descEntry.getEditableText().toString();
                    String loc = locEntry.getEditableText().toString();
                    Event newEventApp = new Event(title, date, loc, desc, 0, 0, "");


                    // put the object in the database
                    db.collection("eventApplications").document(title).set(newEventApp);

                    // redirect to home page
                    Intent home = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(home);
                }
        });
    }
}