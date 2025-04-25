package com.csci335.bulletin.AdminClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.csci335.bulletin.Events.Event;
import com.csci335.bulletin.Main.Notif;
import com.csci335.bulletin.Organizations.Organization;
import com.csci335.bulletin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

class A_RecyclerViewAdapter extends RecyclerView.Adapter<A_RecyclerViewAdapter.MyViewHolder> {
    ArrayList<Event> pendingEvents;
    Context context;

    public A_RecyclerViewAdapter(Context context, ArrayList<Event> pendingEvents){
        this.pendingEvents = pendingEvents;
        this.context = context;
    }
    //inflates layout and gives look to each row
    public A_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.admin_recyler_view_row, parent,false);
        return new A_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull A_RecyclerViewAdapter.MyViewHolder holder, int position) {
        //assigning values
        holder.eventNameVT.setText(pendingEvents.get(position).getTitle());
        holder.dateVT.setText(pendingEvents.get(position).getDate());
        holder.locationVT.setText(pendingEvents.get(position).getLocation());
        holder.descriptionVT.setText(pendingEvents.get(position).getDescription());
        holder.status.setText(pendingEvents.get(position).getCategory());

        //load the image
        Glide.with(context)
                .load(pendingEvents.get(position).getPosterImg())  // event.getPosterImg() should be the download URL
                .into(holder.poster);


        /*
        updates collections upon approval/denial
         */
        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Event event = pendingEvents.get(holder.getAdapterPosition());
                //Deletes from ArrayList
                pendingEvents.remove(holder.getAdapterPosition());

                //Move to approved collection
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("eventApplications").document(event.getTitle()).delete();
                db.collection("approvedEvents").document(event.getTitle()).set(event);
                notifyDataSetChanged();

                /*
                notifies organization
                 */
                db.collection("organizationInfo").document(event.getOrganizationID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            Organization org = task.getResult().toObject(Organization.class);
                            Notif approval = new Notif("Event Approved", "Congradulations! Your event " + event.getTitle() + " has been approved.");
                            org.addNotification(approval);
                            db.collection("organizationInfo").document(org.getId()).update("notifications", org.getNotifications());
                        }
                    }
                });
            }
        });

       holder.rejectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    Event event = pendingEvents.get(holder.getAdapterPosition());
                    //Deletes from ArrayList
                    pendingEvents.remove(holder.getAdapterPosition());

                    //Delete from Collection 1 in Firebase
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("eventApplications").document(event.getTitle()).delete();
                    db.collection("approvedEvents").document(event.getTitle()).set(event);
                    notifyDataSetChanged();

                    /*
                notifies organization
                 */
                db.collection("organizationInfo").document(event.getOrganizationID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            Organization org = task.getResult().toObject(Organization.class);
                            Notif approval = new Notif("Event post rejected", "Unfortunately, an admin has denied your post request for your event " + event.getTitle()+".");
                            org.addNotification(approval);
                            db.collection("organizationInfo").document(org.getId()).update("notifications", org.getNotifications());
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return pendingEvents.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing views and assigning variables
        TextView eventNameVT, dateVT, locationVT,descriptionVT;
        ImageView poster;
        TextView status;
        Button approveButton, rejectButton, zoomButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameVT = itemView.findViewById(R.id.eventNameVT);
            dateVT = itemView.findViewById(R.id.dateVT);
            locationVT = itemView.findViewById(R.id.locationVT);
            descriptionVT = itemView.findViewById(R.id.descriptionVT);
            poster = itemView.findViewById(R.id.myImageView);
            status = itemView.findViewById(R.id.status);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            approveButton = itemView.findViewById(R.id.approveButton);



        }
    }

}
