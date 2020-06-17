package com.biowiki.biowiki;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.biowiki.biowiki.models.Animal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class Data extends Fragment {

    EditText searchdata;
    ImageView bckgr, btnchangetype;

    ArrayList<Animal> animals, animalsSearch;
    ArrayList<User> users, usersSearch;

    RecyclerView mResultList;

    int  CoutCT = 1;
    public DatabaseReference mreference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_data, null);

        searchdata = view.findViewById(R.id.Searchdata);
        mResultList = view.findViewById(R.id.result_list);
        btnchangetype = view.findViewById(R.id.btnchangetype);
        bckgr = view.findViewById(R.id.backgroundsearch);

        animals = new ArrayList<>();
        users = new ArrayList<>();

        animalsSearch = new ArrayList<>();
        usersSearch = new ArrayList<>();

        mreference = FirebaseDatabase.getInstance().getReference();

        mResultList.setHasFixedSize(false);
        mResultList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mResultList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        mResultList.setVisibility(View.GONE);

        btnchangetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CoutCT == 1){
                    btnchangetype.setImageResource(R.drawable.man); CoutCT = 2;
                } else  if (CoutCT == 2){
                    btnchangetype.setImageResource(R.drawable.whale); CoutCT = 1;
                }
            }
        });

        searchdata.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    setAdapter(s.toString().toLowerCase());
                    mResultList.setVisibility(View.VISIBLE);
                    bckgr.setVisibility(View.INVISIBLE);
                } else {
                    mResultList.removeAllViews();
                    mResultList.setVisibility(View.INVISIBLE);
                    bckgr.setVisibility(View.VISIBLE);
                }
            }
        });


        mreference.child("Animal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animals.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Animal animal = snapshot.getValue(Animal.class);
                    animals.add(animal);

                }

                Collections.sort(animals, new Animal());

                setAdapter(searchdata.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mreference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    User user = snapshot.getValue(User.class);
                    users.add(user);
                }

                setAdapter(searchdata.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void setAdapter(final String searchstring) {
        if (CoutCT == 1){
            animalsSearch.clear();

            for (Animal animal : animals)
                if (animal.getName().toLowerCase().contains(searchstring)) {
                    animalsSearch.add(animal);
                }

            AnimalAdapter dulieu = new AnimalAdapter(getActivity(), animalsSearch);
            mResultList.setAdapter(dulieu);
        } else {

            usersSearch.clear();

            for (User user : users)
                if (user.getName().toLowerCase().contains(searchstring)) {
                    usersSearch.add(user);
                }

            UserAdapter data = new UserAdapter(getActivity(), usersSearch);
            mResultList.setAdapter(data);
        }
    }
}

