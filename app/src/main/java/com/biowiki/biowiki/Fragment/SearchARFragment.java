package com.biowiki.biowiki.Fragment;

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
import android.widget.LinearLayout;

import com.biowiki.biowiki.Adapter.ModelAdapter;
import com.biowiki.biowiki.Models.Model;
import com.biowiki.biowiki.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchARFragment extends Fragment {

    DatabaseReference mref;

    EditText edtSearch;
    RecyclerView mResultList;

    ArrayList<Model> models, modelsSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_ar, null);

        mref = FirebaseDatabase.getInstance().getReference();

//        Model model = new Model("Duck", "https://firebasestorage.googleapis.com/v0/b/demo1-7937b.appspot.com/o/Models%2F42269878-1-f.jpg?alt=media&token=4ada8f29-1e36-47b7-a709-4a23b83ba6fc", "https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/Duck/glTF/Duck.gltf");
//        mref.child("Models").push().setValue(model);
        //mref.child("Models").push().setValue(new Model("duck", "https://firebasestorage.googleapis.com/v0/b/demo1-7937b.appspot.com/o/Models%2F42269878-1-f.jpg?alt=media&token=4ada8f29-1e36-47b7-a709-4a23b83ba6fc", "https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/Duck/glTF/Duck.gltf"));

        edtSearch = view.findViewById(R.id.SearchAR);
        mResultList = view.findViewById(R.id.resultArList);

        mResultList.setHasFixedSize(false);
        mResultList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mResultList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        mResultList.setVisibility(View.GONE);

        models = new ArrayList<>();
        modelsSearch = new ArrayList<>();

        edtSearch.addTextChangedListener(new TextWatcher() {
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
                } else {
                    mResultList.removeAllViews();
                    mResultList.setVisibility(View.INVISIBLE);
                }
            }
        });

        mref.child("Models").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                models.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Model model = snapshot.getValue(Model.class);
                    models.add(model);
                }

                setAdapter(edtSearch.getText().toString().toLowerCase());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void setAdapter(String searchKey) {
        modelsSearch.clear();

        for (Model model : models) if (model.getName().toLowerCase().contains(searchKey)) modelsSearch.add(model);

        ModelAdapter data = new ModelAdapter(getActivity(), modelsSearch);
        mResultList.setAdapter(data);
    }
}