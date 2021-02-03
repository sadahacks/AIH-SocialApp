package com.example.smartapp.ResearchPaper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartapp.R;
import com.example.smartapp.UserClass.ClassArticles;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class Research_Paper_Activity extends AppCompatActivity {

    EditText txt_Pdf_Btn;
    Button upload_pdf_Btn;

    FirebaseAuth mAuth;

    StorageReference sRef;
    DatabaseReference dRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research__paper_);

        txt_Pdf_Btn=findViewById(R.id.txt_Pdf_Btn);
        upload_pdf_Btn=findViewById(R.id.upload_pdf_Btn);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        String userID=user.getUid();

        sRef= FirebaseStorage.getInstance().getReference();
        dRef= FirebaseDatabase.getInstance().getReference("Articles").child(userID);

        upload_pdf_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPdfFile();
            }
        });

    }

    private void selectPdfFile() {

        Intent intent=new Intent();
        intent.setType("Application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select PDF File"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode== Activity.RESULT_OK &&
                data !=null && data.getData() != null){

            Uri pdfuri=data.getData();

            uploadPDFFile(pdfuri);

        }
    }

    private void uploadPDFFile(final Uri pdfuri) {
        // upload Image To Firebase storage

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading...");

        final StorageReference fileref=sRef.
                child("pdfFile/"+mAuth.getCurrentUser().getUid()+".pdf");

        fileref.putFile(pdfuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                //assert user != null;
                String userID=user.getUid();

                Task<Uri> uri= taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete());
                final Uri url=uri.getResult();

                assert url != null;

                ClassArticles classArticles=new ClassArticles(txt_Pdf_Btn.getText().toString(),url.toString());
                dRef.setValue(classArticles);

                Toast.makeText(Research_Paper_Activity.this, "File Upload", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress=(100.0*taskSnapshot.getBytesTransferred())
                        /taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded"+(int)progress+"%");
            }
        });
    }

    public void btn_action(View view) {
        startActivity(new Intent(getApplicationContext(),VeiwPDFfile.class));
    }
}
