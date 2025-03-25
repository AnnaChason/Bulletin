package com.csci335.bulletin.AdminClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csci335.bulletin.Event;
import com.csci335.bulletin.EventRecyclerViewAdapter;
import com.csci335.bulletin.R;

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
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.admin_recyler_view_row, parent,false);
        return new A_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull A_RecyclerViewAdapter.MyViewHolder holder, int position) {
        //assigning values
        holder.eventNameVT.setText(pendingEvents.get(position).getName());
        holder.dateVT.setText(pendingEvents.get(position).getDate());
        holder.locationVT.setText(pendingEvents.get(position).getLocation());
        holder.descriptionVT.setText(pendingEvents.get(position).getDescription());
        //holder.poster.setImageResource(pendingEvents.get(position).getPosterImg());
        holder.status.setText(pendingEvents.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return pendingEvents.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing views and assigning variables
        TextView eventNameVT, dateVT, locationVT,descriptionVT;
        ImageView poster;
        CheckBox status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventNameVT = itemView.findViewById(R.id.eventNameVT);
            dateVT = itemView.findViewById(R.id.dateVT);
            locationVT = itemView.findViewById(R.id.locationVT);
            descriptionVT = itemView.findViewById(R.id.descriptionVT);
            //poster = itemView.findViewById(R.id.eventPosterView);
            status = itemView.findViewById(R.id.pendingBtn);
        }
    }

}
