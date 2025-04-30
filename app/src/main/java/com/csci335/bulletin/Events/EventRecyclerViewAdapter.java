package com.csci335.bulletin.Events;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.csci335.bulletin.StudentClasses.Student;
import com.csci335.bulletin.Events.EventEdit;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.csci335.bulletin.Main.UserLoadingScreen;
import com.csci335.bulletin.Organizations.Organization;
import com.csci335.bulletin.Organizations.OrganizationProfilePage;
import com.csci335.bulletin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder>{

    private ArrayList<Event> events;
    private Context context;
    private EventRecyclerViewAdapter.MyViewHolder holder;
    private boolean editBtnVisible;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Student student;
    public EventRecyclerViewAdapter(Context context, ArrayList<Event> events, boolean editBtnVisible){
        this.events = events;
        this.context = context;
        this.editBtnVisible = editBtnVisible;
    }

    @NonNull
    @Override
    //inflates layout and gives look to each row
    public EventRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.event_recyclerview_row, parent,false);
        holder = new EventRecyclerViewAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    //assigns value to each row as it comes on screen
    public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.eventNameVT.setText(events.get(position).getTitle());
        holder.dateVT.setText(events.get(position).getDate());

        // time assignment
        String time = "";
        if (events.get(position).getHour() > 12) time += String.valueOf(events.get(position).getHour() - 12);
        else time += String.valueOf(events.get(position).getHour());
        time += ":" + String.valueOf(events.get(position).getMinute());
        if (events.get(position).getHour() > 12) time += " p.m.";
        else time += " a.m.";
        holder.timeView.setText(time);

        holder.locationVT.setText(events.get(position).getLocation());
        holder.descriptionVT.setText(events.get(position).getDescription());
        holder.numAttendingVT.setText(""+ events.get(position).getAttendance() + " people attending");
        holder.categoryVT.setText("#" + events.get(position).getCategory());
        holder.orgNameBtn.setText(events.get(position).getOrganizationName());
        //load the image
        Glide.with(context)
                .load(events.get(position).getPosterImg())  // event.getPosterImg() should be the download URL
                .into(holder.poster);


        /*
        only students need to be able to mark events as attending
         */
        if(UserLoadingScreen.getCurrentUserType() == 3){
            holder.attendingBtn.setVisibility(View.VISIBLE);
            //set check boxes
            setAttendanceBoxes(position, holder.attendingBtn);

        }
        else
            holder.attendingBtn.setVisibility(View.GONE);

        /*
        updates attendance when button pressed
         */
        holder.attendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(student == null){
                    FirebaseAuth fauth = FirebaseAuth.getInstance();
                    String currentUID = fauth.getCurrentUser().getUid();
                    db.collection("studentInfo").document(currentUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult().exists())
                                student = task.getResult().toObject(Student.class);

                            Student student = task.getResult().toObject(Student.class);
                            String studentID = task.getResult().getId();
                            Event event = events.get(holder.getAdapterPosition());
                            String eventTitle = event.getTitle();
                            if(holder.attendingBtn.isChecked()) {
                                event.updateAttendance(+1);
                                event.addStudent(studentID);
                                student.addEvent(eventTitle);
                            } else {
                                event.updateAttendance(-1);
                                event.removeStudent(studentID);
                                student.removeEvent(eventTitle);
                            }
                            db.collection("approvedEvents").document(event.getTitle()).update("attendance",event.getAttendance());
                            db.collection("approvedEvents").document(event.getTitle()).update("students", event.getStudents());
                            db.collection("studentInfo").document(currentUID).update("events", student.getEvents());
                            notifyDataSetChanged();//not best practice but doesn't work when you only update the individual item

                        }
                    });
                }
                else{
                    String studentID = student.getID();
                    Event event = events.get(holder.getAdapterPosition());
                    String eventTitle = event.getTitle();
                    if(holder.attendingBtn.isChecked()) {
                        event.updateAttendance(+1);
                        event.addStudent(studentID);
                        student.addEvent(eventTitle);
                    } else {
                        event.updateAttendance(-1);
                        event.removeStudent(studentID);
                        student.removeEvent(eventTitle);
                    }
                    db.collection("approvedEvents").document(event.getTitle()).update("attendance",event.getAttendance());
                    db.collection("approvedEvents").document(event.getTitle()).update("students", event.getStudents());
                    db.collection("studentInfo").document(student.getID()).update("events", student.getEvents());
                    notifyDataSetChanged();//not best practice but doesn't work when you only update the individual item

                }
            }
        });

        holder.zoomButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = v.getContext();
                    Intent intent = new Intent(context, Zoom.class);
                    intent.putExtra("imageUrl", events.get(holder.getAdapterPosition()).getPosterImg());
                    context.startActivity(intent);

            }
        });

        /*
        to organization profile
         */
        holder.orgNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toOrgProfile = new Intent(context, OrganizationProfilePage.class);
                toOrgProfile.putExtra("OrgId",events.get(holder.getAdapterPosition()).getOrganizationID());
                context.startActivity(toOrgProfile);
            }
        });
        if(editBtnVisible) {
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toEdit = new Intent(context, EventEdit.class);
                    toEdit.putExtra("imageUrl", events.get(holder.getAdapterPosition()).getPosterImg());
                    toEdit.putExtra("docId", events.get(holder.getAdapterPosition()).getDocId());
                    context.startActivity(toEdit);
                }
            });
        }
        else{
            holder.editBtn.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    //num items to be displayed
    public int getItemCount() {
        return events.size();
    }

    /*
    make sure attendance checkBoxes are correctly checked
     */
    private void setAttendanceBoxes(int position, CheckBox attendingBtn) {
        if(UserLoadingScreen.getCurrentUserType() == 3) {
            FirebaseAuth fauth = FirebaseAuth.getInstance();
            String currentUID = fauth.getCurrentUser().getUid();

            if (student == null) {
                db.collection("studentInfo").document(currentUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            student = task.getResult().toObject(Student.class);
                            setAttendBoxHelper(position, attendingBtn);
                        }
                    }
                });
            } else {
                setAttendBoxHelper(position, attendingBtn);
            }
        }
    }
    /*
    actually updates check boxes
     */
    private void setAttendBoxHelper(int position, CheckBox attendingBtn){
        if(events.get(position).getStudents() != null && events.get(position).getStudents().contains(student.getID())) {
            attendingBtn.setChecked(true);
        }
        else
            attendingBtn.setChecked(false);
    }

    //gets all views from row layout and assigns them to variables
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView eventNameVT, dateVT, timeView, locationVT,descriptionVT, numAttendingVT, categoryVT;
        ImageView poster;
        CheckBox attendingBtn;
        Button orgNameBtn, zoomButton;
        ImageButton editBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameVT = itemView.findViewById(R.id.eventNameVT);
            dateVT = itemView.findViewById(R.id.dateVT);
            timeView = itemView.findViewById(R.id.timeView);
            locationVT = itemView.findViewById(R.id.locationVT);
            descriptionVT = itemView.findViewById(R.id.descriptionVT);
            numAttendingVT = itemView.findViewById(R.id.numAttendingVT);
            poster = itemView.findViewById(R.id.eventPosterView);
            attendingBtn = itemView.findViewById(R.id.attendingBtn);
            categoryVT = itemView.findViewById(R.id.categoryTV);
            orgNameBtn = itemView.findViewById(R.id.orgNameBtn);
            zoomButton = itemView.findViewById(R.id.zoomButton);
            editBtn = itemView.findViewById(R.id.editBtn);

        }
    }

}