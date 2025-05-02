package com.csci335.bulletin.Events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.csci335.bulletin.Organizations.OrganizationProfilePage;
import com.csci335.bulletin.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EventEdit extends AppCompatActivity {

    private EditText dateInput, descInput, locationInput;
    private TextView titleInput;
    private TimePicker timePicker;
    private Spinner categorySpinner;
    private ImageView image;
    private Button saveBtn, cancelBtn, deleteBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        String imageUrl = getIntent().getStringExtra("imageUrl");
        title = getIntent().getStringExtra("title");
        // Initialize views
        titleInput = findViewById(R.id.editTextTitle2);
        dateInput = findViewById(R.id.editTextDate2);
        timePicker = findViewById(R.id.timePicker2);
        descInput = findViewById(R.id.editTextDesc2);
        locationInput = findViewById(R.id.editTextLocation2);
        image = findViewById(R.id.imageView);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        //load Image
        if (imageUrl != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(image);
        }

        loadData();
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        saveBtn.setOnClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String date = dateInput.getText().toString().trim();
            String desc = descInput.getText().toString().trim();
            String loc = locationInput.getText().toString().trim();
            saveChanges(title, date, loc, desc, hour, minute);
        });
        cancelBtn.setOnClickListener(v -> {
            finish();
        });

        deleteBtn.setOnClickListener(v -> {
            //Delete in Firebase
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("approvedEvents").document(title).delete();
            Intent homeIntent = new Intent(this, OrganizationProfilePage.class);
            startActivity(homeIntent);
            finish();
        });
    }

    private void loadData() {
        String docId = getIntent().getStringExtra("docId"); // event = document ID
        title = docId;
        db.collection("approvedEvents").document(docId).get()
                .addOnSuccessListener(document -> {
                        titleInput.setText(document.getString("title"));
                        dateInput.setText(document.getString("date"));
                        descInput.setText(document.getString("description"));
                        locationInput.setText(document.getString("location"));
                        Event e = document.toObject(Event.class);
                        timePicker.setHour(e.getHour());
                        timePicker.setMinute(e.getMinute());

                });
    }

    private void saveChanges(String title, String date, String loc, String desc, int hour, int minute) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("date", date);
        updates.put("description", desc);
        updates.put("location", loc);
        updates.put("hour", hour);
        updates.put("minute", minute);

        db.collection("approvedEvents").document(title).update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(this, OrganizationProfilePage.class);
                    startActivity(homeIntent);
                    finish(); // Return to previous screen (or redirect to homepage)
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                    Log.e("EventEdit", "Update error", e);
                });


    }
    }





