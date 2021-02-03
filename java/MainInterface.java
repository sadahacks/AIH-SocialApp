package com.example.smartapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartapp.Post.DiscussionActivity;
import com.example.smartapp.ResearchPaper.Research_Paper_Activity;
import com.example.smartapp.UserClass.ProfileUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MainInterface extends AppCompatActivity {

    private static final String TAG="MainInterface";
    private static final int REQUEST_CODE =1 ;

    /*Shared prefrence for keep logged in*/

    SharedPreferences sharedPreferences;

    public static final String filename="MainInterface";
    public static final String SharedEmail="";

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    Button messages,btn3,researchParer,groups,about,setting,logout,disscusiongroup;
    TextView interfaceName,interfaceStatus,side_user_name;
    ConstraintLayout maincontent;
    LinearLayout mainmenu;
    Animation fromtop,frombottom;




    private Toolbar mtoolbar;

    //Image Veiw or Edit profile

    ImageView profilepic,btnMenu;

    Button profile;

    // storage Ref
    StorageReference storageReference;


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater=getMenuInflater();
//        inflater.inflate(R.menu.main_menu,menu);
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_interface);

        verifyPermissions();


        messages=(Button)findViewById(R.id.Messages);
        disscusiongroup=(Button)findViewById(R.id.disscusiongroup);
        btn3=(Button)findViewById(R.id.button3);
        researchParer=(Button)findViewById(R.id.research_btn);
        interfaceName=(TextView)findViewById(R.id.textView6);
        interfaceStatus=(TextView)findViewById(R.id.textView7);


        setting=(Button)findViewById(R.id.Settings);
        logout=(Button)findViewById(R.id.Logout);
        profile=(Button)findViewById(R.id.Profile);
        groups=(Button)findViewById(R.id.Groups);
        about=(Button)findViewById(R.id.About);
        profilepic=(ImageView)findViewById(R.id.Profilepic);
        side_user_name=(TextView)findViewById(R.id.Side_user_name);

        fromtop= AnimationUtils.loadAnimation(this,R.anim.fromtop);
        frombottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);

        btnMenu=(ImageView) findViewById(R.id.btnmenu);
        maincontent=(ConstraintLayout)findViewById(R.id.maincontent);
        mainmenu=(LinearLayout)findViewById(R.id.mainmenu);

       /* mtoolbar=(Toolbar)findViewById(R.id.main_interface_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Academic Information Hub");*/




        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String userID=user.getUid();
        firebaseDatabase= FirebaseDatabase.getInstance();

        //for storage
        storageReference= FirebaseStorage.getInstance().getReference();

        final StorageReference profileRef=storageReference
                .child("users/"+mAuth.getCurrentUser().getUid()+System.currentTimeMillis()+"Profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilepic);
            }
        });


        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainInterface.this,com.example.smartapp.Messagesdirectory.allusershow.class));
            }
        });

        disscusiongroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainInterface.this, DiscussionActivity.class));
            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainInterface.this, Settings_Activity.class));
            }
        });

        researchParer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainInterface.this, Research_Paper_Activity.class));
            }
        });




        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                maincontent.animate().translationX(0);
                mainmenu.animate().translationX(0);

                profile.startAnimation(frombottom);
                groups.startAnimation(frombottom);
                setting.startAnimation(frombottom);
                about.startAnimation(frombottom);
                logout.startAnimation(frombottom);

                profilepic.startAnimation(fromtop);
                side_user_name.startAnimation(fromtop);
            }
        });
        maincontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                maincontent.animate().translationX(-580);
                mainmenu.animate().translationX(-580);

            }
        });






        final DatabaseReference databaseReference= firebaseDatabase.getReference("User").child(userID);


        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ProfileUser profileUser = dataSnapshot.getValue(ProfileUser.class);

                assert profileUser != null;
                side_user_name.setText(profileUser.getName());
                interfaceName.setText(profileUser.getName());
                if(profileUser.getPhotoUrl().equals("Default")){
                    profilepic.setImageResource(R.drawable.cycle_profile);
                }else{
                    Picasso.get().load(profileUser.getPhotoUrl()).into(btnMenu);
                    Picasso.get().load(profileUser.getPhotoUrl()).into(profilepic);
                    //Glide.with(MainInterface.this).load(profileUser.getPhotoUrl()).into(profilepic);
                }

                /*String name= dataSnapshot.child("Name").getValue().toString();
                interfaceName.setText(name);
                side_user_name.setText(name);*/
               /* String imageUri=dataSnapshot.child("Profile_Pic/photoUrl").getValue().toString();
                Picasso.get().load(imageUri).into(btnMenu);*/

                //if (databaseReference.child("Profile/photoUrl"));
                /*
                ProfileUser usrprofile = dataSnapshot.getValue(ProfileUser.class);
                usrprofile.setName(usrprofile.getName());
                usrprofile.setMail(usrprofile.getMail());
                usrprofile.setUniversity(usrprofile.getUniversity());
*/

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(MainInterface.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open gallery
                Intent OpenGalleryIntent=new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(OpenGalleryIntent,1000);
            }
        });

        // goto Reg_Universities

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainInterface.this,Reg_Universities.class));
            }
        });

        /*Shared prefrence for keep logged in*/

        sharedPreferences=getSharedPreferences(filename, Context.MODE_PRIVATE);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(SharedEmail);
                editor.commit();

                finish();

                startActivity(new Intent(MainInterface.this,MainActivity.class));
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1000){
            if(resultCode==Activity.RESULT_OK);

            Uri imageuri=data.getData();
            //profilepic.setImageURI(imageuri);

            uploadImageToFirebase(imageuri);

        }
    }

    private void uploadImageToFirebase(Uri imageuri) {
        // upload Image To Firebase storage

        final StorageReference fileref=storageReference.
                child("users/"+mAuth.getCurrentUser().getUid()+"Profile.jpg");

        fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilepic);

                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();
                        //assert user != null;
                        String userID=user.getUid();


                        DatabaseReference imageStore=firebaseDatabase.getReference("User").child(userID);
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("photoUrl",String.valueOf(uri));

//                        String id=imageStore.push().getKey();

                        imageStore.updateChildren(hashMap);
/*

                        if(uri != null){
                            Picasso.get().load(uri).into(profilepic);
                            Picasso.get().load(uri).into(btnMenu);
                        }
*/
                    }
                });
                Toast.makeText(MainInterface.this, "Image Upload", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainInterface.this, "Uploading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void verifyPermissions(){

        Log.d(TAG,"VerifyPermissions:asking for permissions");
        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED) {

            setupVeiwpager();
        }else{
            ActivityCompat.requestPermissions(MainInterface.this,permissions,
                    REQUEST_CODE);
        };

    }

    private void setupVeiwpager() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }

}
