package com.csci335.bulletin.Main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.csci335.bulletin.Organizations.Organization;
import com.csci335.bulletin.StudentClasses.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Utility {
    /*
     * For testing purposes only
     * Makes all current students in db follow an organization of choice
     */
    public static void AllStudentFollow() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("studentInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db.collection("organizationInfo").document("F1eSlY8Zv5c26rxxFNmwGsyVCGX2").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                Student student = document.toObject(Student.class);
                                String studId = student.getID();

                                Log.d("Follow", "Make modifications for student: " + studId);
                                Organization organization = task.getResult().toObject(Organization.class);
                                String orgId = organization.getId();
                                if(!organization.containsFollower(studId)) {
                                    organization.addFollower(studId);
                                }
                                if(!student.containsFollowedOrg(orgId)) {
                                    student.addFollowedOrg(orgId);
                                }
                                db.collection("studentInfo").document(studId).update("followedOrgs", student.getFollowedOrgs());
                                db.collection("organizationInfo").document(orgId).update("followers", organization.getFollowers());
                            }
                        });
                    }
                }
            }
        });
    }
}
