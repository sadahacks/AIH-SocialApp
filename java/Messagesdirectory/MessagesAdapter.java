package com.example.smartapp.Messagesdirectory;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewholder> {
    private List<Messages> usermesssagelist;
    FirebaseAuth mAuth;
    DatabaseReference usersDatabaseRefernce;

    public MessagesAdapter(List<Messages> usermesssagelist) {
        this.usermesssagelist = usermesssagelist;
    }
    public  class MessageViewholder extends RecyclerView.ViewHolder{
        public TextView Sendermsgtxt,Recievermsgtxt;
        public MessageViewholder(@NonNull View itemView) {
            super(itemView);
            Sendermsgtxt=itemView.findViewById(R.id.sender_message_text);
            Recievermsgtxt=itemView.findViewById(R.id.reciever_message_text);
        }
    }

    @NonNull
    @Override
    public MessageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout_of_users,
                parent,false);
        mAuth=FirebaseAuth.getInstance();
        return new MessageViewholder(v);

    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBindViewHolder(@NonNull MessageViewholder holder, int position) {
        String messagesenderid=mAuth.getCurrentUser().getUid();
        Messages messages=usermesssagelist.get(position);
        String fromuserid=messages.getFrom();
        holder.Sendermsgtxt.setVisibility(View.INVISIBLE);
        holder.Recievermsgtxt.setVisibility(View.INVISIBLE);
        Log.e("idddd dekh",fromuserid);
        if(fromuserid.equals(messagesenderid)){
holder.Sendermsgtxt.setText(messages.getMessage());
            holder.Sendermsgtxt.setVisibility(View.VISIBLE);
            holder.Recievermsgtxt.setVisibility(View.INVISIBLE);


        }
        else{
holder.Sendermsgtxt.setVisibility(View.INVISIBLE);
            holder.Recievermsgtxt.setVisibility(View.VISIBLE);
            holder.Recievermsgtxt.setText(messages.getMessage());
            Log.e("maa aya hn else k andar","andar");
        }


    }

    @Override
    public int getItemCount() {
        return usermesssagelist.size();
    }
}
