package com.example.smartapp.Messagesdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class allusershow extends AppCompatActivity {
    LinearLayoutManager linearLayoutManager;
    FirebaseRecyclerAdapter adapter;
    RecyclerView reccyclerView;
    DatabaseReference doctorref;
    EditText et;
    ImageButton iv;
    String searchtext;
    Query Doctorquery=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allusershow);
        et=findViewById(R.id.searchedittext);
        iv=findViewById(R.id.searchbutton);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchtext=et.getText().toString();
                Searchuser(searchtext);
            }
        });

iv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        searchtext=et.getText().toString();
        if(searchtext.isEmpty()){
            et.setError("Please enter name to search");
        }
        Searchuser(searchtext);
    }
});
        doctorref= FirebaseDatabase.getInstance().getReference().child("User");
        reccyclerView=findViewById(R.id.alluserrecyclerView);
        
        linearLayoutManager = new LinearLayoutManager(this);

        reccyclerView.setLayoutManager(new LinearLayoutManager(allusershow.this));
        reccyclerView.setNestedScrollingEnabled(false);
        fetch();
    }
    private void Searchuser(String searchBoxInput) {
        Doctorquery= doctorref.orderByChild("Name").startAt(searchBoxInput)
                .endAt(searchBoxInput.concat("\uf8ff"));
        FirebaseRecyclerOptions<allusermodel> options =
                new FirebaseRecyclerOptions.Builder<allusermodel>()
                        .setQuery(Doctorquery, new SnapshotParser<allusermodel>() {
                            @NonNull
                            @Override
                            public allusermodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new allusermodel(snapshot.child("Name").getValue().toString(),
                                        snapshot.child("Institute").getValue().toString());
                            }
                        })
                        .build();

        adapter=new FirebaseRecyclerAdapter<allusermodel,ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull allusermodel allusermodel) {

                holder.setTxtinstitue(allusermodel.getInstitute());
                holder.setTxtusername(allusermodel.getName());
                final String visituserid =getRef(position).getKey();
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
Intent i=new Intent(allusershow.this,Chatactivity.class);
i.putExtra("id",visituserid);
startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.alluserlist, parent, false);

                return new ViewHolder(view);
            }




        };
        reccyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("User");
        FirebaseRecyclerOptions<allusermodel> options =
                new FirebaseRecyclerOptions.Builder<allusermodel>()
                        .setQuery(query, new SnapshotParser<allusermodel>() {
                            @NonNull
                            @Override
                            public allusermodel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new allusermodel(snapshot.child("Name").getValue().toString(),
                                        snapshot.child("Institute").getValue().toString());
                            }
                        })
                        .build();
        adapter=new FirebaseRecyclerAdapter<allusermodel, ViewHolder>(options) {




            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull allusermodel allusermodel) {

                holder.setTxtinstitue(allusermodel.getInstitute());
                holder.setTxtusername(allusermodel.getName());
                final String visituserid =getRef(position).getKey();
holder.root.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent(allusershow.this,Chatactivity.class);
        i.putExtra("id",visituserid);
        startActivity(i);

    }
});
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.alluserlist, parent, false);

                return new ViewHolder(view);
            }




        };
        reccyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout root;
        public TextView txtusername;
        public TextView txtinstitute;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root=itemView.findViewById(R.id.liist_root);
            txtinstitute=itemView.findViewById(R.id.alluseuniname);
            txtusername=itemView.findViewById(R.id.allusername);

        }

        public void setTxtinstitue(String stringb) {
            txtinstitute.setText(stringb);
        }
        public void setTxtusername(String stringc) {
            txtusername.setText(stringc);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
