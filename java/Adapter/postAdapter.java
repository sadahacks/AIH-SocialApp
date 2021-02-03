package com.example.smartapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartapp.UserClass.ProfileUser;
import com.example.smartapp.UserClass.postClass;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class postAdapter extends RecyclerView.Adapter<postAdapter.Viewholder>{

    public Context mContext;
    public List<postClass> mPost;

    private FirebaseUser fUser;

    public postAdapter(Context mContext, List<postClass> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.post_items,parent,false);
        return new postAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        fUser= FirebaseAuth.getInstance().getCurrentUser();

        postClass postClass=mPost.get(position);

        Picasso.get().load(postClass.getPostImage()).into(holder.post_Image);

        if (postClass.getDescription().equals("")){
            holder.description.setVisibility(View.GONE);
        }else{
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(postClass.getDescription());
        }

        profileInfoUser(holder.Image_profile, holder.userName,holder.publisher,postClass.getPublisher());


    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public  class Viewholder extends RecyclerView.ViewHolder{
        public ImageView Image_profile,post_Image,comments_Image,like,save;
        public TextView userName,likes,publisher,description,comment;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Image_profile=itemView.findViewById(R.id.Image_profile);
            post_Image=itemView.findViewById(R.id.post_Image);
            comments_Image=itemView.findViewById(R.id.comments_Image);
            like=itemView.findViewById(R.id.like);
            save=itemView.findViewById(R.id.save);
            userName=itemView.findViewById(R.id.ItemUserName);
            likes=itemView.findViewById(R.id.likes);
            publisher=itemView.findViewById(R.id.publisher);
            comment=itemView.findViewById(R.id.comment);
            description=itemView.findViewById(R.id.description);


        }
    }

    private void profileInfoUser(final ImageView Image_profile, final TextView userName, final TextView publisher, String userID){

        DatabaseReference dRef= FirebaseDatabase.getInstance().getReference("User").child(userID);

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileUser profileUser=dataSnapshot.getValue(ProfileUser.class);

                Picasso.get().load(profileUser.getPhotoUrl()).into(Image_profile);
                userName.setText(profileUser.getName());
                publisher.setText(profileUser.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
