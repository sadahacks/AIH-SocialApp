package com.example.smartapp.Messagesdirectory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Chatactivity extends AppCompatActivity {
    String visitid,currentuserid,messagepushkey;
DatabaseReference userRef;
EditText chatet;
TextView tn;
FirebaseAuth mAuth;
String savecurrentdate,savecurrenttime;
RecyclerView userMessageList;
private final List<Messages> messagesList=new ArrayList<>();
private LinearLayoutManager linearLayoutManager;
private MessagesAdapter messagesAdapter;

ImageButton send;
DatabaseReference senderref,recieveref,rootref,usermesagref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);
        Intent j=getIntent();
   visitid=j.getStringExtra("id");
   tn=findViewById(R.id.toolbarname);
   chatet=findViewById(R.id.chatedittext);
   send=findViewById(R.id.sendbutton);
mAuth=FirebaseAuth.getInstance();
rootref=FirebaseDatabase.getInstance().getReference();


        messagesAdapter=new MessagesAdapter(messagesList);
        userMessageList=(RecyclerView) findViewById(R.id.messageslistusers);

        linearLayoutManager=new LinearLayoutManager(Chatactivity.this);
        userMessageList.setLayoutManager(linearLayoutManager);
        userMessageList.setAdapter(messagesAdapter);


   Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("dd-MMMM-yyyy");
        savecurrentdate=currentdate.format(calfordate.getTime());

        Calendar calfortime=Calendar.getInstance();
        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm");
        savecurrenttime=currenttime.format(calfortime.getTime());


currentuserid=mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("User");
        FetchMessages();

send.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        sendmessage( chatet.getText().toString());
    }
});




        userRef.child(visitid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(dataSnapshot.hasChild("Name")){
                        String fullname = dataSnapshot.child("Name").getValue().toString();
                       tn.setText(fullname);
                    }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchMessages() {
        rootref.child("messages").child(currentuserid).child(visitid)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot datasnapshot, @Nullable String previousChildName) {
                    if(datasnapshot.exists()){
                    Messages messages=datasnapshot.getValue(Messages.class);
                    messagesList.add(messages);
                    messagesAdapter.notifyDataSetChanged();
                    Log.e("i Am here","here");
                            }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sendmessage(String chatdata) {
        if(chatdata.isEmpty()){
            Toast.makeText(Chatactivity.this,"Please enter message",Toast.LENGTH_SHORT).show();
        }
        else{
            senderref=FirebaseDatabase.getInstance().getReference().child("messages").child(currentuserid)
                    .child(visitid);
            recieveref=FirebaseDatabase.getInstance().getReference().child("messages").child(visitid)
                    .child(currentuserid);
            usermesagref=rootref.child("messages").child(currentuserid)
                    .child(visitid).push();
            messagepushkey=usermesagref.getKey();
            HashMap map=new HashMap();
            map.put("message",chatdata);
            map.put("date",savecurrentdate);
            map.put("time",savecurrenttime);
            map.put("from",currentuserid);
            recieveref.child(messagepushkey).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Log.e("updated","data");
                }
            });
            senderref.child(messagepushkey).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
Log.e("updated","data");

                }
            });

        }

    }


}
