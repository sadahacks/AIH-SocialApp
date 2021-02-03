package com.example.smartapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings_Activity extends AppCompatActivity {


    private Button update_data_btn;
    private CircleImageView userProfileImage;
    private EditText name_update,uni_update;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    ProgressDialog dialog;
    String Display_name;
    String Profile_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_);



        name_update=(EditText)findViewById(R.id.setting_et_name);
        update_data_btn=(Button)findViewById(R.id.Update_btn);

        final FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();


        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference= firebaseDatabase.getReference("User").child(mAuth.getUid());

        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                name_update.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Settings_Activity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
