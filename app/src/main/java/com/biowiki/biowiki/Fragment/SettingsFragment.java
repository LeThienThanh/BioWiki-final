package com.biowiki.biowiki.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.biowiki.biowiki.Ar;
import com.biowiki.biowiki.R;
import com.biowiki.biowiki.login;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    Button btnsignOut, btnAR;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);

        btnsignOut = view.findViewById(R.id.btnsignout);
        btnsignOut.setOnClickListener(this);

        btnAR = view.findViewById(R.id.btnAR);
        btnAR.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsignout:
                Toast.makeText(getActivity(), "Sign out", Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Do you want to Sign Out ?");
                alertDialogBuilder
                        .setMessage("Click yes to Sign Out!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mAuth.signOut();
                                        startActivity(new Intent(getActivity(), login.class));
                                        getActivity().finish();
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

            case R.id.btnAR:
                Intent intent = new Intent(getActivity(), Ar.class);
                startActivity(intent);
                break;
        }
    }
}
