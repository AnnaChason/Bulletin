package com.csci335.bulletin.AdminClasses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.Main.LoginScreen;
import com.csci335.bulletin.Main.UserLoadingScreen;
import com.csci335.bulletin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminInfoForm extends AppCompatActivity {

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_info_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


       Button registerbtn = findViewById(R.id.AdminInfoFormbutton);
        EditText codeEntry = findViewById(R.id.editTextCode);

        registerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(codeEntry.getText().toString().equals(AdminUser.getAdminCode())){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String uid = auth.getCurrentUser().getUid().toString();
                    String email = auth.getCurrentUser().getEmail().toString();
                    AdminUser newAdmin = new AdminUser(email, uid);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("adminInfo").document(newAdmin.getUid()).set(newAdmin);

                    Intent toHome = new Intent(getApplicationContext(), UserLoadingScreen.class);
                    startActivity(toHome);
                }
                else{
                    /*
                    To Do: delete account if unsucessful
                     */
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser user = auth.getCurrentUser();
                    auth.signOut();
                    user.delete();
                    Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}