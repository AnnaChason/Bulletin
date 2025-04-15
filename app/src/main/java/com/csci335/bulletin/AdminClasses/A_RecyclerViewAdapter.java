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
import com.csci335.bulletin.R;
import com.google.firebase.firestore.FirebaseFirestore;

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
        updates collections
         */
        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Event event = pendingEvents.get(holder.getAdapterPosition());
                //Deletes from ArrayList
                pendingEvents.remove(holder.getAdapterPosition());

                //Delete from Collection 1  in Firebase
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("eventApplications").document(event.getTitle()).delete();
                db.collection("approvedEvents").document(event.getTitle()).set(event);
                notifyDataSetChanged();
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
