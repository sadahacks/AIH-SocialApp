package com.example.smartapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartapp.UserClass.ProfileUser;
import com.example.smartapp.R;
import com.example.smartapp.TextActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<ProfileUser> mUsers;
    private String adapterTag;

    FirebaseUser fUser;

    public UserAdapter(Context mContext,List<ProfileUser> mUsers){
        this.mUsers=mUsers;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_items,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        fUser=FirebaseAuth.getInstance().getCurrentUser();
        final ProfileUser profileUser=mUsers.get(position);
        holder.Name.setText(profileUser.getName());
        Log.d(adapterTag,"Name"+profileUser.getName());
        if(profileUser.getPhotoUrl().equals("Default")){
            holder.profile_pic.setImageResource(R.drawable.cycle_profile);
        }else{
            Picasso.get().load(profileUser.getPhotoUrl()).into(holder.profile_pic);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, TextActivity.class);
                intent.putExtra("userID", FirebaseAuth.getInstance().getUid());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView Name;
        public ImageView profile_pic;

        public ViewHolder(View itemView){
            super(itemView);

            Name=itemView.findViewById(R.id.ItemUserName);
            profile_pic=itemView.findViewById(R.id.profile_image);

        }
    }

}
