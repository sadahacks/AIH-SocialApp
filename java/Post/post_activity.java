package com.example.smartapp.Post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartapp.MainInterface;
import com.example.smartapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class post_activity extends AppCompatActivity {
ImageView postimg;
Button postbtn;
EditText des;
int GALLERY_PICK=1;
    String savecurrentdate,savecurrenttime;
    private Uri imageuri;
    FirebaseAuth mAuth;
String currentuserid,postrandomname;

DatabaseReference postref,userref;
    StorageReference postimageRef;
    String downloadurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_activity);
        postimg=findViewById(R.id.posttimage);
        postbtn=findViewById(R.id.addpostbtn);
des=findViewById(R.id.postdescription);

        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("dd-MMMM-yyyy");
        savecurrentdate=currentdate.format(calfordate.getTime());

        Calendar calfortime=Calendar.getInstance();
        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm");
        savecurrenttime=currenttime.format(calfortime.getTime());
        postrandomname=savecurrentdate.concat(savecurrenttime);
mAuth=FirebaseAuth.getInstance();
currentuserid=mAuth.getCurrentUser().getUid();
        postimageRef= FirebaseStorage.getInstance().getReference();
postref=FirebaseDatabase.getInstance().getReference().child("posts");
        userref=FirebaseDatabase.getInstance().getReference().child("User").child(currentuserid);
postimg.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        OpenGallery();
    }
});
postbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(des.getText().toString().isEmpty()){
            Tost("Please add description");
        }
        else if(imageuri==null){
            Tost("Please add Image");
        }
        else {
            storeindatabase();

        }
    }
});

    }

    private void storeindatabase() {
        final StorageReference filepath=postimageRef.child("Post images").child(imageuri.getLastPathSegment() +".jpg");
filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
if(task.isSuccessful()){
    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {
            downloadurl=uri.toString();
            userref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datSsnapshot) {
               if(datSsnapshot.exists()){
                   String username = datSsnapshot.child("Name").getValue().toString();
                   HashMap postmap=new HashMap();
                   postmap.put("uid",currentuserid);
                   postmap.put("fullname",username);
                   postmap.put("date",savecurrentdate);
                   postmap.put("time",savecurrenttime);
                   postmap.put("descryption",des.getText().toString());
                   postmap.put("postimage",downloadurl);
                   postref.child(currentuserid.concat( postrandomname)).
                           updateChildren(postmap).addOnCompleteListener(new OnCompleteListener() {
                       @Override
                       public void onComplete(@NonNull Task task) {

                           final ProgressDialog progress = new ProgressDialog(post_activity.this);
                           final Timer t = new Timer();
                           progress.setTitle("Loading");
                           progress.setMessage("Wait while loading...");
                           progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                           progress.show();
                           t.schedule(new TimerTask() {
                               public void run() {

                                   Intent n=new Intent(post_activity.this,DiscussionActivity.class);
                                   startActivity(n);
                                   progress.dismiss();

                                   // when the task active then close the dialog
                                   t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                               }
                           }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.

                       }
                   });

               }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    });
}
    }
});
    }

    private void Tost(String a) {
        Toast.makeText(post_activity.this,a,Toast.LENGTH_SHORT).show();
    }

    private void OpenGallery() {
        Intent galleryintent=new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, GALLERY_PICK);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PICK&&resultCode==RESULT_OK&&data!=null){
            imageuri=data.getData();
            Picasso.get().load(imageuri).
                    resize(1500, 700).into(postimg);
            postimg.setImageURI(null);

        }
}
}