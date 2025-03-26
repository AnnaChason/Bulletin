package com.csci335.bulletin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.csci335.bulletin.Mockups.FlyerApproval;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;


public class EventApplicationForm extends AppCompatActivity {

    FirebaseFirestore db;
    String category;
    // for image upload use
    StorageReference storageReference;
    Uri image;
    Button selectImgBtn, uploadImgBtn;
    ImageView imgView;
    StorageReference imgRef;
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
        /** image upload handling **/
        selectImgBtn = findViewById(R.id.selectImgBtn);
        uploadImgBtn = findViewById(R.id.uploadImgBtn);
        imgView = findViewById(R.id.imageView3);
        //StorageReference imgRef = null;

        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadEvent(image);
            }
        });





    }

    // upload image method
    private void uploadEvent(Uri file) {
        // get the database
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        //get textboxes from activity
        EditText titleEntry = findViewById(R.id.editTextTitle);
        EditText dateEntry = findViewById(R.id.editTextDate);
        EditText descEntry = findViewById(R.id.editTextDesc);
        EditText locEntry = findViewById(R.id.editTextLocation);
        // get values to build the event application object
        String title = titleEntry.getEditableText().toString();
        String date = dateEntry.getEditableText().toString();
        String desc = descEntry.getEditableText().toString();
        String loc = locEntry.getEditableText().toString();

        imgRef = storageReference.child("images/" + UUID.randomUUID().toString());
        if(file != null) {
            /*imgRef.putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Event newEventApp = new Event(title, date, loc, desc, task.getResult().getMetadata().getPath(), 0, category);

                    // put the object in the database
                    db.collection("eventApplications").document(title).set(newEventApp);

                    // redirect to home page
                    Intent home = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(home);
                }
            });*/
            imgRef.putFile(file).addOnSuccessListener(taskSnapshot -> {
                imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();  // This is the URL you can use in Glide
                    // get the organization info
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String orgName = orgIDtoName(id); /// THIS LINE DOES NOT WORK !!!!!!!!!!!!!!
                    Event newEventApp = new Event(title, date, loc,desc, downloadUrl, 0, category, id, orgName);
                    db.collection("eventApplications").document(title).set(newEventApp);

                    // Redirect to home page
                    Intent home = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(home);
                });
            }).addOnFailureListener(e -> {
                Log.e("GlideDebug", "Failed to upload image: ", e);
            });
        }
        else{
            // put the object in the database
            // get the organization info
            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

            String orgName = orgIDtoName(id); /// THIS LINE DOES NOT WORK !!!!!!!!!!!!!!
            Event newEventApp = new Event(title, date, loc, desc, "noImage.jpg", 0, category, id, orgName);
            db.collection("eventApplications").document(title).set(newEventApp);

            // redirect to home page
            Intent home = new Intent(getApplicationContext(), HomePage.class);
            startActivity(home);
        }
    }
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                if (result.getData() != null) {
                                    uploadImgBtn.setEnabled(true);
                                    image = result.getData().getData();
                                    Glide.with(getApplicationContext()).load(image).into(imgView);
                                }
                            }
                        }});
    /** using a user id, searches the organization info to find the matching organization name **/
    private String orgIDtoName(String id) {
        Query query = db.collection("organizationInfo").whereEqualTo("organizationID", id);
        return "";
    }
}