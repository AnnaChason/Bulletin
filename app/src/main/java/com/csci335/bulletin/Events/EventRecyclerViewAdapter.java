package com.csci335.bulletin.Events;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.csci335.bulletin.Main.UserLoadingScreen;
import com.csci335.bulletin.Organizations.OrganizationProfilePage;
import com.csci335.bulletin.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder>{

    private ArrayList<Event> events;
    private Context context;
    private EventRecyclerViewAdapter.MyViewHolder holder;
    private boolean editBtnVisible;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
        holder.locationVT.setText(events.get(position).getLocation());
        holder.descriptionVT.setText(events.get(position).getDescription());
        holder.numAttendingVT.setText(""+ events.get(position).getAttendance() + " people attending");
        holder.categoryVT.setText("#" + events.get(position).getCategory());
        holder.orgNameBtn.setText(events.get(position).getOrganizationName());
        //load the image
        Glide.with(context)
                .load(events.get(position).getPosterImg())  // event.getPosterImg() should be the download URL
                .into(holder.poster);
        if(UserLoadingScreen.getCurrentUserType() != 3){
            holder.attendingBtn.setVisibility(View.GONE); //organizations and admin don't attend events
        }

        /*
        updates attendance
         */
        holder.attendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                need to figure out how to store which users are attending, and for the user which events they are attending.
                */
                if(holder.attendingBtn.isChecked())
                    events.get(holder.getAdapterPosition()).updateAttendance(+1);
                else
                    events.get(holder.getAdapterPosition()).updateAttendance(-1);

                db.collection("approvedEvents").document(events.get(holder.getAdapterPosition()).getTitle()).update("attendance",events.get(holder.getAdapterPosition()).getAttendance());
                notifyDataSetChanged();//not best practice but doesn't work when you only update the individual item
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
                   toEdit.putExtra("event", events.get(holder.getAdapterPosition()).getTitle());
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

    //gets all views from row layout and assigns them to variables
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView eventNameVT, dateVT, locationVT,descriptionVT, numAttendingVT, categoryVT;
        ImageView poster;
        CheckBox attendingBtn;
        Button orgNameBtn, zoomButton;
        ImageButton editBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameVT = itemView.findViewById(R.id.eventNameVT);
            dateVT = itemView.findViewById(R.id.dateVT);
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

