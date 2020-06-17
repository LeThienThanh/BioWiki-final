package com.biowiki.biowiki;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.biowiki.biowiki.models.Animal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class UserView extends AppCompatActivity {

    TextView username, userdec;
    ImageView avatar;

    ArrayList<Animal> animals;

    RecyclerView animalRecycler;

    DatabaseReference mreference;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        username = findViewById(R.id.userName1);
        userdec = findViewById(R.id.userDec);
        avatar = findViewById(R.id.avatar1);
        animalRecycler = findViewById(R.id.listuser);

        User user = (User) getIntent().getSerializableExtra("User");

        userId = user.getId();

        username.setText(user.getName());
        userdec.setText(user.getDec());
        Picasso.get().load(user.getImage()).into(avatar);

        animals = new ArrayList<>();

        animalRecycler.setHasFixedSize(false);
        animalRecycler.setLayoutManager(new LinearLayoutManager(this));

        mreference = FirebaseDatabase.getInstance().getReference();

        mreference.child("Animal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animals.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Animal animal = snapshot.getValue(Animal.class);

                    if (animal.getUserId().equals(userId)) animals.add(animal);
                }

                Collections.sort(animals, new Animal());

                AnimalAdapter dulieu = new AnimalAdapter(UserView.this, animals);
                animalRecycler.setAdapter(dulieu);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
