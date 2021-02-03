package com.example.smartapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.smartapp.Adapter.UserAdapter;
import com.example.smartapp.UserClass.ProfileUser;
import com.example.smartapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.

 */
public class UserFragment extends Fragment {

    private RecyclerView recycler_View;
    private UserAdapter userAdapter;
    private List<ProfileUser> mUser;
    EditText search_bar;
    private String TAG;
    private  String userID;
    private String FragmentTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_user, container, false);

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        userID=user.getUid();


        recycler_View=(RecyclerView) rootView.findViewById(R.id.recycler_View);
        recycler_View.setHasFixedSize(true);
        recycler_View.setAdapter(userAdapter);
        recycler_View.setLayoutManager(new LinearLayoutManager(getContext()));

        search_bar=rootView.findViewById(R.id.search_bar);
        mUser= new ArrayList<>();

        readUsers();
        /*search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/ //search_bar listner
        return rootView;
    }

    private void searchUser(final String query) {
        /*final Query query=FirebaseDatabase.getInstance().getReference("User").child(userID).orderByChild("Name")
                .startAt(s)
                .endAt(s+"\uf8ff");
        */
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.
                getInstance().getReference("User");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    ProfileUser profileUser = snapshot.getValue(ProfileUser.class);
                    if (!profileUser.getEmailID().equals(firebaseUser.getUid())) {

                        if(profileUser.getName().toLowerCase().contains(query.toLowerCase()));
                        mUser.add(profileUser);
                        //Log.d(FragmentTag,"Name"+mUser.add(profileUser));
                    }


                }
                userAdapter = new UserAdapter(getContext(), mUser);
                recycler_View.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.
                getInstance().getReference("User");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if (search_bar.getText().toString().equals("")){

                    mUser.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                        ProfileUser profileUser = snapshot.getValue(ProfileUser.class);

                        assert profileUser != null;
                        assert firebaseUser != null;
                        if (!profileUser.getEmailID().equals(firebaseUser.getUid())) {
                            mUser.add(profileUser);
                            //Log.d(FragmentTag,"Name"+mUser.add(profileUser));
                        }
                    }
                //}

                userAdapter = new UserAdapter(getContext(), mUser);
                recycler_View.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.main_menu,menu);

        MenuItem item=menu.findItem(R.id.search_bar);

        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(TextUtils.isEmpty(s.trim())){
                    searchUser(s);
                }else {
                    readUsers();
                }

                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(TextUtils.isEmpty(s.trim())){
                    searchUser(s);
                }else {
                    readUsers();
                }

                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }
}
