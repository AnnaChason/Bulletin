package com.csci335.bulletin.Events;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EventEdit extends AppCompatActivity {

    private EditText titleInput, dateInput, descInput, locationInput;
    private Spinner categorySpinner;
    private ImageView imageView;
    private Button saveBtn, cancelBtn, deleteBtn;
    private String docId;
    Event event;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        docId = getIntent().getStringExtra("event");
        // Initialize views
        titleInput = findViewById(R.id.editTextTitle2);
        dateInput = findViewById(R.id.editTextDate2);
        descInput = findViewById(R.id.editTextDesc2);
        locationInput = findViewById(R.id.editTextLocation2);
        categorySpinner = findViewById(R.id.categorySpinner2);
        imageView = findViewById(R.id.imageView);
        saveBtn = findViewById(R.id.saveBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        Log.d("EventEdit", "docId: " + docId);
        loadData();
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        saveBtn.setOnClickListener(v -> {
            String newTitle = titleInput.getText().toString().trim();
            String date = dateInput.getText().toString().trim();
            String desc = descInput.getText().toString().trim();
            String loc = locationInput.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString();
            String orgId = "placeholder"; // replace with actual org ID if available
            String imageUrl = "noImage.jpg"; // or update with real image logic
            int approvalStatus = 0;
            saveChanges(docId, newTitle, date, loc, desc, imageUrl, approvalStatus, category, orgId);
        });
    }

    private void loadData() {
        db.collection("events").document(docId).get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                titleInput.setText(doc.getString("title"));
                dateInput.setText(doc.getString("date"));
                descInput.setText(doc.getString("description"));
                locationInput.setText(doc.getString("location"));
                // Load spinner and imageView data as needed
            }
        });
    }

    private void saveChanges(String oldTitle, String newTitle, String date, String loc, String desc,
                             String imageUrl, int approvalStatus, String category, String orgId) {


        Map<String, Object> updates = new HashMap<>();
        updates.put("title", newTitle);
        updates.put("date", date);
        updates.put("description", desc);
        updates.put("location", loc);
        updates.put("category", category);
        updates.put("imageUrl", imageUrl);
        updates.put("approvalStatus", approvalStatus);

        db.collection("eventApplications").document(docId).update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
                    finish(); // Return to previous screen (or redirect to homepage)
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                    Log.e("EventEdit", "Update error", e);
                });
    }
    }





