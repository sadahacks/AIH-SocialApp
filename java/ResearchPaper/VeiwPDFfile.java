package com.example.smartapp.ResearchPaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.smartapp.UserClass.ClassArticles;

import com.example.smartapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VeiwPDFfile extends AppCompatActivity {

    ListView mPDFListView;
    DatabaseReference reference;
    List<ClassArticles> mUser;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiw_pdffile);

        mPDFListView=findViewById(R.id.Pdf_list_item);

        mUser=new ArrayList<>();

        ViewAllFiles();

        mPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassArticles classArticles=mUser.get(position);

                Intent intent=new Intent();
                intent.setData(Uri.parse(classArticles.getUrlPDF()));
                startActivity(intent);
            }
        });

/*

        String namePDF=intent.getStringExtra("namePDF");
        String urlPDF=intent.getStringExtra("urlPDF");
*/




    }

    private void ViewAllFiles() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String userID=user.getUid();
        reference= FirebaseDatabase.getInstance().getReference().child("Articles").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    ClassArticles classArticles=dataSnapshot.getValue(ClassArticles.class);

                    mUser.add(classArticles);

                }
                String[] profile= new String[mUser.size()];
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,profile){

                };
                mPDFListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
}
