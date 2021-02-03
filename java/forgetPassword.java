package com.example.smartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPassword extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText email;
    Button reset;
    ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email=(EditText)findViewById(R.id.emailet);
        reset=(Button) findViewById(R.id.Reset_btn);
        mAuth=FirebaseAuth.getInstance();


        mprogress=new ProgressDialog(this);
        mprogress.setTitle("Sending");
        mprogress.setMessage("Please wait");




        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogress.show();
                String passEmail= email.getText().toString().trim();

                if(passEmail.equals("")){
                    Toast.makeText(forgetPassword.this,"please enter registered Email!",Toast.LENGTH_SHORT).show();
                    mprogress.dismiss();
                }else {
                    mAuth.sendPasswordResetEmail(passEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(forgetPassword.this,"Reset Email sent!",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgetPassword.this,MainActivity.class));
                                mprogress.dismiss();
                            }else{
                                Toast.makeText(forgetPassword.this,"Error in sending reset password!",Toast.LENGTH_SHORT).show();
                                mprogress.dismiss();
                            }
                        }
                    });
                }

            }
        });
    }
}
