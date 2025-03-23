package com.csci335.bulletin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder>{

    private ArrayList<Event> events;
    private Context context;


    public EventRecyclerViewAdapter(Context context, ArrayList<Event> events){
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    //inflates layout and gives look to each row
    public EventRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.event_recyclerview_row, parent,false);
        return new EventRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    //assigns value to each row as it comes on screen
    public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.eventNameVT.setText(events.get(position).getEventName());
        holder.dateVT.setText(events.get(position).getDate());
        holder.locationVT.setText(events.get(position).getLocation());
        holder.descriptionVT.setText(events.get(position).getDescription());
        holder.numAttendingVT.setText(""+ events.get(position).getAttending() + " people attending");
        holder.poster.setImageResource(events.get(position).getPosterImg());
        holder.categoryVT.setText("#"+events.get(position).getCategory());


        /*
        updates attendance
         */
        holder.attendingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                updates event attendance, fix when we sort out differences between event and event application also need to deal with checked/vs unchecked
                also need to figure out how to store which users are attending, and for the user which events they are attending.
                */
                if(holder.attendingBtn.isChecked())
                    events.get(holder.getAdapterPosition()).updateAttending(1);
                else
                    events.get(holder.getAdapterPosition()).updateAttending(-1);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("eventApplications").document(events.get(holder.getAdapterPosition()).getEventName()).update("attendance",events.get(holder.getAdapterPosition()).getAttending());
                notifyDataSetChanged();//not best practice but doesn't work when you only update the individual item
            }
        });
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

        }
    }

}

