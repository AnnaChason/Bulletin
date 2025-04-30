package com.csci335.bulletin.Main;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/*
recycler view adapter for events
 */
public class NotificationRVAdapter extends RecyclerView.Adapter<NotificationRVAdapter.NotifViewHolder>{

    private ArrayList<Notif> notifs;
    private ArrayList<Notif> allNots;
    private Context context;

    public NotificationRVAdapter(ArrayList<Notif> notifs, ArrayList<Notif> allNots, Context context){
        this.notifs = notifs;
        this.context = context;
        this.allNots = allNots;
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
        holder.readTv.setText(setReadMsg(position));

        holder.readTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifs.get(holder.getAdapterPosition()).setRead(!notifs.get(holder.getAdapterPosition()).getRead());
                holder.readTv.setText(setReadMsg(holder.getAdapterPosition()));

                //updating read status in database
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                if(UserLoadingScreen.getCurrentUserType() == 2)
                    db.collection("organizationInfo").document(fAuth.getCurrentUser().getUid()).update("notifications", allNots);
                else
                    db.collection("studentInfo").document(fAuth.getCurrentUser().getUid()).update("notifications", allNots);

            }
        });
    }

    @Override
    public int getItemCount() {
        return notifs.size();
    }

    public SpannableString setReadMsg(int position){
        SpannableString readMsg;
        if(notifs.get(position).getRead())
            readMsg = new SpannableString(context.getString(R.string.unread));

        else
            readMsg = new SpannableString(context.getString(R.string.read));

        readMsg.setSpan(new UnderlineSpan(), 0, readMsg.length(), 0);

        return readMsg;
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
