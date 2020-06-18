package com.biowiki.biowiki;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biowiki.biowiki.Models.Animal;
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AnimalView extends AppCompatActivity {
    static final String IS_INCREASE = "increase";
    static final String IS_DECREASE = "decrease";

    TextView txtName, txtUser, txtVote;
    ImageView avatar, imgIncrease, imgDecrease;

    FirebaseAuth mAuth;
    DatabaseReference mref;

    String userId, status = null;

    long vote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_view);

        mAuth = FirebaseAuth.getInstance();
        mref = FirebaseDatabase.getInstance().getReference();

        userId = mAuth.getCurrentUser().getUid();

        Animal animal = (Animal) getIntent().getSerializableExtra("Animal");

        txtName = findViewById(R.id.nameAnimal);
        txtUser = findViewById(R.id.animalSource);
        avatar = findViewById(R.id.avatarAnimal);
        txtVote = findViewById(R.id.txtVote);
        imgDecrease = findViewById(R.id.imgDecrease);
        imgIncrease = findViewById(R.id.imgIncrease);

        vote = animal.getVote();

        txtName.setText(animal.getName());
        txtUser.setText("From: " + animal.getUserName());
        Picasso.get().load(animal.getImage()).into(avatar);
        txtVote.setText(String.valueOf(vote));

        mref.child("Animal").child(animal.getAnimalId()).child("VoteList").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    status = dataSnapshot.getValue().toString();
                    if (status.equals(IS_INCREASE)) imgIncrease.setImageResource(R.drawable.isincreased);
                    else if (status.equals(IS_DECREASE)) imgDecrease.setImageResource(R.drawable.isdecreased);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imgDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == null) {
                    vote--;

                    mref.child("Animal").child(animal.getAnimalId()).child("vote").setValue(vote);
                    mref.child("Animal").child(animal.getAnimalId()).child("VoteList").child(userId).setValue(IS_DECREASE);

                    txtVote.setText(String.valueOf(vote));
                    imgDecrease.setImageResource(R.drawable.isdecreased);

                    status = IS_DECREASE;
                } else if (status.equals(IS_INCREASE)) {
                    vote--;

                    mref.child("Animal").child(animal.getAnimalId()).child("vote").setValue(vote);
                    mref.child("Animal").child(animal.getAnimalId()).child("VoteList").child(userId).removeValue();

                    txtVote.setText(String.valueOf(vote));
                    imgIncrease.setImageResource(R.drawable.notincreased);

                    status = null;
                } else {
                    Toast.makeText(AnimalView.this, "You've already voted", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == null) {
                    vote++;

                    mref.child("Animal").child(animal.getAnimalId()).child("vote").setValue(vote);
                    mref.child("Animal").child(animal.getAnimalId()).child("VoteList").child(userId).setValue(IS_INCREASE);

                    txtVote.setText(String.valueOf(vote));
                    imgIncrease.setImageResource(R.drawable.isincreased);

                    status = IS_INCREASE;
                } else if (status.equals(IS_DECREASE)) {
                    vote++;

                    mref.child("Animal").child(animal.getAnimalId()).child("vote").setValue(vote);
                    mref.child("Animal").child(animal.getAnimalId()).child("VoteList").child(userId).removeValue();

                    txtVote.setText(String.valueOf(vote));
                    imgDecrease.setImageResource(R.drawable.notdecreased);

                    status =  null;
                } else {
                    Toast.makeText(AnimalView.this, "You've already voted", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Rich text
        Editor renderer = (Editor) findViewById(R.id.previewRenderer);

        Map<Integer, String> headingTypeface = getHeadingTypeface();
        Map<Integer, String> contentTypeface = getContentface();
        renderer.setHeadingTypeface(headingTypeface);
        renderer.setContentTypeface(contentTypeface);
        renderer.setDividerLayout(R.layout.tmpl_divider_layout);
        renderer.setEditorImageLayout(R.layout.tmpl_image_view);
        renderer.setListItemLayout(R.layout.tmpl_list_item);

        EditorContent Deserialized = renderer.getContentDeserialized(animal.getDec());

        renderer.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {

            }

            @Override
            public void onUpload(Bitmap image, String uuid) {

            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> settings, int index) {
                return null;
            }
        });

        renderer.render(Deserialized);
    }

    public Map<Integer,String> getHeadingTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/GreycliffCF-Bold.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/GreycliffCF-Bold.ttf");
        return typefaceMap;
    }

    public Map<Integer,String> getContentface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL,"fonts/Lato-Medium.ttf");
        typefaceMap.put(Typeface.BOLD,"fonts/Lato-Bold.ttf");
        typefaceMap.put(Typeface.ITALIC,"fonts/Lato-MediumItalic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC,"fonts/Lato-BoldItalic.ttf");
        return typefaceMap;
    }
}
