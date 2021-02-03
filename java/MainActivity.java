package com.example.smartapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    /*Shared prefrence for keep logged in*/

    SharedPreferences sharedPreferences;

    public static final String filename="MainInterface";
    public static final String SharedEmail="";
    public static final String SharedPassword="";

    private FirebaseAuth firebaseAuth;

    private String userID;

    FirebaseAuth mAuth;
    EditText lgmail,lgpass;
    Button sgnin, sgnup;
    TextView forget;

    ProgressDialog mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth= FirebaseAuth.getInstance();
        lgmail=(EditText)findViewById(R.id.usermail);
        lgpass=(EditText)findViewById(R.id.userpass);
        sgnin=(Button) findViewById(R.id.login_btn);
        sgnup=(Button) findViewById(R.id.btn_signup);
        forget=(TextView)findViewById(R.id.forget_pass);


        mprogress=new ProgressDialog(this);
        mprogress.setTitle("Wait for login");
        mprogress.setMessage("Please wait");

        /*Shared prefrence for keep logged in*/

        sharedPreferences=getSharedPreferences(filename, Context.MODE_PRIVATE);

        if(sharedPreferences.contains(SharedEmail)){
            startActivity(new Intent(MainActivity.this,MainInterface.class));

        }

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,forgetPassword.class));
            }
        });


        sgnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });

        sgnin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String lgEmail= lgmail.getText().toString().trim();
                String lgPass= lgpass.getText().toString();

                lgEmail.toLowerCase();
                if(TextUtils.isEmpty(lgEmail)||TextUtils.isEmpty(lgPass))
                {
                    if(TextUtils.isEmpty(lgEmail))
                    {
                        lgmail.setError("Please enter valid email");
                    }
                    if(TextUtils.isEmpty(lgPass))
                    {
                        lgpass.setError("Please enter password");
                    }
                }
                else
                {
                    loginUse(lgEmail,lgPass);
                }



            }
        });

    }

    private void loginUse(final String lgEmail, final String lgPass) {
        mprogress.show();
        mAuth.signInWithEmailAndPassword(lgEmail,lgPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Boolean emailflag = firebaseUser.isEmailVerified();

                if(task.isSuccessful()){
                    if(emailflag){

                        /*Shared prefrence for keep logged in*/
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString(SharedEmail,lgEmail);
                        editor.putString(SharedPassword,lgPass);
                        editor.commit();


                        Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                        Intent loginDash= new Intent(MainActivity.this,MainInterface.class);
                        startActivity(loginDash);
                        mprogress.dismiss();
                        finish();
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Verify your Email",Toast.LENGTH_SHORT).show();
                        mprogress.dismiss();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Enter Correct Email and Password",Toast.LENGTH_SHORT).show();
                    mprogress.dismiss();
                }
            }
        });
    }



}
