package com.example.smartapp.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartapp.Adapter.postAdapter;
import com.example.smartapp.MainInterface;
import com.example.smartapp.R;
import com.example.smartapp.UserClass.ProfileUser;
import com.example.smartapp.UserClass.postClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DiscussionActivity extends AppCompatActivity {

TextView adpostactivity;
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;
    RecyclerView reccyclerView;
    DatabaseReference doctorref;
    FirebaseAuth mAuth;
    String Currentuserid,clickpostimage,clickpostdescrytion,visitkey;
    DatabaseReference deleteref,clickpostref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
adpostactivity=findViewById(R.id.addpost);
mAuth=FirebaseAuth.getInstance();
Currentuserid=mAuth.getCurrentUser().getUid();
deleteref=FirebaseDatabase.getInstance().getReference().child("posts");

        doctorref= FirebaseDatabase.getInstance().getReference().child("posts");
        reccyclerView=findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);

        reccyclerView.setLayoutManager(new LinearLayoutManager(DiscussionActivity.this));
        fetch();
adpostactivity.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent p=new Intent(DiscussionActivity.this,post_activity.class);
        startActivity(p);
    }
});
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("posts");
        FirebaseRecyclerOptions<postmodel> options =
                new FirebaseRecyclerOptions.Builder<postmodel>()
                        .setQuery(query, new SnapshotParser<postmodel>() {
                            @NonNull
                            @Override
                            public postmodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new postmodel(
                                        snapshot.child("postimage").getValue().toString(),
                                        snapshot.child("descryption").getValue().toString(),
                                        snapshot.child("fullname").getValue().toString(),
                                        snapshot.child("time").getValue().toString(),
                                        snapshot.child("date").getValue().toString()


                                       );
                            }
                        })
                        .build();
        adapter=new FirebaseRecyclerAdapter<postmodel,ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int pos, @NonNull postmodel postmodel) {
                holder.setTxtimageview(postmodel.getPostimage());
                holder.setTxtdate("Date:"+postmodel.getDate()+"\n Time:"+postmodel.getTime());
                holder.setTxtusername(postmodel.getFullname()+" Posted: ");
                holder.setTxtdescription("Description: "+postmodel.getDescryption());
                holder.postcl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       visitkey=getRef(pos).getKey();

                        if(visitkey.contains(Currentuserid)){
                            new AlertDialog.Builder(DiscussionActivity.this)
                                    .setTitle("Delete entry")
                                    .setMessage("Are you sure you want to delete this entry?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("Delete Post", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            deleteref.child(visitkey).removeValue();
                                            Toast.makeText(DiscussionActivity.this," Delete post",Toast.LENGTH_SHORT).show();
                                            Intent again=new Intent(DiscussionActivity.this,DiscussionActivity.class);
                                            startActivity(again);

                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                        else{
                            Toast.makeText(DiscussionActivity.this,"Its not your post to delete"
                            ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.allpostlist, parent, false);

                return new ViewHolder(view);
            }



        };
        reccyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void currentvalues(String visitkey) {
        clickpostref.removeValue();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView txtimgview;
        public TextView txtdescrition;
        public TextView txtdatentime;
        public TextView txtusername;

public ConstraintLayout postcl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtimgview=itemView.findViewById(R.id.poostimageview);
            txtdescrition=itemView.findViewById(R.id.poostdescription);
            txtdatentime=itemView.findViewById(R.id.poostdateandtime);
            txtusername=itemView.findViewById(R.id.poostusername);
            postcl=itemView.findViewById(R.id.clallpost);


        }
        public void setTxtimageview(String stringd) {
            Picasso.get().load(stringd).into(txtimgview);
        }
        public void setTxtdescription(String stringa) {
            txtdescrition.setText(stringa);
        }
        public void setTxtdate(String stringb) {
            txtdatentime.setText(stringb);
        }
        public void setTxtusername(String stringc) {
            txtusername.setText(stringc);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}