package com.biowiki.biowiki;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.biowiki.biowiki.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity implements View.OnClickListener {

    EditText name, email, pass;
    Button btnReg;
    TextView mLoginPage;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ProgressDialog mDialog;

    String Name, Email, Pass, img,Dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        img = "https://firebasestorage.googleapis.com/v0/b/demo1-7937b.appspot.com/o/man.png?alt=media&token=19c6c846-241e-4766-ad34-8f1a27aa6446";
        Dec = "Chưa có mô tả nào về bản thân";

        name = (EditText) findViewById(R.id.edtName);
        email = (EditText) findViewById(R.id.edtEmail);
        pass = (EditText) findViewById(R.id.edtPass);
        mLoginPage = (TextView) findViewById(R.id.alReg);
        btnReg = (Button) findViewById(R.id.btnReg);

        mAuth = FirebaseAuth.getInstance();

        btnReg.setOnClickListener(this);
        mLoginPage.setOnClickListener(this);
        mDialog = new ProgressDialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void onClick(View view) {
        if (view == btnReg) {
            UserRegister();
        } else if (view == mLoginPage){
            finish();
        }
    }

    private void UserRegister() {
        Name = name.getText().toString().trim();
        Email = email.getText().toString().trim();
        Pass = pass.getText().toString().trim();

        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(register.this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Email)) {
            Toast.makeText(register.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (Pass.length() < 6) {
            Toast.makeText(register.this, "Password must be greater than 6 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        mDialog.setMessage("Creating user please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendEmailVerification();
                    mDialog.dismiss();

                    String userId = task.getResult().getUser().getUid();

                    mDatabase.child(userId).setValue(new User(Name, Email, Dec, img, userId, 0));

                    mAuth.signOut();
                    finish();
                } else {
                    Toast.makeText(register.this, "Error while creating user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmailVerification() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(register.this, "Check your Email for verification", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(register.this, "Can't send Email for verification\nPlease try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
