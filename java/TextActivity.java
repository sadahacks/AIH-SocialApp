package com.example.smartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartapp.UserClass.ProfileUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TextActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView ItemUserName;

    FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText text_Send;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        text_Send=findViewById(R.id.text_Send);
        btn_send=findViewById(R.id.btn_Send);
        profile_image=findViewById(R.id.profile_image);
        ItemUserName=findViewById(R.id.ItemUserName);
        intent = getIntent();

        final String userID= intent.getStringExtra("userID");


       // const userID.replace('.',',');


        fuser= FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=text_Send.getText().toString();
                if(!msg.equals("")){
                    sendMessages(fuser.getUid(),userID,msg);
                }else {
                    Toast.makeText(TextActivity.this, "You can't send Empty msg", Toast.LENGTH_SHORT).show();
                }
                text_Send.setText("");
            }
        });

        assert userID != null;
        reference= (DatabaseReference) FirebaseDatabase.getInstance().getReference().child("User").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileUser profileUser=dataSnapshot.getValue(ProfileUser.class);
                ItemUserName.setText(profileUser.getName());
                if(profileUser.getPhotoUrl().equals("Default")){
                    profile_image.setImageResource(R.drawable.cycle_profile);
                }else {
                    Picasso.get().load(profileUser.getPhotoUrl()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
    private void sendMessages(String sender,String receiver,String messages){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Massages");

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("messages",messages);

        reference.setValue(hashMap);
    }
}
