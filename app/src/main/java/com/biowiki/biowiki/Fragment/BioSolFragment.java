package com.biowiki.biowiki.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biowiki.biowiki.R;

import java.util.ArrayList;

public class BioSolFragment extends Fragment {

    ArrayList<String> biosollist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biosol, null);

        biosollist = new ArrayList<>();

        return view;
    }
}
