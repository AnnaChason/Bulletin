package com.csci335.bulletin.Main;

import android.content.Context;
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

import com.csci335.bulletin.Events.EventRecyclerViewAdapter;
import com.csci335.bulletin.R;

import java.util.ArrayList;

/*
recycler view adapter for events
 */
public class NotificationRVAdapter extends RecyclerView.Adapter<NotificationRVAdapter.NotifViewHolder>{

    private ArrayList<Notif> notifs;
    private Context context;

    public NotificationRVAdapter(ArrayList<Notif> notifs, Context context){
        this.notifs = notifs;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationRVAdapter.NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(context);
        View view = inflator.inflate(R.layout.notification_recycler_view_row, parent,false);
        return new NotificationRVAdapter.NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRVAdapter.NotifViewHolder holder, int position) {
        holder.titleTv.setText(notifs.get(position).getTitle());
        holder.messageTv.setText(notifs.get(position).getMessage());


        holder.readTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifs.get(holder.getAdapterPosition()).setRead(!notifs.get(holder.getAdapterPosition()).getRead());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifs.size();
    }

    public static class NotifViewHolder extends RecyclerView.ViewHolder{
        TextView titleTv, messageTv, readTv;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.notifTitleVt);
            messageTv = itemView.findViewById(R.id.notifMsgTv);
            readTv = itemView.findViewById(R.id.readTv);
        }
    }
}
