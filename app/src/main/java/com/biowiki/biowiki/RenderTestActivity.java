package com.biowiki.biowiki;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.biowiki.biowiki.Adapter.RendererPagerAdapter;
import com.biowiki.biowiki.Models.Animal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class RenderTestActivity extends AppCompatActivity {

    DatabaseReference mref;
    StorageReference msto;
    ProgressBar progressBar;

    Button btnUpload;

    Long coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render_test);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnUpload = findViewById(R.id.btnUpload);

        progressBar = findViewById(R.id.progressBarUpload);
        progressBar.setVisibility(View.INVISIBLE);

        Animal animal = (Animal) getIntent().getSerializableExtra("Animal");

        msto = FirebaseStorage.getInstance().getReference();
        mref = FirebaseDatabase.getInstance().getReference();

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new RendererPagerAdapter(getSupportFragmentManager(),
                RenderTestActivity.this, animal.getDec()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        mref.child("Users").child(animal.getUserId()).child("coin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                coin = Long.parseLong(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnUpload.setVisibility(View.INVISIBLE);

                final StorageReference filepath = msto.child("Avatar").child(Objects.requireNonNull(Uri.parse(animal.getImage()).getLastPathSegment()));

                filepath.putFile(Uri.parse(animal.getImage())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                animal.setImage(url);

                                mref.child("Animal").child(animal.getAnimalId()).setValue(animal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        coin = coin + 5;

                                        mref.child("Users").child(animal.getUserId()).child("coin").setValue(coin).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RenderTestActivity.this, "Upload done !", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.INVISIBLE);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RenderTestActivity.this, "fail to increase coin !", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                                btnUpload.setVisibility(View.VISIBLE);

                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RenderTestActivity.this, "Upload fail !", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        btnUpload.setVisibility(View.VISIBLE);

                                    }
                                });
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RenderTestActivity.this,"Set animal's avatar fail !", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        btnUpload.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}

