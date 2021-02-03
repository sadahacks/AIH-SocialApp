package com.example.smartapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference reference;


    Button btnregister;
    EditText etname,etuni,etmail,etpass;
    String emaill,nme,pswd,uni;
    ProgressDialog mprogress;
    RadioGroup radiogroup;
    RadioButton selectedradio;

    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        mAuth = FirebaseAuth.getInstance();

        etmail=(EditText)findViewById(R.id.reg_mail);
        etpass=(EditText)findViewById(R.id.reg_pass);
        etname=(EditText)findViewById(R.id.fullname);
        etuni=(EditText)findViewById(R.id.university);
        btnregister=(Button)findViewById(R.id.Reg_btn);
        radiogroup=(RadioGroup)findViewById(R.id.radiog);



        selectedradio=radiogroup.findViewById(radiogroup.getCheckedRadioButtonId());

        mprogress=new ProgressDialog(this);
        mprogress.setTitle("Registering");
        mprogress.setMessage("Please wait");


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emaill=etmail.getText().toString().trim();
                nme=etname.getText().toString().trim();
                pswd=etpass.getText().toString().trim();
                uni=etuni.getText().toString().trim();


                emaill.toLowerCase();
                if(TextUtils.isEmpty(emaill)||TextUtils.isEmpty(pswd)||TextUtils.isEmpty(nme)||TextUtils.isEmpty(uni)){
                    if(TextUtils.isEmpty(emaill))
                    {
                        etmail.setError("Please enter Email Address");
                    }
                    if(TextUtils.isEmpty(pswd))
                    {
                        if (pswd.length() < 6){
                            etpass.setError("Password length > 6");
                        }else{
                            etpass.setError("Please enter Password");
                        }
                    }
                    if(TextUtils.isEmpty(nme))
                    {
                        etname.setError("Please enter Name");
                    }
                    if(TextUtils.isEmpty(uni))
                    {
                        etuni.setError("Please enter University");
                    }

                }/*else if(!emaill.contains("edu.pk"))
                {
                    etmail.setError("Please enter valid edu.pk email");
                }*/
                else
                {
                    RegisterUser(emaill,pswd);
                }


            }
        });
    }

    private void RegisterUser(final String eemail, String epass) {

        mprogress.show();
        mAuth.createUserWithEmailAndPassword(eemail,epass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                final FirebaseUser user = auth.getCurrentUser();
                //assert user != null;
                /*String userID=user.getUid();
                reference=FirebaseDatabase
                        .getInstance().getReference("User").child(userID);

                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("Name",nme);
                hashMap.put("Password",pswd);
                hashMap.put("Institute",uni);
                hashMap.put("EmailID",emaill);
                hashMap.put("photoUrl","Default");
                hashMap.put("urlPDF","Default");
                reference.setValue(hashMap);
               *//* reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Registration Successfully, Verification mail sent!", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent loginintent = new Intent(Register.this, MainActivity.class);
                            loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(loginintent);
                            mprogress.dismiss();
                        } else {

                            Toast.makeText(Register.this, "Registration fail", Toast.LENGTH_SHORT).show();
                            mprogress.dismiss();
                        }
                    }
                });*/
                if(user!=null) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                String userID=user.getUid();
                                reference=FirebaseDatabase
                                        .getInstance().getReference("User").child(userID);

                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("Name",nme);
                                hashMap.put("Password",pswd);
                                hashMap.put("Institute",uni);
                                hashMap.put("userUid",userID);
                                hashMap.put("EmailID",emaill);
                                hashMap.put("photoUrl","Default");
                                reference.setValue(hashMap);

                                //                              SendToDatabase();
                                Toast.makeText(Register.this, "Registration Successfully, Verification mail sent!", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent loginintent = new Intent(Register.this, MainActivity.class);
                                startActivity(loginintent);
                                mprogress.dismiss();
                            } else {

                                Toast.makeText(Register.this, "Registration fail", Toast.LENGTH_SHORT).show();
                                mprogress.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }
    /*private void SendToDatabase(){


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(mAuth.getUid());

        *//*HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("photoUrl","Default");
        myRef.setValue(hashMap);*//*

        ProfileUser userData=new ProfileUser(nme,emaill,uni,pswd);

        myRef.setValue(userData);
    }*/
}
